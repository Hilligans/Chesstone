package dev.hilligans.game;

import dev.hilligans.board.Board;
import dev.hilligans.board.BoardBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;

public class GameHandler {

    public HashMap<String, Game> games = new HashMap<>();

    public Game createGame(String type, String gameCode) {
        if(type.equals("normal")) {
            Board board = BoardBuilder.buildStandardBoard();
            Game game = new Game(board);
            game.gameCode = gameCode;
            return game;
        }
        return null;
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

    public synchronized int joinGame(String code, Player player) {
        Game game = games.get(code);
        if(game == null) {
            return -1;
        }
        if(game.started) {
            return 0;
        }
        game.player2 = new GamePlayer(player);
        game.player2.playerID = 2;
        return 1;
    }

}
