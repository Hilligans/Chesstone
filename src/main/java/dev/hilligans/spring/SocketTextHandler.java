package dev.hilligans.spring;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;

import dev.hilligans.Main;
import dev.hilligans.game.Game;
import dev.hilligans.game.GameHandler;
import dev.hilligans.game.Player;
import dev.hilligans.game.PlayerHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketTextHandler extends TextWebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        InetSocketAddress address = session.getRemoteAddress();
       // String s = address.getHostName();// + new String(hardwareAddress);
       // Player player = Main.playerHandler.connect(s);
       // Main.gameHandler.joinGame(s,player);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        InetSocketAddress address = session.getRemoteAddress();
        address.getHostName();
        System.out.println(address.getHostName());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        try {
            System.out.println(session.getRemoteAddress().getHostName());

            String payload = message.getPayload();
            System.out.println(payload);
            JSONObject jsonObject = new JSONObject(payload);
            session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
    }

    public static String resolveID(WebSocketSession session) {
        return session.getUri().getPath().substring(Main.path.length());
    }

    public static Player getPlayer(WebSocketSession session, String name) {
        return Main.playerHandler.getPlayer(session.getRemoteAddress().getHostName() + ":" + name);
    }

    public void handlePacket(WebSocketSession session, JSONObject packet) {
        String packetID = packet.getString("type");
        if(packetID.equals("name")) {
            Player player = getPlayer(session, packet.getString("data"));
            String gameCode = resolveID(session);
            int result = Main.gameHandler.joinGame(gameCode,player);
            Game game = Main.gameHandler.getGame(gameCode);
            if(game != null) {
                JSONObject info = new JSONObject();
                JSONArray array = new JSONArray();
                array.put(game.player1.player.name).put(game.player2.player.name);
                info.put("names",array);
                info.put("in_progress",true);
                info.put("type", "info");
                game.sendPacketToPlayers(info);

                JSONObject game_start = new JSONObject();
                game_start.put("type", "game_start");
                game.sendPacketToPlayers(game_start);
            }
        }
    }

}