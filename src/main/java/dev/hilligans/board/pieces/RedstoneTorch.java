package dev.hilligans.board.pieces;

import dev.hilligans.board.BoardBuilder;
import dev.hilligans.board.Direction;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;
import dev.hilligans.board.movement.RookMovementController;

import java.util.ArrayList;

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
