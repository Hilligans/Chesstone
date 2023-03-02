package dev.hilligans.spring.chesstone;

import dev.hilligans.storage.Database;
import dev.hilligans.storage.Token;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ChesstoneLogout {

    public static final String auth_url = "https://discord.com/api/oauth2/authorize?client_id=1076953398684958830&redirect_uri=https%3A%2F%2Fhilligans.dev%3A443%2Fchesstone%2Fauth&response_type=code&scope=identify";

    @PostMapping("chesstone/logout")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String token = Database.getTokenString(request);
        if (token != null) {
            Token token1 = Database.getInstance().getToken(token);
            Database.getInstance().removeToken(token);
            response.addCookie(getLogoutCookie(token));
            if(token1 != null) {
                Database.getInstance().invalidateTokens(token1.owner);
            }
        }
    }

    public static Cookie getLogoutCookie(String token) {
        Cookie cookie = new Cookie("chesstone_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        return cookie;
    }
}