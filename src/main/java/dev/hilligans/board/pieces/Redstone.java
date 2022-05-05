package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.Piece;

public class Redstone extends Piece {

    public Direction direction;

    public Redstone() {
        super(0);
    }

    @Override
    public boolean canBeCapturedBy(Piece piece) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Direction getPullingDirection() {
        return Direction.ALL;
    }

    @Override
    protected int getID() {
        return 1;
    }

    @Override
    public Direction getFacingDirection() {
        return direction;
    }

    private void updateDirection() {

    }
}
