package dev.hilligans.game;

import dev.hilligans.board.IBoard;
import dev.hilligans.spring.packet.ClockPacket;
import dev.hilligans.spring.packet.GameOverPacket;
import dev.hilligans.spring.packet.GameStartPacket;
import dev.hilligans.storage.IGameResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public class NewGame implements IGame {

    IPlayer player1;
    IPlayer player2;
    IBoard board;
    String gameCode;

    final ArrayList<Spectator> spectators = new ArrayList<>();

    public float increment = 0;
    public boolean started = false;
    public int turn = 1;
    public boolean running = false;


    public NewGame(IBoard board) {
        this.gameCode = RandomStringUtils.random(6);
        this.board = board;
    }

    @Override
    public IGameResult getGameResult() {
        return null;
    }

    @Override
    public int addPlayer(GamePlayer gamePlayer) {
        return 0;
    }

    @Override
    public IViewer addPlayer(Account account, WebSocketSession session) {

        if(player1 == null) {
            Playerr player = new Playerr(this, account, 1).addSession(session);
            player1 = player;
            return player;
        } else {
            if(player1 instanceof Playerr player) {
                if(player.account.userID == account.userID && !player.account.anonymousAccount) {
                    //player.addSession(session);
                    //return null;
                }
            }
        }
        if(player2 == null) {
            Playerr player = new Playerr(this, account, 2).addSession(session);
            player2 = player;
            return player;
        } else {
            if(player2 instanceof Playerr player) {
                if(player.account.userID == account.userID && !player.account.anonymousAccount) {
                    player.addSession(session);
                    return player2;
                }
            }
        }
        Spectator spectator = new Spectator(this, session);
        synchronized (spectators) {
            spectators.add(spectator);
        }

        return spectator;
    }

    @Override
    public GamePlayer[] getGamePlayers() {
        return new GamePlayer[0];
    }

    @Override
    public IPlayer[] getPlayers() {
        return new IPlayer[]{player1, player2};
    }

    @Override
    public IPlayer getCurrentPlayersTurn() {
        return turn == 1 ? player1 : player2;
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
        return turn;
    }

    @Override
    public void startGame() {
        this.running = true;
        sendPacketToAll(new GameStartPacket().toEncodedPacket());
    }

    @Override
    public void swapTurns() {
        turn = turn == 1 ? 2 : 1;
        player1.endTurn(turn);
        player2.endTurn(turn);
        synchronized (spectators) {
            for(Spectator spectator : spectators) {
                spectator.endTurn(turn);
            }
        }
        ClockPacket clockPacket = new ClockPacket(player1.getRemainingTime(), player2.getRemainingTime());
        sendPacketToAll(clockPacket.toEncodedPacket());

        int code = IGame.getWinner(getBoard().getAlivePlayers());
        if(code == 1 || code == 2) {
            GameOverPacket packet = new GameOverPacket(code, "Player " + (code == 1 ? 1 : 0) + " ran out of time");
            sendPacketToAll(packet.toEncodedPacket());
            running = false;
        }
        handlerAfterMove();
    }

    public void handlerAfterMove() {
        board.update();
        for(int x = 0; x < 8; x++) {
            board.tick();
            int state = IGame.getWinner(board.getAlivePlayers());
            if(state == 1 || state == 2) {
                GameOverPacket packet = new GameOverPacket(state, "Player " + (state == 1 ? player1 : player2).getName() + " has checkmated player " + (state == 2 ? player1 : player2).getName());
                sendPacketToAll(packet.toEncodedPacket());
                running = false;
                return;
            }
        }
    }

    public void sendPacketToAll(String packet) {
        player1.sendPacketToViewer(packet);
        player2.sendPacketToViewer(packet);
        synchronized (spectators) {
            for(Spectator spectator : spectators) {
                spectator.sendPacketToViewer(packet);
            }
        }
    }
}
