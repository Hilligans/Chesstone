package dev.hilligans.board.movement;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;

import java.util.ArrayList;

public class PawnMovementController extends MovementController {
    @Override
    public void getMoveList(ArrayList<Move> moves) {
        int x = piece.x;
        int y = piece.y;
        Board board = piece.board;
        if(piece.team == 1) {
            if(board.getPiece(x,y+1) == null) {
                moves.add(new Move(piece,x,y+1));
                if(y == 1) {
                    if(board.getPiece(x,y+2) == null) {
                        moves.add(new Move(piece,x,y+2));
                    }
                }
            }
            if(x != 0) {
                Piece piece1 = board.getPiece(x-1,y+1);
                if(piece1 != null && piece1.canBeCapturedBy(piece)) {
                    moves.add(new Move(piece,x-1,y+1));
                }
            }
            if(x != 7) {
                Piece piece1 = board.getPiece(x+1,y+1);
                if(piece1 != null && piece1.canBeCapturedBy(piece)) {
                    moves.add(new Move(piece,x+1,y+1));
                }
            }
        } else {
            if(board.getPiece(x,y-1) == null) {
                moves.add(new Move(piece,x,y-1));
                if(y == 6) {
                    if(board.getPiece(x,y-2) == null) {
                        moves.add(new Move(piece,x,y-2));
                    }
                }
            }
            if(x != 0) {
                Piece piece1 = board.getPiece(x-1,y-1);
                if(piece1 != null && piece1.canBeCapturedBy(piece)) {
                    moves.add(new Move(piece,x-1,y-1));
                }
            }
            if(x != 7) {
                Piece piece1 = board.getPiece(x+1,y-1);
                if(piece1 != null && piece1.canBeCapturedBy(piece)) {
                    moves.add(new Move(piece,x+1,y-1));
                }
            }
        }
    }
}
