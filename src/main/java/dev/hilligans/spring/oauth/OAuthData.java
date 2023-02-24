package dev.hilligans.spring.oauth;

import java.util.HashMap;

public class OAuthData {

    public HashMap<String, String> redirectURLS = new HashMap<>();

    public synchronized String getRedirectURL(String sessionID) {
        return redirectURLS.getOrDefault(sessionID, "/chesstone");
    }

    public synchronized void putRedirectURL(String sessionID, String url) {
        redirectURLS.put(sessionID, url);
    }

    public static OAuthData instance = new OAuthData();

    public static OAuthData getInstance() {
        return instance;
    }
}
