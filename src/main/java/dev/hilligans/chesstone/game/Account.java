package dev.hilligans.chesstone.game;

import dev.hilligans.chesstone.storage.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;

public class Account {

    public long userID;
    public String username;
    public boolean anonymousAccount = false;

    public final ArrayList<WebSocketSession> activeSessions = new ArrayList<>();
    public final HashMap<WebSocketSession, Player> playerHashMap = new HashMap<>();

    public Account(User user) {
        this.userID = user.id;
        this.username = user.username;
    }

    public Account(String username) {
        this.username = username;
        this.anonymousAccount = true;
    }

    public void addSession(WebSocketSession session) {
        synchronized (activeSessions) {
            this.activeSessions.add(session);
        }
    }

    public void removeSession(WebSocketSession session) {
        synchronized (activeSessions) {
            this.activeSessions.remove(session);
        }
    }

    public void addGame(WebSocketSession session, Player player) {
        synchronized (playerHashMap) {
            this.playerHashMap.put(session, player);
        }
    }

    public String getName() {
        if(anonymousAccount) {
            return "Anonymous User";
        }
        return username;
    }
}
