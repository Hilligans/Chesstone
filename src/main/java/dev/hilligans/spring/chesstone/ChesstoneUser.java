package dev.hilligans.spring.chesstone;

import dev.hilligans.Main;
import dev.hilligans.game.IGame;
import dev.hilligans.storage.Database;
import dev.hilligans.storage.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class ChesstoneUser {

    @GetMapping("/chesstone/user")
    public String user(HttpServletRequest request, @RequestBody String payload) {
        JSONObject body = new JSONObject(payload);
        JSONObject response = new JSONObject();

        long id = Long.parseLong(body.getString("id"));
        User user = Database.getInstance().getUser(id);

        response.put("name", user.username);
        response.put("avatar", user.getAvatarPath());
        synchronized (Main.gameHandler) {
            ArrayList<IGame> games = Main.gameHandler.id_to_games.getOrDefault(id, null);
            if(games != null && !games.isEmpty()) {
                JSONArray gameList = new JSONArray(games.size());
                for(int x = 0; x < games.size(); x++) {
                    gameList.put(x, games.get(x).getGameCode());
                }
                response.put("current_games", games);
            }
        }
        return response.toString();
    }
}
