package dev.hilligans.board.pieces;

import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;

public class Lamp extends Piece {

    public boolean extended = false;

    public Lamp(int team) {
        super(team, new MovementController());
    }

    @Override
    public void tick() {
        super.tick();
        Piece[] pieces = board.getSurroundingSpaces(x,y);
        for(Piece piece : pieces) {
            if(piece != null) {
                if (piece.getFacingDirection().facesTowards(x, y, piece.x, piece.y)) {
                    if (piece.getPowerLevel() != 0) {
                        extended = true;
                    }
                }
            }
        }


    }

    @Override
    public int getExtraData() {
        return extended ? 1 : 0;
    }

    @Override
    public int getID() {
        return 7;
    }
}
