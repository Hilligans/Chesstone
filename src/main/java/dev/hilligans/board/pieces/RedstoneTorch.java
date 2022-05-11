package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.RookMovementController;

import java.util.ArrayList;

public class RedstoneTorch extends Piece {

    public RedstoneTorch(int team) {
        super(team, new RookMovementController());
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
    public String toString() {
        return super.toString();
    }
}
