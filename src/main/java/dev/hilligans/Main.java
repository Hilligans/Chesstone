package dev.hilligans;

import dev.hilligans.ai.SimpleMoveCalculator;
import dev.hilligans.board.*;
import dev.hilligans.board.pieces.*;
import dev.hilligans.game.GameHandler;
import dev.hilligans.spring.SpringHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main {

    public static GameHandler gameHandler = new GameHandler();
    public static String path = "/chesstone/play/";

    public static void main(String[] args) {
        new Thread(() -> SpringHandler.run(args)).start();
        SimpleMoveCalculator simpleMoveCalculator = new SimpleMoveCalculator(3);

      //  Server.main(args);
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

        Repeater repeater = new Repeater(2);
        repeater.rotation = 3;
        board.setPiece(6,7,repeater);
        board.setPiece(5,7,new Redstone());

        board.update();
        board.tick();
        board.tick();





       /* Board board = new Board();
        board.setPiece(1,0,new Redstone());
        board.setPiece(1,1,new Redstone());
        board.setPiece(2,0,new Redstone());
        Repeater repeater = new Repeater(1);
        repeater.delay = 1;
        board.setPiece(2,1,repeater);
        Comparator comparator = new Comparator(2);
        comparator.subtract = true;
        board.setPiece(1,2,comparator);
        board.setPiece(1,3,new RedstoneTorch(2));
        board.setPiece(2,2,new Redstone());
        Repeater repeater1 = new Repeater(1);
        repeater1.rotation = 3;
        repeater1.delay = 3;
        board.setPiece(3,1,repeater1);
        Repeater repeater2 = new Repeater(1);
        repeater2.rotation = 3;
        repeater2.delay = 3;
        board.setPiece(4,1,repeater2);
        Repeater repeater3 = new Repeater(1);
        repeater3.rotation = 3;
        repeater3.delay = 3;
        board.setPiece(5,1,repeater3);
        board.setPiece(6,1, new RedstoneTorch(1));

        */

        board.update();
        board.tick();
        board.tick();
       // board.tick();
       // board.tick();

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
