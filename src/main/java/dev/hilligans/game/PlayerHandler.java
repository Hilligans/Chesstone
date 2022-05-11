package dev.hilligans.game;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerHandler {

    public final PlayerHandler handler = this;
    public HashMap<String,Player> players = new HashMap<>();

    public HashMap<Player, ArrayList<Game>> activeGames = new HashMap<>();

    //slight chance something could break but unlikely
    public synchronized void disconnect(String identifier) {
        Player player = getPlayer(identifier);
        if(player != null) {
            ArrayList<Game> games = activeGames.get(player);
            player.online = false;
            if(games != null && games.size() != 0) {
                player.callback = () -> {
                    synchronized (handler) {
                        ArrayList<Game> games1 = activeGames.get(player);
                        if (games1 == null || games1.size() == 0) {
                            player.alive = false;
                            handleRemove(identifier);
                        }
                    }
                };
            } else {
                handleRemove(identifier);
            }
        }
    }

    public synchronized void handleRemove(String identifier) {
        activeGames.remove(players.remove(identifier));
    }

    public synchronized Player connect(String identifier) {
        Player player = getPlayer(identifier);
        if(player == null) {
            player = new Player(identifier);
            players.put(identifier,player);
        }
        return player;
    }

    public synchronized void updateData(String identifier, String name) {
        Player player = getPlayer(identifier);
        if(player != null) {
            player.name = name;
        }
    }

    @Nullable
    public synchronized Player getPlayer(String identifier) {
        return players.get(identifier);
    }
}
