package dev.hilligans.board.pieces;

import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;

import java.util.ArrayList;

public class RedstoneTorch extends Piece {

    public RedstoneTorch(int team) {
        super(team);
    }

    @Override
    public void getMoveList(ArrayList<Move> moves) {
        boolean posX = true;
        boolean negX = true;
        boolean posY = true;
        boolean negY = true;
        for(int a = 1; a < 8; a++) {
            if(a + x < 8) {
                if(posX) {
                    Piece piece = board.getPiece(a + x,y);
                    if(piece != null) {
                        posX = false;
                        if (board.getPiece(a + x, y).canBeCapturedBy(this)) {
                            moves.add(new Move(this,a + x,y));
                        }
                    } else {
                        moves.add(new Move(this,a + x,y));
                    }
                }
            }

            if(x - a >= 0) {
                if(negX) {
                    Piece piece = board.getPiece(x - a,y);
                    if(piece != null) {
                        negX = false;
                        if (board.getPiece(x - a, y).canBeCapturedBy(this)) {
                            moves.add(new Move(this,x - a,y));
                        }
                    } else {
                        moves.add(new Move(this,x - a,y));
                    }
                }
            }

            if(y + a < 8) {
                if(posY) {
                    Piece piece = board.getPiece(x,y + a);
                    if(piece != null) {
                        posY = false;
                        if (board.getPiece(x, y + a).canBeCapturedBy(this)) {
                            moves.add(new Move(this,x,y + a));
                        }
                    } else {
                        moves.add(new Move(this,x,y + a));
                    }
                }
            }

            if(y - a >= 0) {
                if(negY) {
                    Piece piece = board.getPiece(x,y - a);
                    if(piece != null) {
                        negY = false;
                        if (board.getPiece(x, y - a).canBeCapturedBy(this)) {
                            moves.add(new Move(this,x,y - a));
                        }
                    } else {
                        moves.add(new Move(this,x,y - a));
                    }
                }
            }
        }
    }
}
