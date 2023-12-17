package dev.hilligans.chesstone.storage;

import dev.hilligans.chesstone.board.IMove;
import org.bson.BsonInt32;

import java.util.ArrayList;

public class DataBaseMoveContainer {

    public ArrayList<BsonInt32> moves = new ArrayList<>();

    public DataBaseMoveContainer addMove(IMove move) {
        int encoded = GameResult.moveToInt(move);
        moves.add(new BsonInt32(encoded));
        return this;
    }
}
