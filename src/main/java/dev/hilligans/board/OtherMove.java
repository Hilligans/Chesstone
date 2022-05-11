package dev.hilligans.board;

import java.util.Objects;

public class OtherMove {

    public int newID;
    public int type;

    public int x;
    public int y;

    public OtherMove(Piece piece, int newID) {
        this.x = piece.x;
        this.y = piece.y;
        this.newID = newID;
    }

    public OtherMove(int x, int y, int newID, int type) {
        this.x = x;
        this.y = y;
        this.newID = newID;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OtherMove move = (OtherMove) o;
        return newID == move.newID && x == move.x && y == move.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(newID, x, y);
    }
}
