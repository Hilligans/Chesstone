package dev.hilligans.chesstone.board.pieces;

import dev.hilligans.chesstone.board.BoardBuilder;
import dev.hilligans.chesstone.board.Direction;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.chesstone.board.movement.MovementController;

public class RedstoneTorch extends Piece {

    public RedstoneTorch(int team) {
        this(team, BoardBuilder.STANDARD_ROOK_CONTROLLER);
    }

    public RedstoneTorch(int team, MovementController movementController) {
        super(team, movementController);
    }


    @Override
    public Direction getPullingDirection() {
        return Direction.ALL;
    }

    @Override
    public Direction getFacingDirection() {
        return Direction.NONE;
    }

    @Override
    public int getPowerLevel() {
        return 15;
    }

    @Override
    public int getID() {
        return 3;
    }

    @Override
    public Piece copy() {
        RedstoneTorch redstoneTorch = new RedstoneTorch(team);
        redstoneTorch.setDataFrom(this);
        return redstoneTorch;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
