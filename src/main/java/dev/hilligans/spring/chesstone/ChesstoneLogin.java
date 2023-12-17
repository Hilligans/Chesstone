package dev.hilligans.spring.chesstone;


import dev.hilligans.spring.oauth.OAuthData;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ChesstoneLogin {

    public static final String auth_url = "https://discord.com/api/oauth2/authorize?client_id=1076953398684958830&redirect_uri=https%3A%2F%2Fhilligans.dev%3A443%2Fchesstone%2Fauth&response_type=code&scope=identify";

    @GetMapping("chesstone/login")
    public String login(HttpServletRequest request, @RequestBody String payload) {
        JSONObject data = new JSONObject(payload);
        JSONObject response = new JSONObject();

        String return_url = data.optString("return_url", "hilligans.dev/chesstone");
        //String origin = request.getHeader("Origin");

        response.put("url", auth_url);
        OAuthData.getInstance().putRedirectURL(request.getSession().getId(), return_url);

        return response.toString();
    }
}
