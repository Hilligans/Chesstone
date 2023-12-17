package dev.hilligans.chesstone.game;

import dev.hilligans.spring.chesstone.ChesstoneWebSocket;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public class GamePlayer {

    //public Player player;
    public GameHandler handler;
 //   public Game game;

    public ArrayList<WebSocketSession> sessions = new ArrayList<>();
    public long identifier;
    public int playerID;

    public long time = -1;
    public long timeMark = 0;

    //public GamePlayer(Player player, long identifier) {
       // this.player = player;
    //    this.identifier = identifier;
   // }

    //public GamePlayer setGame(Game game) {
      //  this.game = game;
   //     return this;
    //}

    /**
     *
     */

    /*
    public GamePlayer setTime(long time) {
        this.time = time;
        return this;
    }

     */

    public synchronized long getTime() {
        long remaining = time;
        if(remaining < 0) {
            remaining = 0;
        }
        return remaining;
    }

    public synchronized boolean swapTime(boolean myTurn, float increment) {
        long currentTime = System.currentTimeMillis();
        if(timeMark != 0) {
            long dif = timeMark - currentTime;
            time += dif;
        }
        if(getTime() == 0) {
            return false;
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


    public synchronized void sendPacket(JSONObject packet) {
        sessions.removeIf(session -> !session.isOpen());
        for(WebSocketSession session : sessions) {
            if(session.isOpen()) {
                ChesstoneWebSocket.sendPacket(session, packet);
            }
        }
    }

    public synchronized void addSession(WebSocketSession session, GameHandler gameHandler) {
        if(!sessions.contains(session)) {
            sessions.add(session);
            this.handler = gameHandler;
            //handler.players.put(session, this);
        }
    }
}
