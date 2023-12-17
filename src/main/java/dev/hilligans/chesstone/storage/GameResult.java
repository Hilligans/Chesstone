package dev.hilligans.chesstone.storage;

import dev.hilligans.chesstone.board.IMove;
import dev.hilligans.chesstone.board.Move;
import dev.hilligans.chesstone.board.StateChangeMove;
import org.bson.*;

import java.util.ArrayList;

public class GameResult implements IGameResult {


    public long user1;
    public long user2;
    public int winner;
    public ArrayList<IMove> moves;


    public GameResult() {
    }


    @Override
    public String type() {
        return "normal";
    }

    @Override
    public IGameResult read(BsonDocument document) {
        user1 = document.getInt64("u1").getValue();
        user2 = document.getInt64("u2").getValue();
        winner = document.getInt32("win").intValue();
        BsonArray array = document.getArray("mov");
        moves = new ArrayList<>();
        for(int x = 0; x < array.size(); x++) {
            moves.add(intToMove(array.get(x).asInt32().getValue()));
        }
        return this;
    }

    @Override
    public IGameResult write(BsonDocument document) {
        document.put("u1", new BsonInt64(user1));
        document.put("u2", new BsonInt64(user2));
        document.put("win", new BsonInt32(winner));
        BsonArray array = new BsonArray();
        for(IMove move : moves) {
            array.add(new BsonInt32(moveToInt(move)));
        }
        document.put("mov", array);
        return this;
    }

    public static int moveToInt(IMove m) {
        if(m instanceof Move move) {
            return (move.startY * 8 + move.startX) << 8 | (move.endY * 8 + move.endX) << 14 | ((m.getTeam() - 1) << 31);
        } else if(m instanceof StateChangeMove move) {
            return move.getType() | (move.y * 8 + move.x) << 8 | ((int) ((short) move.newID)) << 16 | ((m.getTeam() - 1) << 31);
        }
        return -1;
    }

    public static IMove intToMove(int val) {
        int type = val & 0b11111111;
        int team = ((val & (1 << 31)) >>> 31);
        val = val & (~(team << 31));
        team++;
        val = val >>> 8;
        if(type == 0) {
            int comp = val & 0b111111;
            return new Move(comp & 0b111, comp >> 3, (val >> 6) & 0b111, val >> 9, team);
        } else if(type == 1 | type == 2) {
            int comp = val & 0b111111;
            return new StateChangeMove(comp & 0b111, comp >> 3, val >> 8, type, team);
        }
        return null;
    }


    @Override
    public long[] getUsers() {
        return new long[]{user1, user2};
    }
}
