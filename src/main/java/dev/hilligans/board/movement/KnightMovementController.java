package dev.hilligans.board.movement;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;

import java.util.ArrayList;

public class KnightMovementController extends MovementController {

    @Override
    public void getMoveList(ArrayList<Move> moves) {
        add(2,1,moves);
        add(-2,1,moves);
        add(2,-1,moves);
        add(-2,-1,moves);

        add(1,2,moves);
        add(-1,2,moves);
        add(1,-2,moves);
        add(-1,-2,moves);
    }

    private void add(int x, int y, ArrayList<Move> moves) {
        if(piece.board.validSquare(x + piece.x, y + piece.y)) {
            Piece piece1 = piece.board.getPiece(x + piece.x, y + piece.y);
            if(piece1 == null || piece1.canBeCapturedBy(piece)) {
                moves.add(new Move(piece,x + piece.x, y + piece.y));
            }
        }
    }
}
