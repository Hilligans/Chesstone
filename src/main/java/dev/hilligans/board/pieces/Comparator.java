package dev.hilligans.board.pieces;

import dev.hilligans.board.Piece;

public class Comparator extends Piece {
    public Comparator(int team) {
        super(team);
    }

    @Override
    protected int getID() {
        return 6;
    }
}
