package dev.hilligans.storage;

import org.bson.BsonDocument;

public interface IGameResult {

    String type();
    IGameResult read(BsonDocument document);

    IGameResult write(BsonDocument document);

    long[] getUsers();

    default boolean containsArg(String arg, String[] args) {
        for(String s : args) {
            if(arg.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
