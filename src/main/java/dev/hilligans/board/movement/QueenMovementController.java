package dev.hilligans.board.movement;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.pieces.Redstone;

import java.util.ArrayList;

public class QueenMovementController extends MovementController {

    @Override
    public void getMoveList(ArrayList<Move> moves) {
        super.getMoveList(moves);
        int x = piece.x;
        int y = piece.y;
        Board board = piece.board;
        boolean posX = true;
        boolean negX = true;
        boolean posY = true;
        boolean negY = true;
        for(int a = 1; a < 8; a++) {
            if(a + x < 8 && a + y < 8) {
                if(posX) {
                    Piece piece = board.getPiece(a + x,y + a);
                    if(piece != null) {
                        posX = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,a + x,a + y));
                        }
                    } else {
                        moves.add(new Move(this.piece,a + x,a + y));
                    }
                }
            }

            if(x - a >= 0 && a + y < 8) {
                if(negX) {
                    Piece piece = board.getPiece(x - a,y + a);
                    if(piece != null) {
                        negX = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,x - a,y + a));
                        }
                    } else {
                        moves.add(new Move(this.piece,x - a,y + a));
                    }
                }
            }

            if(x + a < 8 && y - a >= 0) {
                if(posY) {
                    Piece piece = board.getPiece(x + a,y - a);
                    if(piece != null) {
                        posY = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,x + a,y - a));
                        }
                    } else {
                        moves.add(new Move(this.piece,x + a,y - a));
                    }
                }
            }

            if(x - a >= 0 && y - a >= 0) {
                if(negY) {
                    Piece piece = board.getPiece(x - a,y - a);
                    if(piece != null) {
                        negY = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,x - a,y - a));
                        }
                    } else {
                        moves.add(new Move(this.piece,x - a,y - a));
                    }
                }
            }
        }
        posX = true;
        negX = true;
        posY = true;
        negY = true;
        for(int a = 1; a < 8; a++) {
            if(a + x < 8) {
                if(posX) {
                    Piece piece = board.getPiece(a + x,y);
                    if(piece != null) {
                        posX = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,a + x,y));
                        }
                    } else {
                        moves.add(new Move(this.piece,a + x,y));
                    }
                }
            }

            if(x - a >= 0) {
                if(negX) {
                    Piece piece = board.getPiece(x - a,y);
                    if(piece != null) {
                        negX = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,x - a,y));
                        }
                    } else {
                        moves.add(new Move(this.piece,x - a,y));
                    }
                }
            }

            if(y + a < 8) {
                if(posY) {
                    Piece piece = board.getPiece(x,y + a);
                    if(piece != null) {
                        posY = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,x,y + a));
                        }
                    } else {
                        moves.add(new Move(this.piece,x,y + a));
                    }
                }
            }

            if(y - a >= 0) {
                if(negY) {
                    Piece piece = board.getPiece(x,y - a);
                    if(piece != null) {
                        negY = false;
                        if (piece.canBeCapturedBy(this.piece)) {
                            moves.add(new Move(this.piece,x,y - a));
                        }
                    } else {
                        moves.add(new Move(this.piece,x,y - a));
                    }
                }
            }
        }
    }

    @Override
    public void performMove(int startX, int startY, int endX, int endY) {
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
