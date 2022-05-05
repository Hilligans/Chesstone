package dev.hilligans.game;

import netscape.javascript.JSObject;
import org.springframework.web.socket.WebSocketSession;

public class GamePlayer {

    public Player player;
    public WebSocketSession session;
    public Game game;
    int playerID;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void sendBoard(JSObject packet) {

    }
}
