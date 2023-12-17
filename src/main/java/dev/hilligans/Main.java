package dev.hilligans;

import dev.hilligans.chesstone.ai.SimpleMoveCalculator;
import dev.hilligans.chesstone.board.IMove;
import dev.hilligans.chesstone.board.Move;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.chesstone.game.AccountHandler;
import dev.hilligans.chesstone.game.GameHandler;
import dev.hilligans.chesstone.game.IGame;
import dev.hilligans.chesstone.storage.Database;
import dev.hilligans.chesstone.storage.GameResult;
import dev.hilligans.chesstone.util.ArgumentContainer;
import dev.hilligans.chesstone.util.ConsoleParser;
import dev.hilligans.chesstone.util.ConsoleReader;
import dev.hilligans.spring.SpringHandler;
import org.json.JSONObject;

import java.util.function.Consumer;

public class Main {

    public static final GameHandler gameHandler = new GameHandler();
    public static final AccountHandler accountHandler = new AccountHandler();


    public static String path = "/chesstone/play/";
    public static final int START_USER_RATING = 0;
    public static ArgumentContainer argumentContainer;
    public static boolean allowUnauthorizedPlayersToPlayGames = false;

    public static final int tokenExpiryTime = 604800;

    public static final String[] gameModes = new String[] {"default"};


    public static void main(String[] args) {
        argumentContainer = new ArgumentContainer(args);
        Move move = new Move(7, 6, 5, 4, 2);
        IMove m = GameResult.intToMove(GameResult.moveToInt(move));
        System.out.println(move);
        System.out.println(m);

        new Thread(() -> SpringHandler.run(args)).start();
        SimpleMoveCalculator simpleMoveCalculator = new SimpleMoveCalculator(4);

        ConsoleParser consoleParser = new ConsoleParser();
        consoleParser.addCommand("stop", "Stops the server", 0, s -> System.exit(0));
        consoleParser.addCommand("data", "Gets the data of a piece of a certain game", 3, args1 -> {
            try {
                int x = Integer.parseInt(args1[1]);
                int y = Integer.parseInt(args1[2]);
                IGame game = gameHandler.getGame(args1[0]);
                Piece piece = game.getBoard().getPiece(x, y);
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
                        IGame game = gameHandler.getGame(vals[0]);
                        Piece piece = game.getBoard().getPiece(x, y);
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
                        IGame game = gameHandler.getGame(vals[0]);
                        Piece piece = game.getBoard().getPiece(x, y);
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
                        IGame game = gameHandler.getGame(vals[0]);
                        Piece piece = game.getBoard().getPiece(x, y);
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
                    IGame game = gameHandler.getGame(code);
                    if (game != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", "server_message");
                        jsonObject.put("data", vals[2]);
                        game.sendPacketToAll(jsonObject.toString());
                    }
                } else if(s.startsWith("purge-tokens")) {
                    Database.getInstance().removeOldTokens();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //MoveBook.setupOpenings();
        //DataBaseMoveContainer moveContainer = new DataBaseMoveContainer();
        //System.out.println("Openings: " + Database.getInstance().getOpeningWithMoves(moveContainer));
    }
}
