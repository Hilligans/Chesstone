package dev.hilligans;

import dev.hilligans.ai.SimpleMoveCalculator;
import dev.hilligans.board.*;
import dev.hilligans.board.pieces.Comparator;
import dev.hilligans.board.pieces.Redstone;
import dev.hilligans.board.pieces.RedstoneTorch;
import dev.hilligans.board.pieces.Repeater;
import dev.hilligans.spring.SpringHandler;
import dev.hilligans.util.ConsoleReader;
import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
       // new Thread(() -> SpringHandler.run(args)).start();
        SimpleMoveCalculator simpleMoveCalculator = new SimpleMoveCalculator(3);

       // new ConsoleReader(s -> {
      //      if(s.equals("stop")) {
       //         System.exit(0);
      //      }
      //  }).start();

        Board board = BoardBuilder.buildStandardBoard();
        //System.out.println(board.getPiece(0,1));
        board.applyMove(new Move(board.getPiece(0,1),0,3));
        board.applyMove(new Move(board.getPiece(0,6),0,4));
        board.applyMove(new Move(board.getPiece(0,0),0,2));
        board.applyMove(new Move(board.getPiece(0,7),0,5));
        board.applyMove(new Move(board.getPiece(0,2),7,2));
        board.applyMove(new Move(board.getPiece(3,6),3,5));
        board.applyMove(new Move(board.getPiece(7,2),7,5));
        board.applyMove(new Move(board.getPiece(2,7),5,4));
        board.applyMove(new Move(board.getPiece(4,1),3,2));
        board.applyMove(new Move(board.getPiece(1,7),2,5));
        board.applyMove(new Move(board.getPiece(3,0),5,2));
        board.applyMove(new Move(board.getPiece(7,5),5,5));
        board.applyMove(new Move(board.getPiece(6,0),7,2));
        board.applyMove(new Move(board.getPiece(7,2),5,3));
        board.applyMove(new Move(board.getPiece(5,2),3,4));
        board.applyMove(new Move(board.getPiece(3,4),1,4));
        board.applyMove(new Move(board.getPiece(1,4),1,5));
        ((Repeater)board.getPiece(2,0)).rotation = 1;
        ((Repeater)board.getPiece(2,0)).delay = 1;

        board.applyMove(new Move(board.getPiece(1,0),0,2));
        board.applyMove(new Move(board.getPiece(1,5),1,3));
        ((Comparator)board.getPiece(1,3)).rotation = 2;
        ((Comparator)board.getPiece(1,3)).subtract = true;
        //board.applyMove(new Move(board.getPiece(0,2),2,4));
        board.setPiece(2,3,new Redstone());
        board.setPiece(2,4,null);

        board.update();
        board.tick();
        board.tick();
       // board.tick();
       // board.tick();

        System.out.println("SHAPE:" + ((Redstone)board.getPiece(0,0)).shape);

        ArrayList<Move> moves = board.getAllValidMoves(1);

        JSONObject obj = new JSONObject();
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
                if(piece != null && piece.team == 1) {
                    OtherMove[] rots = piece.getRotationMoves();
                    OtherMove[] modes = piece.getModeMoves();
                    if(rots.length != 0) {
                        JSONArray r = new JSONArray();
                        rotations.put(getPos(x,y) + "",r);
                        for(int a = 0; a < rots.length; a++) {
                           r.put(rots[a].newID);
                        }
                    }
                    if(modes.length != 0) {
                        JSONArray r = new JSONArray();
                        modeChanges.put(getPos(x,y) + "",r);
                        for(int a = 0; a < modes.length; a++) {
                            r.put(modes[a].newID);
                        }
                    }
                }
            }
        }
        obj.put("mode_changes",modeChanges);
        obj.put("rotations",rotations);
        //System.out.println(obj);
   /*     short[] valsB = board.getBoardList();
        for(int x = 0; x < 64; x++) {
            if(!vals[x]) {
                valsB[x] = 0;
            }
        }

    */
        JSONArray boardArr = new JSONArray();
        boardArr.putAll(board.getBoardList());
        obj.put("board",boardArr);
        System.out.println(obj);
    }

    public static int getPos(int x, int y) {
        return y * 8 + x;
    }
}
