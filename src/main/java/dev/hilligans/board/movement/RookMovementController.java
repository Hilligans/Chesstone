package dev.hilligans.board.movement;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.pieces.Redstone;

import java.util.ArrayList;

public class RookMovementController extends MovementController {

    @Override
    public void getMoveList(Piece piece, ArrayList<Move> moves) {
        addMoves(piece, 1,0,moves);
        addMoves(piece, 0,1,moves);
        addMoves(piece, -1,0,moves);
        addMoves(piece, 0,-1,moves);
    }

    @Override
    public void performMove(Piece piece, int startX, int startY, int endX, int endY, Piece endPiece) {
        if(startX != endX) {
            if(startX < endX) {
                for (int x = startX; x < endX; x++) {
                    piece.board.setPiece(x, endY, new Redstone());
                }
            } else {
                for (int x = startX; x > endX; x--) {
                    piece.board.setPiece(x, endY, new Redstone());
                }
            }
        } else {
            if(startY < endY) {
                for (int y = startY; y < endY; y++) {
                    piece.board.setPiece(endX, y, new Redstone());
                }
            } else {
                for (int y = startY; y > endY; y--) {
                    piece.board.setPiece(endX, y, new Redstone());
                }
            }
        }
    }
}
