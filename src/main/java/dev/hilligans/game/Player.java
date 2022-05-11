package dev.hilligans.game;

import dev.hilligans.spring.SocketTextHandler;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Player {

    public String name;
    public Runnable callback;
    public boolean online = true;
    public boolean alive = true;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
