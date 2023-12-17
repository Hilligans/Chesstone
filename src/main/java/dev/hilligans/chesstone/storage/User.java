package dev.hilligans.chesstone.storage;

import dev.hilligans.Main;
import org.bson.*;

import java.util.HashMap;

public class User {

    public long id;
    public String username;
    public String identifier;
    public String avatarPath;
    public boolean banned = false;

    public HashMap<String, Integer> ratings = new HashMap<>();


    public User(long id, String username, String identifier, String avatarPath) {
        this.id = id;
        this.username = username;
        this.identifier = identifier;
        this.avatarPath = avatarPath;
        withDefaultRatings();
    }

    public User withDefaultRatings() {
        for(String gameType : Main.gameModes) {
            ratings.put(gameType, Main.START_USER_RATING);
        }
        return this;
    }

    public User setBanned(boolean banned) {
        this.banned = banned;
        return this;
    }

    public User setRating(String gameMode, int rating) {
        ratings.put(gameMode, rating);
        return this;
    }

    public String getAvatarPath() {
        return "https://cdn.discordapp.com/avatars/" + id + "/" + avatarPath + ".png";
    }

    public String getName() {
        return username;
    }

    public User(BsonDocument document) {
        this.id = document.getInt64("userID").getValue();
        this.username = document.getString("name").getValue();
        this.identifier = document.getString("identifier").getValue();
        this.avatarPath = document.getString("avatar").getValue();
        this.banned = document.getBoolean("banned", new BsonBoolean(false)).getValue();

        BsonDocument ratings = document.getDocument("ratings", new BsonDocument());
        for(String gameType : Main.gameModes) {
            this.ratings.put(gameType, ratings.getInt32(gameType, new BsonInt32(Main.START_USER_RATING)).getValue());
        }
    }

    public BsonDocument write() {
        BsonDocument document = new BsonDocument();
        document.put("userID", new BsonInt64(id));
        document.put("name", new BsonString(username));
        document.put("identifier", new BsonString(identifier));
        document.put("avatar", new BsonString(avatarPath));
        document.put("banned", new BsonBoolean(banned));

        BsonDocument ratings = new BsonDocument();
        for(String key : this.ratings.keySet()) {
            ratings.put(key, new BsonInt32(this.ratings.get(key)));
        }
        document.put("ratings", ratings);

        return document;
    }
}
