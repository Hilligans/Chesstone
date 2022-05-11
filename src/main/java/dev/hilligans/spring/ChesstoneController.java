package dev.hilligans.spring;

import dev.hilligans.Main;
import dev.hilligans.game.GameHandler;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ChesstoneController {

    @CrossOrigin
    @GetMapping("/chesstone/create_game")
    public String create_game(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", Main.gameHandler.newGame("normal"));
            jsonObject.put("success", true);
        } catch (Exception e) {
            jsonObject.put("success", false);
        }

        return jsonObject.toString();
    }
}
