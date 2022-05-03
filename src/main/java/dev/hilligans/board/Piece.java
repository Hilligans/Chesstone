package dev.hilligans.board;

import java.util.ArrayList;

public abstract class Piece implements Cloneable {

    public int team;
    public Board board;
    public int x,y;

    public Piece(int team) {
        this.team = team;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean canBeCapturedBy(Piece piece) {
        return piece.getClass().equals(this.getClass()) && piece.team != team;
    }

    public int getTeam() {
        return team;
    }

    public int getPowerLevel() {
        return 0;
    }

    public void tick() {}

    public Direction getFacingDirection() {
        return Direction.NONE;
    }

    public Direction getPullingDirection() {
        return Direction.NONE;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "team=" + team +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void getMoveList(ArrayList<Move> moves) {
    }

    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (Exception ignored) {}
        return null;
    }
}
