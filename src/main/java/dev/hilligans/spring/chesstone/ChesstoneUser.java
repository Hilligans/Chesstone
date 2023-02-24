package dev.hilligans.spring.chesstone;

import dev.hilligans.storage.Database;
import dev.hilligans.storage.User;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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



        //current game

        return response.toString();
    }
}
