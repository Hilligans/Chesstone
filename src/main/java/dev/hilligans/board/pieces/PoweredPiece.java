package dev.hilligans.board.pieces;

import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;

public abstract class PoweredPiece extends Piece {

    public PoweredPiece(int team) {
        super(team, new MovementController());
    }

}
