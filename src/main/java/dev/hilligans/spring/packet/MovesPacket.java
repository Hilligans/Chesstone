package dev.hilligans.spring.packet;

import dev.hilligans.board.IBoard;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.StateChangeMove;
import dev.hilligans.game.IGame;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovesPacket implements IPacket {

    public IGame game;
    public int playerID;

    public MovesPacket(IGame game, int playerID) {
        this.game = game;
        this.playerID = playerID;
    }


    @Override
    public String toEncodedPacket() {
        IBoard board = game.getBoard();

        ArrayList<Move> moves = board.getAllValidMoves(playerID);

        JSONObject jsonObject = new JSONObject();

        JSONObject obj = new JSONObject();

        jsonObject.put("type", "moves");
        jsonObject.put("data", obj);
        JSONObject movesObj = new JSONObject();
        obj.put("moves", movesObj);
        boolean[] vals = new boolean[64];

        JSONArray moveList = null;
        int pos = -1;

        for(Move move : moves) {
            if(pos != getPos(move.startX,move.startY)) {
                if(moveList != null) {
                    movesObj.put(pos + "",moveList);
                }
                pos = getPos(move.startX,move.startY);
                vals[pos] = true;
                moveList = new JSONArray();
            }
            vals[getPos(move.endX,move.endY)] = true;
            moveList.put(getPos(move.endX,move.endY));
        }
        if(moveList != null) {
            movesObj.put(pos + "",moveList);
        }

        JSONObject modeChanges = new JSONObject();
        JSONObject rotations = new JSONObject();
        for(int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getPiece(x,y);
                if(piece != null && piece.team == playerID) {
                    StateChangeMove[] rots = piece.getRotationMoves();
                    StateChangeMove[] modes = piece.getModeMoves();
                    if(rots.length != 0) {
                        JSONArray r = new JSONArray();
                        rotations.put(getPos(x,y) + "",r);
                        for(int a = 0; a < rots.length; a++) {
                            if(rots[a] != null) {
                                r.put(rots[a].newID);
                            }
                        }
                    }
                    if(modes.length != 0) {
                        JSONArray r = new JSONArray();
                        modeChanges.put(getPos(x,y) + "",r);
                        for(int a = 0; a < modes.length; a++) {
                            if(modes[a] != null) {
                                r.put(modes[a].newID);
                            }
                        }
                    }
                }
            }
        }
        obj.put("mode_changes",modeChanges);
        obj.put("rotations",rotations);

        return obj.toString();
    }

    public static int getPos(int x, int y) {
        return y * 8 + x;
    }
}
