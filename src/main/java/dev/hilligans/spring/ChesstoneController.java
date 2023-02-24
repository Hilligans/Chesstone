package dev.hilligans.spring;

import dev.hilligans.Main;
import dev.hilligans.game.GameHandler;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ChesstoneController {

    @CrossOrigin
    @PostMapping("/chesstone/create_game")
    public String create_game(HttpServletRequest request, @RequestBody String payload) {
        JSONObject content = new JSONObject(payload);

        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject options = content.getJSONObject("options");
            JSONObject clock = options.getJSONObject("clock");
            String mode = options.optString("mode", "default");
            jsonObject.put("game_id", Main.gameHandler.newGame(mode, content.optString("name", ""), content.optBoolean("public")));
            jsonObject.put("success", true);
        } catch (Exception e) {
            jsonObject.put("success", false);
            jsonObject.put("reason", e.getMessage());
        }
        return jsonObject.toString();
    }
}
