package dev.hilligans.storage;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hilligans.Main;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.conversions.Bson;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.function.Supplier;

public class Database {

    private static final Database DATABASE = new Database();

    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<BsonDocument> games;
    MongoCollection<BsonDocument> users;
    MongoCollection<BsonDocument> tokens;

    Long2ObjectOpenHashMap<User> userCache = new Long2ObjectOpenHashMap<>();


    public Database() {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase("chesstone");
        System.out.println("Database " + database);
        games = database.getCollection("games", BsonDocument.class);
        users = database.getCollection("users", BsonDocument.class);
        tokens = database.getCollection("tokens", BsonDocument.class);
    }

    public synchronized User getUser(long userID) {
        User user = userCache.getOrDefault(userID, null);
        if(user == null) {
            user = new User(users.find(Filters.eq("userID", userID)).first());
            userCache.put(userID, user);
        }
        return user;
    }

    public synchronized void putUser(User user) {
        userCache.put(user.id, user);
        users.replaceOne(Filters.eq("userID", user.id), user.write(), new UpdateOptions().upsert(true));
    }

    public synchronized void updateUserAuth(User user) {
        User newUser = getUser(user.id);
        if(newUser != null) {
            newUser.identifier = user.identifier;
            newUser.avatarPath = user.avatarPath;
            newUser.username = user.username;
        } else {
            newUser = user;
        }
        putUser(newUser);
    }

    public synchronized IGameResult getGame(long gameID) {
        FindIterable<BsonDocument> gameList = games.find(Filters.eq("gameID", gameID));
        BsonDocument document = gameList.first();
        return GAME_TYPE_PROVIDERS.get(STRINGS.get(document.getInt32("type").intValue())).get().read(document);
    }

    public synchronized void putGame(long gameID, IGameResult gameResult) {
        BsonDocument bsonDocument = new BsonDocument();
        gameResult.write(bsonDocument);
        bsonDocument.put("gameID", new BsonInt64(gameID));
        bsonDocument.put("type", new BsonInt32(IDS.get(gameResult.type())));
        games.insertOne(bsonDocument);
    }

    public synchronized LongArrayList getGamesForUser(long userID) {
        Bson filter = Filters.or(Filters.eq("u1", userID), Filters.eq("u2", userID));
        LongArrayList list = new LongArrayList();
        FindIterable<BsonDocument> gameList = games.find(filter);
        for(BsonDocument document : gameList) {
            list.add(document.getInt64("gameID").longValue());
        }
        return list;
    }

    public synchronized void putToken(Token token) {
        tokens.insertOne(token.write());
    }

    public synchronized Token getToken(String token) {
        Bson filter = Filters.eq("token", token);
        FindIterable<BsonDocument> gameList = tokens.find(filter);
        for(BsonDocument document : gameList) {
            Token token1 = new Token(document);
            if(token1.expiryDateSeconds > System.currentTimeMillis() / 1000) {
                return token1;
            }
        }
        tokens.deleteMany(filter);
        return null;
    }

    public synchronized void removeToken(String token) {
        tokens.deleteMany(Filters.eq("token", token));
    }

    public synchronized void removeOldTokens() {
        tokens.deleteMany(Filters.lt("expiry", System.currentTimeMillis()/1000));
    }

    public synchronized void updateTokenTimestamp(Token token) {
        tokens.replaceOne(Filters.eq("token", token.token), token.updateTimestamp(Main.tokenExpiryTime).write());
    }

    public static Token getAccountToken(String tokenString) {
        Token token = getInstance().getToken(tokenString);
        if (token != null) {
            getInstance().updateTokenTimestamp(token);
        }
        return token;
    }

    public static String getTokenString(HttpServletRequest request) {
        for(Cookie cookie : request.getCookies()) {
            if("chesstone_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Database getInstance() {
        return DATABASE;
    }

    public static final HashMap<String, Supplier<IGameResult>> GAME_TYPE_PROVIDERS = new HashMap<>();
    public static final HashMap<String, Integer> IDS = new HashMap<>();
    public static final HashMap<Integer, String> STRINGS = new HashMap<>();

    static {
        GAME_TYPE_PROVIDERS.put("default", GameResult::new);

        IDS.put("default", 1);
        STRINGS.put(1, "default");
    }
}
