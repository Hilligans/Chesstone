package dev.hilligans.spring;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;

import dev.hilligans.Main;
import dev.hilligans.game.GameHandler;
import dev.hilligans.game.Player;
import dev.hilligans.game.PlayerHandler;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    public static PlayerHandler playerHandler = new PlayerHandler();
    public static GameHandler gameHandler = new GameHandler();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String key = session.getUri().getPath().substring(Main.path.length());
        System.out.println(key);
        InetSocketAddress address = session.getRemoteAddress();
        NetworkInterface ni = NetworkInterface.getByInetAddress(address.getAddress());
        byte[] hardwareAddress = ni.getHardwareAddress();
        String s = address.getHostName();// + new String(hardwareAddress);
        playerHandler.connect(s);
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

    public void handlePacket(JSONObject packet, Player player) {
        String packetID = packet.getString("type");
        if(packetID.equals("join")) {
            //find the game code somehow
            String gameCode = "";
            int result = gameHandler.joinGame(gameCode,player);
        }
    }

}