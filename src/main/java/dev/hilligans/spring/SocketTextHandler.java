package dev.hilligans.spring;

import java.io.IOException;

import dev.hilligans.Main;
import dev.hilligans.board.IMove;
import dev.hilligans.board.Move;
import dev.hilligans.board.MoveAndChangeState;
import dev.hilligans.board.StateChangeMove;
import dev.hilligans.game.*;
import dev.hilligans.spring.packet.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.Cookie;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        Game game = Main.gameHandler.getGame(resolveID(session));
        if(game == null) {
            sendPacket(session, new JSONObject().put("type", "invalid_game"));
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
                    for (Playerr player : account.playerHashMap.values()) {
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

    public static Player getPlayer(WebSocketSession session, String name) {
        Player player = Main.playerHandler.getPlayer(session.getRemoteAddress().getHostName() + ":" + name);
        if(player == null) {
            player = new Player(name);
            Main.playerHandler.players.put(session.getRemoteAddress().getHostName() + ":" + name, player);
        }
        return player;
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

                        if (viewer instanceof Playerr player) {
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


                    /*

                    Player player = getPlayer(session, packet.getString("data"));
                    System.out.println(player);
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

                    JSONArray names = new JSONArray();
                    JSONArray ids = new JSONArray();

                    int index = 0;
                    for(GamePlayer gamePlayer : game.getGamePlayers()) {
                        if(gamePlayer != null) {
                            names.put(index, gamePlayer.player.name);
                            ids.put(index, gamePlayer.identifier);
                        } else {
                            names.put(index, "");
                            ids.put(index, -1);
                        }
                        index++;
                    }
                    data.put("names", names);
                    data.put("ids", ids);

                    data.put("in_progress", result != 1);
                    data.put("player", result == 1 ? 0 : result == 2 ? 1 : 2);
                    info.put("type", "info");
                    sendPacket(session,info);

                    short[] board = game.board.getEncodedBoardList();
                    sendDataToPlayer(board,game,session);
                    if(game.gameRunning) {
                        if(pl.playerID == 1 || pl.playerID == 2) {
                            game.gameImplementation.sendMoveListToPlayer(game,pl);
                        }
                    }

                    if (result == 2) {
                        data.put("player", 0);
                        game.player1.sendPacket(info);
                        game.startGame();
                    }
                    */
                }
                case "move" -> {
                    JSONObject obj = packet.getJSONObject("data");
                    IGame game = Main.gameHandler.getGame(resolveID(session));

                    if(game == null) {
                        session.close();
                        return;
                    }

                    synchronized (game) {
                        IPlayer pl = game.getCurrentPlayersTurn();
                        bad_move:
                        {
                            if (pl instanceof Playerr player) {
                                if (player.hasSession(session)) {
                                    int type = obj.optInt("type", -1);
                                    if (type != -1) {
                                        int x = obj.getInt("idx") & 0b111;
                                        int y = (obj.getInt("idx") >> 3) & 0b111;
                                        System.out.println("Move: " + x + " " + y + " type " + type);
                                        IMove move;
                                        if (type == 0) {
                                            move = new Move(x, y, obj.getInt("move") & 0b111, (obj.getInt("move") >> 3) & 0b111);
                                        } else if(type == 1 || type == 2) {
                                            move = new StateChangeMove(x, y, obj.getInt("move"), type);
                                        } else {
                                            move = new MoveAndChangeState(0, 0, 0, 0);
                                            throw new RuntimeException();
                                        }
                                        if(move.makeMove(game)) {
                                            sendPacket(session, new MoveAckPacket(true).toEncodedPacket());
                                            game.sendPacketToAll(packet.toString());
                                            game.swapTurns();
                                        }
                                    }
                                }
                            }
                        }
                        sendPacket(session, new MoveAckPacket(false).toEncodedPacket());
                    }
                    /*

                    if (game != null) {
                        GamePlayer player = Main.gameHandler.getPlayer(session);
                        System.out.println(player);
                        int type = obj.optInt("type", -1);

                        out:
                        {
                            if ((game.turn == 1 && game.player1 == player || game.turn == 2 && game.player2 == player) && type != -1) {
                                int x = obj.getInt("idx") & 0b111;
                                int y = (obj.getInt("idx") >> 3) & 0b111;
                                System.out.println("Move: " + x + " " + y + " type " + type);
                                IMove move;
                                if (type == 0) {
                                    move = new Move(x, y, obj.getInt("move") & 0b111, (obj.getInt("move") >> 3) & 0b111);
                                } else if(type == 1 || type == 2) {
                                    move = new StateChangeMove(x, y, obj.getInt("move"), type);
                                } else {
                                    move = new MoveAndChangeState(0, 0, 0, 0);
                                    throw new RuntimeException();
                                }
                                if(move.makeMove(game)) {
                                    game.handlerAfterMove();
                                    break out;
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
                        game.sendBoard();
                        game.sendMoveList();
                        if (game.gameCode.equals(game.gameHandler.snooper)) {
                            System.out.println(game.getBoard());
                        }
                    } else {
                        System.out.println("Invalid Move");
                    }

                     */
                }

                case "resign" -> {
                    Game game = Main.gameHandler.getGame(resolveID(session));
                    GamePlayer player = Main.gameHandler.getPlayer(session);
                    game.resign(player.playerID);
                }

                case "chat" -> {
                    String message = packet.getString("data");
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

