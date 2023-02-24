package dev.hilligans.board.movement;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.pieces.Redstone;

import java.util.ArrayList;

public class QueenMovementController extends MovementController {

    @Override
    public void getMoveList(ArrayList<Move> moves) {
        addMoves(1, 1, moves);
        addMoves(-1, 1, moves);
        addMoves(1, -1, moves);
        addMoves(-1, -1, moves);

        addMoves(1, 0, moves);
        addMoves(-1, 0, moves);
        addMoves(0, 1, moves);
        addMoves(0, -1, moves);
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
