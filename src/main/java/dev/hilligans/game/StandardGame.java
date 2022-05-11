package dev.hilligans.game;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.OtherMove;
import dev.hilligans.board.Piece;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;

import java.util.ArrayList;

public class StandardGame implements GameImplementation{
    @Override
    public void sendDataToPlayer(short[] board, Game game, GamePlayer player) {
        JSONObject jsonObject = new JSONObject();
        JSONArray data = new JSONArray();
        data.putAll(board);
        jsonObject.put("type", "board");
        jsonObject.put("data",data);
        jsonObject.put("turn", game.turn);
        try {
            player.sendPacket(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMoveListToPlayer(Game game, GamePlayer player) {
        Board board = game.board;

        ArrayList<Move> moves = board.getAllValidMoves(player.playerID);

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
                if(piece != null && piece.team == player.playerID) {
                    OtherMove[] rots = piece.getRotationMoves();
                    OtherMove[] modes = piece.getModeMoves();
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
        try {
            player.sendPacket(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getPos(int x, int y) {
        return y * 8 + x;
    }
}
