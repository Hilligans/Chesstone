package dev.hilligans.game;

import dev.hilligans.Main;
import dev.hilligans.board.*;
import dev.hilligans.storage.GameResult;
import dev.hilligans.storage.IGameResult;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenCustomHashMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public class Game implements IGame {

    public IBoard board;
    public GameImplementation gameImplementation = new StandardGame();
    public GamePlayer player1;
    public GamePlayer player2;
    public ArrayList<GamePlayer> spectators = new ArrayList<>();

    public boolean gameRunning = false;

    public boolean started;
    public boolean gamePublic = true;
    public int turn = 1;
    public String gameCode;
    public String gameName = "";
    public float increment;

    public GameHandler gameHandler = Main.gameHandler;

    public Game(IBoard board) {
        this.gameCode = RandomStringUtils.random(6);
        this.board = board;
    }


    public void sendBoard() {
        gameImplementation.sendDataToPlayer(this,player1);
        gameImplementation.sendDataToPlayer(this,player2);
        for(GamePlayer spectator : spectators) {
            gameImplementation.sendDataToPlayer(this,spectator);
        }
    }

    public void update() {
        ArrayList<GamePlayer> removeList = new ArrayList<>();
        for(GamePlayer spectator : spectators) {
            if(!spectator.player.alive) {
                removeList.add(spectator);
            }
        }
        spectators.removeAll(removeList);
    }

    public void sendMoveList() {
        if(turn == 1) {
            gameImplementation.sendMoveListToPlayer(this,player1);
        } else {
            gameImplementation.sendMoveListToPlayer(this,player2);
        }
    }

    public void sendPacketToPlayers(JSONObject jsonObject) {
        try {
            player1.sendPacket(jsonObject);
            player2.sendPacket(jsonObject);
            for(GamePlayer gamePlayer : spectators) {
                gamePlayer.sendPacket(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void sendCheckedPacketToPlayers(JSONObject jsonObject) {
        try {
            if(player1 != null) {
                player1.sendPacket(jsonObject);
            }
            if(player2 != null) {
                player2.sendPacket(jsonObject);
            }
            for(GamePlayer gamePlayer : spectators) {
                gamePlayer.sendPacket(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    public void handlerAfterMove() {
        board.update();
        for(int x = 0; x < 8; x++) {
            board.tick();
            int state = IGame.getWinner(board.getAlivePlayers());
            if(state == 1 || state == 2) {
                String winner = state == 1 ? player2.player.name : player1.player.name;
                String looser = state == 1 ? player1.player.name : player2.player.name;
                endGame(state, winner + " has checkmated " + looser);
                return;
            }
        }
    }

    public void swapTurn() {
        turn = turn == 1 ? 2 : 1;
        boolean pl1Time = player1.swapTime(turn == 1, increment);
        boolean pl2Time = player2.swapTime(turn == 2, increment);
        if(!pl1Time) {
            endGame(2, "Player 1 ran out of time");
        } else if(!pl2Time) {
            endGame(1, "Player 2 ran out of time");
        }
    }

    public boolean addSpectator(GamePlayer gamePlayer) {
        if(gameRunning) {
            if (gamePublic) {
                spectators.add(gamePlayer);
                return true;
            }
        }
        return false;
    }

    public void resign(int player) {
        if(gameRunning || !started) {
            if (player == 1) {
                endGame(2, "Player 1 resigned.");
            } else if (player == 2) {
                endGame(1, "Player 2 resigned.");
            }
        }
    }

    public void disconnect(int player) {
        if(gameRunning || !started) {
            if (player == 1) {
                endGame(2, "Opponent Disconnected");
            } else if (player == 2) {
                endGame(1, "Opponent Disconnected");
            }
        }
    }

    public void endGame(int winner, String reason) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "game_over");
        JSONObject data = new JSONObject();
        data.put("winner", winner - 1);
        data.put("reason", reason);
        jsonObject.put("data", data);
        sendPacketToPlayers(jsonObject);

        gameRunning = false;
        gameHandler.endGame(this);
    }

    public void startGame() {
        gameRunning = true;
        started = true;
        JSONObject game_start = new JSONObject();
        game_start.put("type", "game_start");
        sendPacketToPlayers(game_start);
        sendMoveList();
    }

    @Override
    public IGameResult getGameResult() {
        GameResult gameResult = new GameResult();
        gameResult.user1 = 0;
        gameResult.user2 = 0;
        return gameResult;
    }

    @Override
    public int addPlayer(GamePlayer gamePlayer) {
        int pl = 0;
        if(gamePlayer.identifier != 0 || Main.allowUnauthorizedPlayersToPlayGames) {
            if (player1 == null) {
                player1 = gamePlayer;
                pl = 1;
            } else if (player2 == null) {
                player2 = gamePlayer;
                pl = 2;
            }
        }
        if(!gamePublic) {
            return -1;
        }
        gamePlayer.playerID = pl;
        return pl;
    }

    @Override
    public IViewer addPlayer(Account account, WebSocketSession session) {
        return null;
    }

    @Override
    public GamePlayer[] getGamePlayers() {
        return new GamePlayer[] {player1, player2};
    }

    @Override
    public IPlayer[] getPlayers() {
        return new IPlayer[0];
    }

    @Override
    public IPlayer getCurrentPlayersTurn() {
        return null;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public String getGameCode() {
        return gameCode;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public float getIncrement() {
        return increment;
    }

    @Override
    public int getTurn() {
        return 0;
    }

    @Override
    public void swapTurns() {

    }

    @Override
    public void sendPacketToAll(String packet) {

    }
}
