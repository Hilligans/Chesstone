package dev.hilligans.board;

import dev.hilligans.board.movement.MovementController;

import java.util.ArrayList;

public abstract class Piece implements Cloneable {

    public int team;
    public Board board;
    public MovementController movementController;
    public int x,y;

    public Piece(int team, MovementController movementController) {
        this.team = team;
        this.movementController = movementController;
        movementController.piece = this;
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

    public void update() {}

    public Direction getFacingDirection() {
        return Direction.NONE;
    }

    public Direction getPullingDirection() {
        return Direction.NONE;
    }

    public boolean hardPowers() {
        return false;
    }

    public int hardPowerLevel() {
        return 0;
    }

    public OtherMove[] getModeMoves() {
        return new OtherMove[0];
    }

    public OtherMove[] getRotationMoves() {
        return new OtherMove[0];
    }

    public Piece[] getSurroundingPieces() {
        return board.getSurroundingSpaces(x, y);
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
        movementController.getMoveList(moves);
    }

    public abstract int getID();

    public int getExtraData() {
        return 0;
    }

    public void decodeData(int data) {}

    public short getPacket() {
        return (short) ((team == 2 ? 1 : 0) | getID() << 1 | getExtraData() << 4);
    }

    public void onPlace() {}
    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (Exception ignored) {}
        return null;
    }
}
