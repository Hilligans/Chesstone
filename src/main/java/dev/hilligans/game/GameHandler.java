package dev.hilligans.game;

import dev.hilligans.board.Board;
import dev.hilligans.board.BoardBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

public class GameHandler {

    public HashMap<String, Game> games = new HashMap<>();
    public HashMap<WebSocketSession, GamePlayer> players = new HashMap<>();

    public HashMap<Game, HashMap<Player, GamePlayer>> gamePlayers = new HashMap<>();

    public Game getGame(String code) {
        return games.get(code);
    }

    public Game createGame(String type, String gameCode) {
        if(type.equals("normal")) {
            Board board = BoardBuilder.buildStandardBoard();
            Game game = new Game(board);
            game.gameCode = gameCode;
            return game;
        }
        return null;
    }

    public String newGame(String type) {
        String gameCode = RandomStringUtils.randomAlphanumeric(8);
        if(games.get(gameCode) != null) {
            return newGame(type);
        } else {
            System.out.println("New Game Created: " + gameCode);
            Game game = createGame(type,gameCode);
            games.put(gameCode,game);
        }
        return gameCode;
    }

    public String startGame(String type, Player player) {
        String gameCode = RandomStringUtils.random(8);
        if(games.get(gameCode) != null) {
            return startGame(type, player);
        } else {
            Game game = createGame(type,gameCode);
            game.player1 = new GamePlayer(player);
            game.player1.playerID = 1;
            games.put(gameCode,game);
        }
        return gameCode;
    }

    public synchronized int joinGame(String code, Player player, WebSocketSession session) {
        Game game = games.get(code);

        if(game == null) {
            return -1;
        }
        GamePlayer pl = getActivePlayer(game,player);
        if(pl != null) {
            pl.addSession(session,this);
            return pl.playerID;
        }

        pl = new GamePlayer(player);
        HashMap<Player, GamePlayer> pla = gamePlayers.computeIfAbsent(game,(a) -> new HashMap<>());
        pla.put(player,pl);

        if(game.started) {
            if(!game.addSpectator(pl)) {
                return -1;
            }
            pl.addSession(session,this);
            pl.playerID = 0;
            return 0;
        }
        pl.addSession(session,this);
        if(game.player1 == null) {
            game.player1 = pl;
            pl.playerID = 1;
            return  1;
        }
        pl.addSession(session,this);
        game.player2 = pl;
        pl.playerID = 2;
        return 2;
    }

    public synchronized GamePlayer getPlayer(WebSocketSession session) {
        return players.get(session);
    }

    public GamePlayer getActivePlayer(Game game, Player player) {
        HashMap<Player, GamePlayer> map = gamePlayers.get(game);
        if(map != null) {
            return map.getOrDefault(player,null);
        }
        return null;
    }

}
