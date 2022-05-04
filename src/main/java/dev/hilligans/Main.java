package dev.hilligans;

import dev.hilligans.ai.SimpleMoveCalculator;
import dev.hilligans.board.Board;
import dev.hilligans.board.Piece;
import dev.hilligans.board.pieces.Redstone;
import dev.hilligans.board.pieces.RedstoneTorch;
import dev.hilligans.spring.SpringHandler;
import dev.hilligans.util.ConsoleReader;

import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) throws Exception {
        new Thread(() -> SpringHandler.run(args)).start();
        SimpleMoveCalculator simpleMoveCalculator = new SimpleMoveCalculator(3);

        new ConsoleReader(s -> {
            if(s.equals("stop")) {
                System.exit(0);
            }
        }).run();

        Board board = new Board();
        board.setPiece(0,0,new RedstoneTorch(1));
        board.setPiece(7,0,new RedstoneTorch(2));
     //   System.out.println(simpleMoveCalculator.findMove(board,1));
    }
}
