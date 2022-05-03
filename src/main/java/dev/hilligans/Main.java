package dev.hilligans;

import dev.hilligans.ai.SimpleMoveCalculator;
import dev.hilligans.board.Board;
import dev.hilligans.board.pieces.Redstone;
import dev.hilligans.board.pieces.RedstoneTorch;

public class Main {

    public static void main(String[] args) throws Exception {
        SimpleMoveCalculator simpleMoveCalculator = new SimpleMoveCalculator(3);
        Board board = new Board();
        board.setPiece(0,0,new RedstoneTorch(1));
        board.setPiece(7,7,new RedstoneTorch(2));
        System.out.println(simpleMoveCalculator.findMove(board,1));
    }
}
