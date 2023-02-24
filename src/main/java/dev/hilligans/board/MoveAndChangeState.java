package dev.hilligans.board;

import dev.hilligans.game.Game;

public class MoveAndChangeState implements IMove {

    public int startX, startY;
    public int endX, endY;

    public MoveAndChangeState(Piece piece, int endX, int endY) {
        startX = piece.x;
        startY = piece.y;
        this.endX = endX;
        this.endY = endY;
    }

    public MoveAndChangeState(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveAndChangeState move = (MoveAndChangeState) o;
        return startX == move.startX && startY == move.startY && endX == move.endX && endY == move.endY;
    }


    @Override
    public int getType() {
        return 3;
    }

    @Override
    public int getPos() {
        return 0;
    }

    @Override
    public int getData() {
        return 0;
    }

    @Override
    public boolean makeMove(Game game) {
        return false;
    }

    @Override
    public void applyMove(Board board) {

    }
}
