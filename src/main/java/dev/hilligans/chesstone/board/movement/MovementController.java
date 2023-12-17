package dev.hilligans.chesstone.board.movement;

import dev.hilligans.chesstone.board.Board;
import dev.hilligans.chesstone.board.Move;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.chesstone.board.bounds.IBoardBounds;
import dev.hilligans.chesstone.board.bounds.StandardBoardBounds;

import java.util.ArrayList;

public class MovementController {

    public IBoardBounds bounds = new StandardBoardBounds();

    public void getMoveList(Piece piece, ArrayList<Move> moves) {}

    public void performMove(Piece piece, int startX, int startY, int endX, int endY, Piece endPiece) {}


    public void addMoves(Piece piece, int mulX, int mulY, ArrayList<Move> moves) {
        int x = piece.x;
        int y = piece.y;
        Board board = piece.board;
        for(int a = 1; a < bounds.getSize(); a++) {
            if(bounds.isInBounds(x + a * mulX, y + a * mulY)) {
                Piece piece1 = board.getPiece(x + a * mulX,y + a * mulY);
                if(piece1 != null) {
                    if (piece1.canBeCapturedBy(piece)) {
                        moves.add(new Move(piece,x + a * mulX,y + a * mulY));
                    }
                    return;
                } else {
                    moves.add(new Move(piece,x + a * mulX,y + a * mulY));
                }
            } else {
                return;
            }
        }
    }
}
