package dev.hilligans.spring.chesstone;

import dev.hilligans.Main;
import dev.hilligans.chesstone.game.IGame;
import dev.hilligans.chesstone.game.IPlayer;
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

        ArrayList<IGame> games = Main.gameHandler.getPublicGames();
        int x = 0;
        for(IGame game : games) {
            JSONObject jsonObject = new JSONObject();
            jsonArray.put(x++, jsonObject);
            jsonObject.put("id", game.getGameCode());
            jsonObject.put("game_name", game.getGameName());
            jsonObject.put("in_progress", game.isRunning());

            IPlayer[] players = game.getPlayers();
            JSONArray names = new JSONArray(players.length);
            for(int i = 0; i < players.length; i++) {
                if(players[i] != null) {
                    names.put(i, players[i].getName());
                } else {
                    names.put(i, "");
                }
            }
            jsonObject.put("names", names);
        }

        return jsonArray.toString();
    }
}