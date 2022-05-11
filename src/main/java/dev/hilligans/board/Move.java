package dev.hilligans.board;

import java.util.Objects;

public class Move {

    public int startX,startY;
    public int endX,endY;

    public Move(Piece piece, int endX, int endY) {
        startX = piece.x;
        startY = piece.y;
        this.endX = endX;
        this.endY = endY;
    }

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return startX == move.startX && startY == move.startY && endX == move.endX && endY == move.endY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startX, startY, endX, endY);
    }

    @Override
    public String toString() {
        return "Move{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                '}';
    }
}
