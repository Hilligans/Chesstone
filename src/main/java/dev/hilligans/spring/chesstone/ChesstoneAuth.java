package dev.hilligans.spring.chesstone;

import dev.hilligans.Main;
import dev.hilligans.spring.oauth.OAuthData;
import dev.hilligans.storage.Database;
import dev.hilligans.storage.Token;
import dev.hilligans.storage.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Collections;


@RestController
@RequestMapping("/chesstone/auth")
public class ChesstoneAuth {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping()
    public RedirectView account(HttpServletRequest req, HttpServletResponse response, @RequestParam("code") String code) {
        System.out.println(code);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", Main.argumentContainer.getString("client_id", ""));
        map.add("client_secret", Main.argumentContainer.getString("client_secret", ""));
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", "https://hilligans.dev:443/chesstone/auth");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String uri = "https://discord.com:443/api/v10/oauth2/token";

        ResponseEntity<String> c = restTemplate.postForEntity(uri, request, String.class);

        JSONObject jsonObject = new JSONObject(c.getBody());

        uri = "https://discordapp.com:443/api/users/@me";

        restTemplate = new RestTemplate();

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "" + jsonObject.getString("token_type") + " " + jsonObject.getString("access_token"));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        JSONObject data = new JSONObject(result.getBody());

        User user = new User(Long.parseLong(data.getString("id")), data.getString("username"), data.getString("discriminator"), data.getString("avatar"));
        Database.getInstance().updateUserAuth(user);

        SecureRandom random = new SecureRandom();
        String secret = Long.toString((System.nanoTime()), 36) + RandomStringUtils.random(16,0,0,true,true, null, random);
        Cookie cookie = new Cookie("chesstone_token", secret);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(604800);

        response.addCookie(cookie);

        Token token = new Token(secret, Main.tokenExpiryTime, "", Long.parseLong(data.getString("id")));
        Database.getInstance().putToken(token);

        String redirect = OAuthData.getInstance().getRedirectURL(req.getSession().getId());

        return new RedirectView(redirect);
    }
}
