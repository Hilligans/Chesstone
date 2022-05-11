package dev.hilligans.game;

import dev.hilligans.spring.SocketTextHandler;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public class GamePlayer {

    public Player player;
    public GameHandler handler;
    public Game game;

    public ArrayList<WebSocketSession> sessions = new ArrayList<>();
    public int playerID;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public synchronized void sendPacket(JSONObject packet) {
        sessions.removeIf(session -> !session.isOpen());
        for(WebSocketSession session : sessions) {
            if(session.isOpen()) {
                SocketTextHandler.sendPacket(session, packet);
            }
        }
    }

    public synchronized void addSession(WebSocketSession session, GameHandler gameHandler) {
        sessions.add(session);
        this.handler = gameHandler;
        handler.players.put(session,this);
    }
}
