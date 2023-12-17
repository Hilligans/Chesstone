package dev.hilligans.spring.chesstone;

import java.io.IOException;

import dev.hilligans.Main;
import dev.hilligans.chesstone.board.IMove;
import dev.hilligans.chesstone.board.Move;
import dev.hilligans.chesstone.board.StateChangeMove;
import dev.hilligans.chesstone.game.*;
import dev.hilligans.spring.chesstone.packet.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.Cookie;

@Component
public class ChesstoneWebSocket extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        IGame game = Main.gameHandler.getGame(resolveID(session));
        if(game == null) {
            sendPacket(session, new JSONObject().put("type", "invalid_game").toString());
            session.close();
            return;
        }

        Cookie cookie = (Cookie)session.getAttributes().getOrDefault("chesstone_token", null);
        if(cookie != null) {
            Main.accountHandler.openSocket(session, cookie.getValue());
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        Account account = Main.accountHandler.closeSocket(session);
        if(account != null) {
            if(account.anonymousAccount) {
                synchronized (account.playerHashMap) {
                    for (Player player : account.playerHashMap.values()) {
                        player.resign("Disconnected");
                    }
                }
            }
        }
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

    public synchronized void handlePacket(WebSocketSession session, JSONObject packet) {
        if(Main.gameHandler.logPackets) {
            System.out.println("Incoming Packet: " + packet);
        }
        try {
            String packetID = packet.getString("type");
            switch (packetID) {
                case "name" -> {
                    Account account = Main.accountHandler.getAccount(session);
                    if(account == null) {
                        account = Main.accountHandler.createAnonymousAccount(session, packet.getString("data"));
                    }
                    String gameCode = resolveID(session);
                    IGame game = Main.gameHandler.getGame(gameCode);

                    synchronized (game) {
                        IViewer viewer = Main.gameHandler.joinGame(gameCode, account, session);
                        if (viewer == null) {
                            session.close();
                            return;
                        }
                        BoardPacket boardPacket = new BoardPacket(game.getBoard().getEncodedBoardList());
                        sendPacket(session, boardPacket.toEncodedPacket());
                        InfoPacket infoPacket = new InfoPacket(game, viewer);
                        sendPacket(session, infoPacket.toEncodedPacket());

                        if (viewer instanceof Player player) {
                            MovesPacket movesPacket = new MovesPacket(game, player.playerID);
                            if (!game.isStarted()) {
                                if (player.playerID == 2) {
                                    IPlayer player1 = game.getPlayers()[0];
                                    player1.sendPacketToViewer(infoPacket.toEncodedPacket());
                                    player1.sendPacketToViewer(movesPacket.toEncodedPacket());
                                    game.startGame();
                                }
                            } else {
                                IPlayer[] players = game.getPlayers();
                                sendPacket(session, new ClockPacket(players[0].getRemainingTime(), players[1].getRemainingTime()).toEncodedPacket());
                                if (game.getTurn() == player.playerID) {
                                    sendPacket(session, movesPacket.toEncodedPacket());
                                }
                            }
                        }
                    }
                }
                case "move" -> {
                    JSONObject obj = packet.getJSONObject("data");
                    IGame game = Main.gameHandler.getGame(resolveID(session));

                    if (game == null) {
                        session.close();
                        return;
                    }

                    synchronized (game) {
                        IPlayer pl = game.getCurrentPlayersTurn();
                        if (pl instanceof Player player) {
                            if (player.hasSession(session)) {
                                int type = obj.optInt("type", -1);
                                if (type != -1) {
                                    int x = obj.getInt("idx") & 0b111;
                                    int y = (obj.getInt("idx") >> 3) & 0b111;
                                    System.out.println("Move: " + x + " " + y + " type " + type);
                                    IMove move;
                                    if (type == 0) {
                                        move = new Move(x, y, obj.getInt("move") & 0b111, (obj.getInt("move") >> 3) & 0b111, player.playerID);
                                    } else if (type == 1 || type == 2) {
                                        move = new StateChangeMove(x, y, obj.getInt("move"), type, player.playerID);
                                    } else {
                                        throw new RuntimeException();
                                    }
                                    MoveResult moveResult = move.makeMove(game);
                                    if (moveResult.isValid()) {
                                        game.addMove(move);
                                        sendPacket(session, new MoveAckPacket(true).toEncodedPacket());
                                        game.sendPacketToAll(packet.toString());
                                        if(moveResult.packetsToSend == null) {
                                            game.swapTurns();
                                        } else {
                                            for(String pack : moveResult.packetsToSend) {
                                                game.getPlayer(session).sendPacketToViewer(pack);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sendPacket(session, new MoveAckPacket(false).toEncodedPacket());
                }

                case "resign" -> {
                    IGame game = Main.gameHandler.getGame(resolveID(session));
                    synchronized (game) {
                        if(game.isRunning()) {
                            IPlayer player = game.getPlayer(session);
                            if(player != null) {
                                player.resign("Resigned");
                            }
                        }

                        //Game game = Main.gameHandler.getGame(resolveID(session));
                        //GamePlayer player = Main.gameHandler.getPlayer(session);
                        //game.resign(player.playerID);
                    }
                }

                case "chat" -> {
                    String message = packet.getString("data");
                    IGame game = Main.gameHandler.getGame(resolveID(session));
                    synchronized (game) {
                        IPlayer player = game.getPlayer(session);
                        if(player != null) {
                            game.sendPacketToAll(new ChatPacket(player, message).toEncodedPacket());
                        }
                    }
                    /*

                    GamePlayer player = Main.gameHandler.getPlayer(session);
                    if(player != null) {
                        int id = player.playerID - 1;
                        if(id >= 0) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", "chat");
                            JSONObject data = new JSONObject();
                            jsonObject.put("data", data);
                            data.put("author", id);
                            data.put("message", message);
                            Game game = Main.gameHandler.getGame(resolveID(session));
                            if(game != null) {
                                game.sendCheckedPacketToPlayers(jsonObject);
                            }
                        }
                    }

                     */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(WebSocketSession session, JSONObject packet) {
        if(Main.gameHandler.logPackets) {
            System.out.println("Outgoing Packet: " + packet);
        }
        try {
            session.sendMessage(new TextMessage(packet.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendPacket(WebSocketSession session, String packet) {
        if(Main.gameHandler.logPackets) {
            System.out.println("Outgoing Packet: " + packet);
        }
        try {
            session.sendMessage(new TextMessage(packet));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

