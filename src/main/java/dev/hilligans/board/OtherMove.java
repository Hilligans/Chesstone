package dev.hilligans.board;

public class OtherMove {
    public Piece piece;
    public int newID;

    public OtherMove(Piece piece, int newID) {
        this.piece = piece;
        this.newID = newID;
    }
}
