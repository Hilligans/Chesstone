package dev.hilligans.chesstone.storage;

import java.util.HashMap;

public class SessionHandler {

    public HashMap<String, User> sessionMapping = new HashMap<>();




    public static SessionHandler instance = new SessionHandler();

    public SessionHandler getInstance() {
        return instance;
    }
}
