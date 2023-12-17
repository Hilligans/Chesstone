package dev.hilligans.chesstone.game;

import dev.hilligans.spring.chesstone.ChesstoneWebSocket;
import dev.hilligans.spring.chesstone.packet.BoardPacket;
import dev.hilligans.spring.chesstone.packet.MovesPacket;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public class Player implements IPlayer {

    public Account account;
    public IGame game;
    public boolean alive = true;
    public long time = -1;
    public long timeMark = 0;
    public int playerID;

    public Player(IGame game, Account account, int playerID) {
        this.game = game;
        this.account = account;
        this.playerID = playerID;
    }

    public final ArrayList<WebSocketSession> sessions = new ArrayList<>();

    public synchronized Player addSession(WebSocketSession session) {
        this.sessions.add(session);
        return this;
    }

    public synchronized boolean hasSession(WebSocketSession session) {
        return sessions.contains(session);
    }

    public synchronized Player setTime(long time) {
        this.time = time;
        return this;
    }

    public synchronized long getTime() {
        long remaining = time;
        if(remaining < 0) {
            remaining = 0;
        }
        return remaining;
    }

    public synchronized boolean swapTime(boolean myTurn, float increment) {
        long currentTime = System.currentTimeMillis();
        if(time == -1) {
            return true;
        }

        if(timeMark != 0) {
            long dif = timeMark - currentTime;
            time += dif;
            if(time <= 0) {
                time = 0;
                return false;
            }
        }
        if(myTurn) {
            timeMark = currentTime;
        } else {
            timeMark = 0;
            time += increment * 1000;
        }
        return true;
    }

    public synchronized boolean peekTime() {
        long currentTime = System.currentTimeMillis();
        if(timeMark != 0) {
            long dif = timeMark - currentTime;
            if(time + dif < 0) {
                return false;
            }
        }
        return true;
    }

    public Player(IGame game) {
        this.game = game;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public int playerID() {
        return playerID;
    }

    @Override
    public void endTurn(int nextTurn) {
        sendPacketToViewer(new BoardPacket(game.getBoard().getEncodedBoardList()).toEncodedPacket());
        if(!swapTime(nextTurn != playerID, game.getIncrement())) {
            alive = false;
        }
        if(nextTurn == playerID) {
            sendPacketToViewer(new MovesPacket(game, playerID).toEncodedPacket());
        }
    }

    @Override
    public void sendPacketToViewer(String packet) {
        synchronized (sessions) {
            for(WebSocketSession session : sessions) {
                ChesstoneWebSocket.sendPacket(session, packet);
            }
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void resign(String reason) {
        alive = false;
        //TODO add game resigning
    }

    @Override
    public int getRemainingTime() {
        return (int)time;
    }

    @Override
    public String getName() {
        return account.getName();
    }
}
