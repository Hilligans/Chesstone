package dev.hilligans.board;

public class Move {

    public int startX,startY;
    public int endX,endY;

    public Move(Piece piece, int endX, int endY) {
        startX = piece.x;
        startY = piece.y;
        this.endX = endX;
        this.endY = endY;
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
