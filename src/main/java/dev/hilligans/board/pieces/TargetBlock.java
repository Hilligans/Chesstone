package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.Piece;

public class TargetBlock extends Piece {



    public TargetBlock(int team) {
        super(team);
    }

    @Override
    public Direction getFacingDirection() {
        return Direction.ALL;
    }
}
