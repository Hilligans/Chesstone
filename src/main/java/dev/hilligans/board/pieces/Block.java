package dev.hilligans.board.pieces;

import dev.hilligans.board.Piece;

public class Block extends Piece {
    public Block(int team) {
        super(team);
    }

    @Override
    protected int getID() {
        return 2;
    }
}
