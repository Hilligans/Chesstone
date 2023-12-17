package dev.hilligans.chesstone.game;

import dev.hilligans.chesstone.board.Board;
import dev.hilligans.chesstone.board.BoardBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameHandler {

    public ConcurrentHashMap<String, IGame> games = new ConcurrentHashMap<>();
    //public HashMap<WebSocketSession, GamePlayer> players = new HashMap<>();

    //public HashMap<IGame, HashMap<Player, GamePlayer>> gamePlayers = new HashMap<>();
    public Long2ObjectOpenHashMap<ArrayList<IGame>> id_to_games = new Long2ObjectOpenHashMap<>();

    public boolean logUpdates = false;
    public boolean logPackets = false;
    public String snooper = null;

    public IGame getGame(String code) {
        return games.get(code);
    }

    public IGame createGame(String type, String gameCode) {
        if(type.equals("default")) {
            Board board = BoardBuilder.buildStandardBoard();
            return new NewGame(board, gameCode);
        }
        return null;
    }

    public String newGame(String type, String name, boolean isPublic) {
        String gameCode = RandomStringUtils.randomAlphanumeric(8);
        if(games.get(gameCode) != null) {
            return newGame(type, name, isPublic);
        } else {
            System.out.println("New Game Created: " + gameCode);
            IGame game = createGame(type,gameCode);
            game.setName(name);
            game.setPublic(isPublic);
            games.put(gameCode,game);
        }
        return gameCode;
    }

    /*
    public synchronized int joinGame(String code, Player player, WebSocketSession session) {
        IGame game = games.get(code);

        if (game == null) {
            return -1;
        }

        GamePlayer pl = getActivePlayer(game, player);
        if (pl != null) {
            pl.addSession(session, this);
            return pl.playerID;
        }

        long userID = Database.getUserID(session);
        pl = new GamePlayer(player, userID);

        int val = game.addPlayer(pl);
        if (val != -1) {
            pl.addSession(session, this);
            HashMap<Player, GamePlayer> pla = gamePlayers.computeIfAbsent(game, (a) -> new HashMap<>());
            pla.put(player, pl);
            if(val != 0) {
                id_to_games.computeIfAbsent(userID, (a) -> new ArrayList<>()).add(game);
            }
        }


        //return val;
        return 0;
    }

     */

    public synchronized IViewer joinGame(String code, Account account, WebSocketSession session) {
        IGame game = games.get(code);
        if(game == null) {
            return null;
        }

        return game.addPlayer(account, session);
    }



    /*
    public synchronized void endGame(IGame game) {
        games.remove(game.getGameCode());
       // gamePlayers.remove(game);

        for(GamePlayer gamePlayer : game.getGamePlayers()) {
            if(gamePlayer != null) {
                ArrayList<IGame> gamesList = id_to_games.getOrDefault(gamePlayer.identifier, null);
                if(gamesList != null) {
                    gamesList.remove(game);
                }
            }
        }
    }

     */

    public ArrayList<IGame> getPublicGames() {
        ArrayList<IGame> games = new ArrayList<>();
        this.games.forEach((s, game) -> {
            if(game.isPublic()) {
                games.add(game);
            }
        });
        return games;
    }

    /*
    public synchronized GamePlayer getPlayer(WebSocketSession session) {
        return players.get(session);
    }

     */

    /*
    public GamePlayer getActivePlayer(Game game, Player player) {
        HashMap<Player, GamePlayer> map = gamePlayers.get(game);
        if(map != null) {
            return map.getOrDefault(player,null);
        }
        return null;
    }
     */
}
