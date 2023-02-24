package dev.hilligans.spring.chesstone;

import dev.hilligans.Main;
import dev.hilligans.game.Game;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class ChesstoneGameList {


    @CrossOrigin
    @GetMapping("/chesstone/list_games")
    public String list_games(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();

        ArrayList<Game> games = Main.gameHandler.getPublicGames();
        int x = 0;
        for(Game game : games) {
            JSONObject jsonObject = new JSONObject();
            jsonArray.put(x++, jsonObject);
            jsonObject.put("id", game.gameCode);
            jsonObject.put("game_name", game.gameName);
            jsonObject.put("in_progress", game.gameRunning);

            JSONArray names = new JSONArray();
            if(game.player1 != null) {
                names.put(0, game.player1.player.name);
            } else {
                names.put(0, "");
            }
            if(game.player2 != null) {
                names.put(1, game.player2.player.name);
            } else {
                names.put(1, "");
            }
            jsonObject.put("names", names);
        }

        return jsonArray.toString();
    }
}