package dev.hilligans.chesstone.board.movement;

import dev.hilligans.chesstone.board.Board;
import dev.hilligans.chesstone.board.Move;
import dev.hilligans.chesstone.board.Piece;

import java.util.ArrayList;

public class PawnMovementController extends MovementController {
    @Override
    public void getMoveList(Piece piece, ArrayList<Move> moves) {
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
            if(y == 4) {
                if(board.lastMove instanceof Move move) {
                    Piece piece1 = board.getPiece(move.endX, move.endY);
                    if(piece1 != null && piece1.team != piece.team && piece1.movementController instanceof PawnMovementController) {
                        if(move.startX == move.endX && move.startY == 6 && move.endY == 4) {
                            moves.add(new Move(piece, piece1.x, y + 1));
                        }
                    }
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
            if(y == 3) {
                if(board.lastMove instanceof Move move) {
                    Piece piece1 = board.getPiece(move.endX, move.endY);
                    if(piece1 != null && piece1.team != piece.team && piece1.movementController instanceof PawnMovementController) {
                        if(move.startX == move.endX && move.startY == 1 && move.endY == 3) {
                            moves.add(new Move(piece, piece1.x, y - 1));
                        }
                    }
                }
            }
        }
    }


    @Override
    public void performMove(Piece piece, int startX, int startY, int endX, int endY, Piece endPiece) {
        if(endPiece == null && startX != endX) {
            Board board = piece.board;
            if(startY == 3) {
                board.setPiece(endX, 3, null);
            } else if(startY == 4) {
                board.setPiece(endX, 4, null);
            }
        }
    }
}
