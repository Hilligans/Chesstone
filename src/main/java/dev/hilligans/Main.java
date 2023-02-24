package dev.hilligans;

import dev.hilligans.ai.SimpleMoveCalculator;
import dev.hilligans.board.*;
import dev.hilligans.board.pieces.*;
import dev.hilligans.game.Game;
import dev.hilligans.game.GameHandler;
import dev.hilligans.game.PlayerHandler;
import dev.hilligans.spring.SpringHandler;
import dev.hilligans.storage.Database;
import dev.hilligans.storage.GameResult;
import dev.hilligans.util.ArgumentContainer;
import dev.hilligans.util.ConsoleParser;
import dev.hilligans.util.ConsoleReader;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Main {

    public static GameHandler gameHandler = new GameHandler();
    public static PlayerHandler playerHandler = new PlayerHandler();
    public static String path = "/chesstone/play/";
    public static final int START_USER_RATING = 0;
    public static ArgumentContainer argumentContainer;

    public static final int tokenExpiryTime = 604800;

    public static void main(String[] args) {
        argumentContainer = new ArgumentContainer(args);

        new Thread(() -> SpringHandler.run(args)).start();
        SimpleMoveCalculator simpleMoveCalculator = new SimpleMoveCalculator(3);

        ConsoleParser consoleParser = new ConsoleParser();
        consoleParser.addCommand("stop", "Stops the server", 0, s -> System.exit(0));
        consoleParser.addCommand("data", "Gets the data of a piece of a certain game", 3, args1 -> {
            try {
                int x = Integer.parseInt(args1[1]);
                int y = Integer.parseInt(args1[2]);
                Game game = gameHandler.getGame(args1[0]);
                Piece piece = game.board.getPiece(x, y);
                System.out.println(piece);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        consoleParser.addCommand("update", "Updates a piece on a specified board", 3, new Consumer<String[]>() {
            @Override
            public void accept(String[] strings) {

            }
        });


        new ConsoleReader(s -> {
            try {
                if (s.equals("stop")) {
                    System.exit(0);
                } else if (s.startsWith("data")) {
                    try {
                        s = s.substring(5);
                        String[] vals = s.split(" ");
                        int x = Integer.parseInt(vals[1]);
                        int y = Integer.parseInt(vals[2]);
                        Game game = gameHandler.getGame(vals[0]);
                        Piece piece = game.board.getPiece(x, y);
                        System.out.println(piece);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (s.startsWith("update")) {
                    try {
                        s = s.substring(7);
                        String[] vals = s.split(" ");
                        int x = Integer.parseInt(vals[1]);
                        int y = Integer.parseInt(vals[2]);
                        Game game = gameHandler.getGame(vals[0]);
                        Piece piece = game.board.getPiece(x, y);
                        piece.update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (s.startsWith("tick")) {
                    try {
                        s = s.substring(5);
                        String[] vals = s.split(" ");
                        int x = Integer.parseInt(vals[1]);
                        int y = Integer.parseInt(vals[2]);
                        Game game = gameHandler.getGame(vals[0]);
                        Piece piece = game.board.getPiece(x, y);
                        for (int a = 0; a < 4; a++) {
                            piece.tick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (s.startsWith("log-updates")) {
                    gameHandler.logUpdates = !gameHandler.logUpdates;
                    if (gameHandler.logUpdates) {
                        System.out.println("Enabled logging all game updates");
                    } else {
                        System.out.println("Disabled logging all game updates");
                    }
                } else if (s.startsWith("log-packets")) {
                    gameHandler.logPackets = !gameHandler.logPackets;
                    if (gameHandler.logPackets) {
                        System.out.println("Enabled logging all packets");
                    } else {
                        System.out.println("Disabled logging all packets");
                    }
                } else if (s.startsWith("snoop")) {
                    gameHandler.snooper = s.split(" ", 2)[1].strip();
                    System.out.println("Snooping " + gameHandler.snooper);
                } else if (s.startsWith("message")) {
                    String[] vals = s.split(" ", 3);
                    String code = vals[1].split("\\.")[0];
                    int player = Integer.parseInt(vals[1].split("\\.")[1]);
                    Game game = gameHandler.getGame(code);
                    if (game != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", "server_message");
                        jsonObject.put("data", vals[2]);
                        if (player == 1) {
                            game.player1.sendPacket(jsonObject);
                        } else if (player == 2) {
                            game.player2.sendPacket(jsonObject);
                        }
                    }
                } else if(s.startsWith("purge-tokens")) {
                    Database.getInstance().removeOldTokens();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
