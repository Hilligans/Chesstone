package dev.hilligans.chesstone.board.pieces;

import dev.hilligans.chesstone.board.BoardBuilder;
import dev.hilligans.chesstone.board.Direction;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.chesstone.board.movement.MovementController;

public class RedstoneBlock extends Piece {
    public RedstoneBlock() {
        this(0,  BoardBuilder.EMPTY_MOVEMENT_CONTROLLER);
    }

    public RedstoneBlock(int team, MovementController movementController) {
        super(team, movementController);
    }


    @Override
    public int getPowerLevel() {
        return 15;
    }

    @Override
    public Direction getPullingDirection() {
        return Direction.ALL;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public Piece copy() {
        return null;
    }
}
