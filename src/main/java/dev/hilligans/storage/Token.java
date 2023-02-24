package dev.hilligans.storage;

import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;

public class Token {

    public String token;
    public long expiryDateSeconds;
    public String identifier;
    public long owner;

    public Token(String token, long secondsTillExpiry, String identifier, long owner) {
        this.token = token;
        this.expiryDateSeconds = System.currentTimeMillis() / 1000 + secondsTillExpiry;
        this.identifier = identifier;
        this.owner = owner;
    }

    public Token(BsonDocument document) {
        this.token = document.getString("token").getValue();
        this.expiryDateSeconds = document.getInt64("expiry").longValue();
        this.identifier = document.getString("identifier").getValue();
        this.owner = document.getInt64("owner").longValue();
    }

    public BsonDocument write() {
        BsonDocument document = new BsonDocument();
        document.put("token", new BsonString(token));
        document.put("expiry", new BsonInt64(expiryDateSeconds));
        document.put("identifier", new BsonString("identifier"));
        document.put("owner", new BsonInt64(owner));
        return document;
    }

    public Token updateTimestamp(int secondsTillExpiry) {
        this.expiryDateSeconds = System.currentTimeMillis() / 1000 + secondsTillExpiry;
        return this;
    }
}
