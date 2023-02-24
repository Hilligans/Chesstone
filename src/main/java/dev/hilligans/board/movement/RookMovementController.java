package dev.hilligans.board.movement;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.pieces.Redstone;

import java.util.ArrayList;

public class RookMovementController extends MovementController {

    @Override
    public void getMoveList(ArrayList<Move> moves) {
        addMoves(1,0,moves);
        addMoves(0,1,moves);
        addMoves(-1,0,moves);
        addMoves(0,-1,moves);
    }

    public void addMoves(int mulX, int mulY, ArrayList<Move> moves) {
        int x = piece.x;
        int y = piece.y;
        Board board = piece.board;
        for(int a = 1; a < bounds.getSize(); a++) {
            if(bounds.isInBounds(x + a * mulX, y + a * mulY)) {
                Piece piece = board.getPiece(x,y - a);
                if(piece != null) {
                    if (piece.canBeCapturedBy(this.piece)) {
                        moves.add(new Move(this.piece,x,y - a));
                    }
                    return;
                } else {
                    moves.add(new Move(this.piece,x,y - a));
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void performMove(int startX, int startY, int endX, int endY, Piece endPiece) {
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
