package dev.hilligans.game;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public class Player {

    public String name;
    public ArrayList<WebSocketSession> sessions = new ArrayList<>();
    public Runnable callback;
    public boolean online = true;
    public boolean alive = true;

    public String identifier;

    public Player(String identifier) {
        this.identifier = identifier;
    }

}
