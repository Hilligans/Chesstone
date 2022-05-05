package dev.hilligans.board.pieces;

import dev.hilligans.board.Piece;

public class Lamp extends Piece {

    public boolean extended = false;

    public Lamp(int team) {
        super(team);
    }

    @Override
    public void tick() {
        super.tick();
        Piece[] pieces = board.getSurroundingPieces(x,y);
        for(Piece piece : pieces) {
            if(piece.getFacingDirection().facesTowards(x,y,piece.x,piece.y)) {
                if(piece.getPowerLevel() != 0) {
                    extended = true;
                }
            }
        }
    }

    @Override
    protected int getID() {
        return 7;
    }
}
