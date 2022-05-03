package dev.hilligans.board;

import dev.hilligans.board.pieces.Lamp;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Board {

    public Piece[][] board = new Piece[8][8];
    //-1 game active,  0 draw, 1 player 1 win, 2 player 2 win
    public int gameState = -1;

    public Lamp yellowKing;
    public Lamp blueKing;

    public void setPiece(int x, int y, Piece piece) {
        board[x][y] = piece;
        piece.board = this;
        piece.setPos(x,y);
    }

    @Nullable
    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public Piece[] getSurroundingPieces(int x, int y) {
        return null;
    }

    public int getGameState() {
        return gameState;
    }

    public void tick() {
/*
        if(gameState == -1) {
            if (yellowKing.extended && blueKing.extended) {
                gameState = 0;
            } else if (yellowKing.extended) {
                gameState = 2;
            } else if (blueKing.extended) {
                gameState = 1;
            }
        }

 */
    }

    public ArrayList<Move> getAllValidMoves(int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Piece piece = getPiece(x,y);
                if(piece != null) {
                    if(piece.team == player) {
                        piece.getMoveList(moves);
                    }
                }
            }
        }
        return moves;
    }

    public Board duplicate() {
        Board newBoard = new Board();
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Piece piece = getPiece(x,y);
                if(piece != null) {
                    newBoard.setPiece(x,y,piece.clone());
                }
            }
        }
        return newBoard;
    }

    public void applyMove(Move move) {
        Piece piece = getPiece(move.startX,move.startY);
        setPiece(move.endX,move.endY,piece);
    }
}
