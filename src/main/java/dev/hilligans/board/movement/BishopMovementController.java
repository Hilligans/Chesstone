package dev.hilligans.board.movement;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.pieces.Redstone;

import java.util.ArrayList;

public class BishopMovementController extends MovementController {

    @Override
    public void getMoveList(Piece piece, ArrayList<Move> moves) {
        addMoves(piece, 1,1,moves);
        addMoves(piece, 1,-1,moves);
        addMoves(piece, -1,1,moves);
        addMoves(piece, -1,-1,moves);
    }

    @Override
    public void performMove(Piece piece, int startX, int startY, int endX, int endY, Piece endPiece) {
        int x = startX - endX > 0 ? -1 : 1;
        int y = startY - endY > 0 ? -1 : 1;
        int length = Math.max(Math.abs(startX - endX), Math.abs(startY - endY));
        for(int a = 0; a < length; a++) {
            if(piece.board.validSquare(startX + a * x, startY + a * y)) {
                piece.board.setPiece(startX + a * x, startY + a * y, new Redstone());
            }
        }
    }
}
