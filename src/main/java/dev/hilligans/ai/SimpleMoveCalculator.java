package dev.hilligans.ai;

import dev.hilligans.Main;
import dev.hilligans.board.Board;
import dev.hilligans.board.Move;

import java.util.ArrayList;

public class SimpleMoveCalculator extends MoveCalculator {

    public int depth;
    public int boardTickDepth = 6;

    public SimpleMoveCalculator(int depth) {
        this.depth = depth;
    }


    @Override
    public Move findMove(Board board, int player) {
        int biggestMove = 0;
        Move bestMove = null;
        for(Move move : board.getAllValidMoves(player)) {
            int moveValue = recursiveCalculateMove(board,player,move, 0);
            if(player == 1) {
                if (moveValue > biggestMove) {
                    biggestMove = moveValue;
                    bestMove = move;
                }
            } else {
                if(moveValue < biggestMove) {
                    biggestMove = moveValue;
                    bestMove = move;
                }
            }
            if(Math.abs(moveValue) > 100) {
                System.out.println("Score: " + biggestMove);
            }
        }
        return bestMove;
    }

    public int recursiveCalculateMove(Board board, int player, Move move, int depth) {
        Board board1 = board.duplicate();
        move.applyMove(board1);
        if(board.blueKing.extended && board.yellowKing.extended) {
            return 0;
        } else if(board.blueKing.extended) {
            return Integer.MAX_VALUE;
        } else if(board.yellowKing.extended) {
            return Integer.MIN_VALUE;
        }
        if(depth < this.depth) {
            player = player == 1 ? 2 : 1;
            int biggestMove = 0;
            for(Move move1 : board1.getAllValidMoves(player)) {
                int moveValue = recursiveCalculateMove(board1,player,move1,depth + 1);
                if(player == 1) {
                    if (moveValue > biggestMove) {
                        biggestMove = moveValue;
                    }
                } else {
                    if(moveValue < biggestMove) {
                        biggestMove = moveValue;
                    }
                }
            }
            return biggestMove;
        } else {
           return calculateBoard(board1);
        }
    }

    public int calculateBoard(Board board) {
        board.update();
        for(int x = 0; x < boardTickDepth; x++) {
            board.tick();
        }
        int player1Pieces = board.getPieces(1);
        int player2Pieces = board.getPieces(2);
        if(player1Pieces > player2Pieces) {
            return 10;
        } else if(player1Pieces < player2Pieces) {
            return  -10;

        }
        return 0;
    }
}
