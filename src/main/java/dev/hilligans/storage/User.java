package dev.hilligans.storage;

import dev.hilligans.Main;
import org.bson.*;

public class User {

    public long id;
    public String username;
    public String identifier;
    public String avatarPath;
    public boolean banned = false;
    public int rating = Main.START_USER_RATING;


    public User(long id, String username, String identifier, String avatarPath) {
        this.id = id;
        this.username = username;
        this.identifier = identifier;
        this.avatarPath = avatarPath;
    }

    public User setBanned(boolean banned) {
        this.banned = banned;
        return this;
    }

    public User setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public String getAvatarPath() {
        return "https://cdn.discordapp.com/avatars/" + id + "/" + avatarPath + ".png";
    }

    public String getName() {
        return username;
    }

    public User(BsonDocument document) {
        this.id = document.getInt64("userID").longValue();
        this.username = document.getString("name").toString();
        this.identifier = document.getString("identifier").toString();
        this.avatarPath = document.getString("avatar").toString();
        this.banned = document.getBoolean("banned", new BsonBoolean(false)).getValue();
        this.rating = document.getInt32("rating", new BsonInt32(Main.START_USER_RATING)).intValue();
    }

    public BsonDocument write() {
        BsonDocument document = new BsonDocument();
        document.put("userID", new BsonInt64(id));
        document.put("name", new BsonString(username));
        document.put("identifier", new BsonString(identifier));
        document.put("avatar", new BsonString(avatarPath));
        document.put("banned", new BsonBoolean(banned));
        document.put("rating", new BsonInt32(rating));
        return document;
    }
}
