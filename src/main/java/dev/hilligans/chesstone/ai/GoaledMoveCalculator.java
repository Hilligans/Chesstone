package dev.hilligans.chesstone.ai;

import dev.hilligans.chesstone.board.Board;
import dev.hilligans.chesstone.board.Move;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.board.pieces.*;
import dev.hilligans.chesstone.board.pieces.*;

public class GoaledMoveCalculator extends MoveCalculator {



    @Override
    public Move findMove(Board board, int player) {
        if(player == 1) {
            Lamp king = board.yellowKing;
            Piece[] pieces = board.getSurroundingSpaces(king.x, king.y);
            for(Piece piece : pieces) {
                if(piece instanceof Block block) {
                    calculateBlockPoweredGoal(board, block, player);
                } else if(piece instanceof TargetBlock targetBlock) {
                    calculateTargetBlockPoweredGoal(board, targetBlock, player);
                }
            }
        }

        return null;
    }

    public int calculateBlockPoweredGoal(Board board, Block block, int player) {
        Piece[] pieces = board.getSurroundingSpaces(block.x, block.y);
        for(Piece piece : pieces) {
            if(piece.getFacingDirection().facesTowards(block.x, block.y, piece.x, piece.y)) {
                if(piece instanceof Redstone redstone) {
                    return calculateRedstonePowerGoal(board, redstone, player);
                } else if (piece instanceof Repeater repeater) {
                    return calculateRepeaterPowerGoal(board, repeater, player);
                } else if(piece instanceof Comparator comparator) {

                }
            }
        }
        return 0;
    }

    public int calculateTargetBlockPoweredGoal(Board board, TargetBlock block, int player) {
        Piece[] pieces = board.getSurroundingSpaces(block.x, block.y);
        for(Piece piece : pieces) {
            if(piece.getFacingDirection().facesTowards(block.x, block.y, piece.x, piece.y) || piece instanceof Redstone) {
                if(piece instanceof Redstone redstone) {
                    return calculateRedstonePowerGoal(board, redstone, player);
                }
            }
        }
        return 0;
    }

    public int calculateRedstonePowerGoal(Board board, Redstone piece, int player) {


        return 0;
    }

    public int calculateRepeaterPowerGoal(Board board, Repeater piece, int player) {

        return 0;
    }

    public int recursiveCalculateGoal(Board board, int player, long goal) {
        int x = (int) (goal & 0b111);
        int y = (int) ((goal >> 3) & 0b1111);
        int state = (int)(goal >> 6);


        return 0;
    }
}
