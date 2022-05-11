package dev.hilligans.spring;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ExecutorService;

import dev.hilligans.Main;
import dev.hilligans.board.Move;
import dev.hilligans.board.OtherMove;
import dev.hilligans.game.*;
import org.jetbrains.annotations.NotNull;
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
      //  InetSocketAddress address = session.getRemoteAddress();
       // String s = address.getHostName();// + new String(hardwareAddress);
       // Player player = Main.playerHandler.connect(s);
       // Main.gameHandler.joinGame(s,player);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
     //   InetSocketAddress address = session.getRemoteAddress();
      //  address.getHostName();
      //  System.out.println(address.getHostName());
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
    }

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message)
            throws InterruptedException, IOException {
        try {
            JSONObject jsonObject = new JSONObject(message.getPayload());
            handlePacket(session,jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
    }

    public static String resolveID(WebSocketSession session) {
        return session.getUri().getPath().substring(Main.path.length());
    }

    public static Player getPlayer(WebSocketSession session, String name) {
        Player player = Main.playerHandler.getPlayer(session.getRemoteAddress().getHostName() + ":" + name);
        if(player == null) {
            player = new Player(name);
            Main.playerHandler.players.put(session.getRemoteAddress().getHostName() + ":" + name, player);
        }
        return player;
    }

    public synchronized void handlePacket(WebSocketSession session, JSONObject packet) {
        try {
            String packetID = packet.getString("type");
            switch (packetID) {
                case "name" -> {
                    Player player = getPlayer(session, packet.getString("data"));
                    System.out.println(player);
                    String gameCode = resolveID(session);
                    int result = Main.gameHandler.joinGame(gameCode, player, session);
                    Game game = Main.gameHandler.getGame(gameCode);
                    GamePlayer pl = Main.gameHandler.getPlayer(session);

                    if(game == null) {
                        return;
                    }
                    if(result == -1) {
                        return;
                    }

                    JSONObject info = new JSONObject();
                    JSONObject data = new JSONObject();
                    info.put("data",data);
                    JSONArray array = new JSONArray();
                    array.put(game.player1.player.name).put(game.player2 != null ? game.player2.player.name : "");
                    data.put("names", array);
                    data.put("in_progress", result != 1);
                    data.put("player", result == 1 ? 0 : result == 2 ? 1 : 2);
                    info.put("type", "info");
                    sendPacket(session,info);

                    short[] board = game.board.getBoardList();
                    sendDataToPlayer(board,game,session);

                    if(game.gameRunning) {
                        if(pl.playerID == 1 || pl.playerID == 2) {
                            game.gameImplementation.sendMoveListToPlayer(game,pl);
                        }
                    }

                    if (result == 2) {
                        game.player1.sendPacket(info);
                        game.startGame();
                    }
                }
                case "move" -> {
                    JSONObject obj = packet.getJSONObject("data");
                    Game game = Main.gameHandler.getGame(resolveID(session));
                    GamePlayer player = Main.gameHandler.getPlayer(session);
                    System.out.println(player);
                    int type = obj.optInt("type", -1);

                    out:
                    {
                        if ((game.turn == 1 && game.player1 == player || game.turn == 2 && game.player2 == player) && type != -1) {
                            int x = obj.getInt("idx") & 0b111;
                            int y = (obj.getInt("idx") >> 3) & 0b111;
                            System.out.println("Move: " + x + " " + y + " type " + type);
                            if (type == 0) {
                                Move move = new Move(x, y, obj.getInt("move") & 0b111, (obj.getInt("move") >> 3) & 0b111);
                                if (game.makeMove(move)) {
                                    break out;
                                }
                            } else {
                                OtherMove move = new OtherMove(x, y, obj.getInt("move"), type);
                                if (game.makeMove(move)) {
                                    break out;
                                }
                            }
                        }
                        JSONObject response = new JSONObject();
                        response.put("type", "move_ack");
                        response.put("data", false);
                        sendPacket(session, response);
                        return;
                    }
                    JSONObject response = new JSONObject();
                    response.put("type", "move_ack");
                    response.put("data", true);
                    sendPacket(session, response);
                    game.sendPacketToPlayers(packet);

                    game.swapTurn();
                    short[] board = game.board.getBoardList();
                    game.sendBoard(board);
                    game.sendMoveList();
                }

                case "resign" -> {
                    Game game = Main.gameHandler.getGame(resolveID(session));
                    GamePlayer player = Main.gameHandler.getPlayer(session);
                    game.resign(player.playerID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(WebSocketSession session, JSONObject packet) {
        try {
            session.sendMessage(new TextMessage(packet.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendDataToPlayer(short[] board, Game game, WebSocketSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONArray data = new JSONArray();
        data.putAll(board);
        jsonObject.put("type", "board");
        jsonObject.put("data",data);
        jsonObject.put("turn", game.turn);
        sendPacket(session, jsonObject);
    }
}

