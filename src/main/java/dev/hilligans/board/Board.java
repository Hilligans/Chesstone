package dev.hilligans.board;

public class Board {

    public Piece[][] board = new Piece[8][8];

    public void setPiece(int x, int y, Piece piece) {
        board[x][y] = piece;
        piece.board = this;
        piece.setPos(x,y);
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public Piece[] getSurroundingPieces(int x, int y) {
        return null;
    }
}
