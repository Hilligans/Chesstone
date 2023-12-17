package dev.hilligans.chesstone.game;

import dev.hilligans.chesstone.board.IBoard;
import dev.hilligans.chesstone.board.IMove;
import dev.hilligans.chesstone.storage.GameResult;
import dev.hilligans.chesstone.storage.IGameResult;
import dev.hilligans.spring.chesstone.packet.ClockPacket;
import dev.hilligans.spring.chesstone.packet.GameOverPacket;
import dev.hilligans.spring.chesstone.packet.GameStartPacket;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public class NewGame implements IGame {

    IPlayer player1;
    IPlayer player2;
    IBoard board;
    String gameCode;

    final ArrayList<Spectator> spectators = new ArrayList<>();
    public final ArrayList<IMove> moves = new ArrayList<>();

    public float increment = 0;
    public boolean started = false;
    public int turn = 1;
    public boolean running = false;
    public boolean isPublic = true;
    public String gameName = "";
    public int winner;


    public NewGame(IBoard board) {
        this.gameCode = RandomStringUtils.random(6);
        this.board = board;
    }

    public NewGame(IBoard board, String gameCode) {
        this.board = board;
        this.gameCode = gameCode;
    }

    @Override
    public IGameResult getGameResult() {
        GameResult gameResult = new GameResult();
        if(player1 instanceof Player player) {
            gameResult.user1 = player.account.userID;
        }
        if(player2 instanceof Player player) {
            gameResult.user2 = player.account.userID;
        }
        gameResult.winner = winner;
        gameResult.moves = moves;

        return gameResult;
    }

    @Override
    public IViewer addPlayer(Account account, WebSocketSession session) {

        if(player1 == null) {
            Player player = new Player(this, account, 1).addSession(session);
            player1 = player;
            return player;
        } else {
            if(player1 instanceof Player player) {
                if(player.account.userID == account.userID && !player.account.anonymousAccount) {
                    //player.addSession(session);
                    //return null;
                }
            }
        }
        if(player2 == null) {
            Player player = new Player(this, account, 2).addSession(session);
            player2 = player;
            return player;
        } else {
            if(player2 instanceof Player player) {
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
    public void addMove(IMove move) {
        synchronized (moves) {
            this.moves.add(move);
        }
    }

    @Override
    public ArrayList<IMove> getMovesList() {
        return moves;
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
    public synchronized void endGame(int winner, String reason) {
        this.running = false;
        this.winner = winner;
    }

    @Override
    public void swapTurns() {
        handlerAfterMove();

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
            endGame(code, "Player " + (code == 1 ? 1 : 0) + " ran out of time");
        }
    }

    public void handlerAfterMove() {
        board.update();
        for(int x = 0; x < 8; x++) {
            board.tick();
            int state = IGame.getWinner(board.getAlivePlayers());
            if(state == 1 || state == 2) {
                GameOverPacket packet = new GameOverPacket(state, "Player " + (state == 1 ? player1 : player2).getName() + " has checkmated player " + (state == 2 ? player1 : player2).getName());
                sendPacketToAll(packet.toEncodedPacket());
                endGame(state, "Player " + (state == 1 ? player1 : player2).getName() + " has checkmated player " + (state == 2 ? player1 : player2).getName());
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

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public void setName(String name) {
        this.gameName = name;
    }

    @Override
    public synchronized IPlayer getPlayer(WebSocketSession session) {
        if(player1 instanceof Player player) {
            if(player.hasSession(session)) {
                return player;
            }
        } else if (player2 instanceof Player player) {
            if (player.hasSession(session)) {
                return player;
            }
        }
        return null;
    }
}
