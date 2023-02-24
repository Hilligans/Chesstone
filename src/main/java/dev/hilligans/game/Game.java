package dev.hilligans.game;

import dev.hilligans.Main;
import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.StateChangeMove;
import dev.hilligans.board.Piece;
import dev.hilligans.storage.GameResult;
import dev.hilligans.storage.IGameResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class Game implements IGame {

    public Board board;
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

    public Game(Board board) {
        gameCode = RandomStringUtils.random(6);
        this.board = board;
    }

    public void setPlayer1(GamePlayer player1) {
        this.player1 = player1;
    }

    public void setPlayer2(GamePlayer player2) {
        this.player2 = player2;
    }

    public void sendBoard(short[] board) {
        gameImplementation.sendDataToPlayer(board,this,player1);
        gameImplementation.sendDataToPlayer(board,this,player2);
        for(GamePlayer spectator : spectators) {
            gameImplementation.sendDataToPlayer(board,this,spectator);
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


    public void finishGame() {
        if(player1.player.callback != null) {
            player1.player.callback.run();
        }
        if(player2.player.callback != null) {
            player2.player.callback.run();
        }
    }

    public void handlerAfterMove() {
        board.update();
        for(int x = 0; x < 8; x++) {
            board.tick();
            int state = board.gameWin();
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
        data.put("winner", winner);
        data.put("reason", reason);
        jsonObject.put("data", data);
        sendPacketToPlayers(jsonObject);

        gameRunning = false;
        gameHandler.games.remove(gameCode);
        gameHandler.gamePlayers.remove(this);
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
}
