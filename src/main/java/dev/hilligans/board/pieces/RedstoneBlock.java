package dev.hilligans.board.pieces;

import dev.hilligans.board.BoardBuilder;
import dev.hilligans.board.Direction;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;

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
