package dev.hilligans.spring.chesstone;

import dev.hilligans.storage.Database;
import dev.hilligans.storage.Token;
import dev.hilligans.storage.User;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ChesstoneAccount {


    @GetMapping("/chesstone/account")
    public String account(HttpServletRequest request, HttpServletResponse response) {
        String tokenString = Database.getTokenString(request);
        JSONObject returnValue = new JSONObject();
        if(tokenString != null) {
            Token token = Database.getAccountToken(tokenString);
            if (token != null) {
                User user = Database.getInstance().getUser(token.owner);

                returnValue.put("logged_in", !user.banned);
                if (user.banned) {
                    returnValue.put("reason", "Banned");
                }
                returnValue.put("name", user.getName());
                returnValue.put("id", user.id);
            } else {
                returnValue.put("logged_in", false);
                returnValue.put("reason", "Session Expired");
                response.addCookie(ChesstoneLogout.getLogoutCookie(tokenString));
            }
        } else {
            returnValue.put("logged_in", false);
            returnValue.put("reason", "Session Expired");
        }

        return returnValue.toString();
    }
}
