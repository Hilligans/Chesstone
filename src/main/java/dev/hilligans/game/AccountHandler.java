package dev.hilligans.game;

import dev.hilligans.storage.Database;
import dev.hilligans.storage.User;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

public class AccountHandler {

    public final Long2ObjectOpenHashMap<Account> accounts = new Long2ObjectOpenHashMap<>();
    public final HashMap<WebSocketSession, Account> socketMapping = new HashMap<>();

    public Account getAccount(long id) {
        synchronized (accounts) {
            Account account = accounts.getOrDefault(id, null);
            if (account == null) {
                User user = Database.getInstance().getUser(id);
                if (user == null) {
                    return null;
                }
                account = new Account(user);
                accounts.put(id, account);
            }
            return account;
        }
    }

    public Account getAccount(String token) {
        long id = Database.getInstance().getToken(token).owner;
        return getAccount(id);
    }

    public Account getAccount(WebSocketSession session) {
        synchronized (socketMapping) {
            return socketMapping.get(session);
        }
    }

    public Account createAnonymousAccount(WebSocketSession session, String username) {
        Account account = new Account(username);
        synchronized (socketMapping) {
            socketMapping.put(session, account);
        }
        return account;
    }

    public void openSocket(WebSocketSession webSocketSession, String token) {
        Account account = getAccount(token);
        synchronized (socketMapping) {
            socketMapping.put(webSocketSession, account);
            account.addSession(webSocketSession);
        }
    }

    public Account closeSocket(WebSocketSession session) {
        Account account = socketMapping.remove(session);
        if(account != null) {
            account.removeSession(session);
        }
        return account;
    }
}
