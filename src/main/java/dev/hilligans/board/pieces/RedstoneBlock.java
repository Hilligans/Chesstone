package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;

public class RedstoneBlock extends Piece {
    public RedstoneBlock() {
        super(0,new MovementController());
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
}
