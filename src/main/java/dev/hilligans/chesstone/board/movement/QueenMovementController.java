package dev.hilligans.chesstone.board.movement;

import dev.hilligans.chesstone.board.Move;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.chesstone.board.pieces.Redstone;

import java.util.ArrayList;

public class QueenMovementController extends MovementController {

    @Override
    public void getMoveList(Piece piece, ArrayList<Move> moves) {
        addMoves(piece, 1, 1, moves);
        addMoves(piece, -1, 1, moves);
        addMoves(piece, 1, -1, moves);
        addMoves(piece, -1, -1, moves);

        addMoves(piece, 1, 0, moves);
        addMoves(piece, -1, 0, moves);
        addMoves(piece, 0, 1, moves);
        addMoves(piece, 0, -1, moves);
    }

    @Override
    public void performMove(Piece piece, int startX, int startY, int endX, int endY, Piece endPiece) {
        int x = Integer.compare(0, startX - endX);
        int y = Integer.compare(0, startY - endY);
        int length = Math.max(Math.abs(startX - endX), Math.abs(startY - endY));
        for(int a = 0; a < length; a++) {
            if(piece.board.validSquare(startX + a * x, startY + a * y)) {
                piece.board.setPiece(startX + a * x, startY + a * y, new Redstone());
            }
        }
    }
}
