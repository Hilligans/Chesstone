package dev.hilligans.board;

public abstract class Piece {

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
}
