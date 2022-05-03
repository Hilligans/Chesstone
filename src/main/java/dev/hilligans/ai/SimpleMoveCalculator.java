package dev.hilligans.ai;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;

import java.util.ArrayList;

public class SimpleMoveCalculator extends MoveCalculator {

    public int depth;
    public int boardTickDepth = 8;

    public SimpleMoveCalculator(int depth) {
        this.depth = depth;
    }


    @Override
    public Move findMove(Board board, int player) {
        int biggestMove = 0;
        Move bestMove = null;
        for(Move move : board.getAllValidMoves(player)) {
            int moveValue = recursiveCalculateMove(board,player,move, 0,player);
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
        }
        return bestMove;
    }

    public int recursiveCalculateMove(Board board, int player, Move move, int depth, int targetPlayer) {
        Board board1 = board.duplicate();
        board1.applyMove(move);
        if(depth >= this.depth) {
            player = player == 1 ? 2 : 1;
            int biggestMove = 0;
            for(Move move1 : board1.getAllValidMoves(player)) {
                int moveValue = recursiveCalculateMove(board1,player,move1,depth + 1,targetPlayer);
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
        for(int x = 0; x < boardTickDepth; x++) {
            board.tick();
        }
        return board.getGameState() == 2 ? -1 : 1;
    }
}
