package dev.hilligans.chesstone.board;

import dev.hilligans.chesstone.board.movement.MovementController;

import java.util.ArrayList;

public abstract class Piece implements Cloneable {

    public int team;
    public Board board;
    public MovementController movementController;
    public int x,y;

    public Piece(int team, MovementController movementController) {
        this.team = team;
        this.movementController = movementController;
    }

    public Piece() {}

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

    public StateChangeMove[] getModeMoves() {
        return new StateChangeMove[0];
    }

    public StateChangeMove[] getRotationMoves() {
        return new StateChangeMove[0];
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
        movementController.getMoveList(this, moves);
    }

    public void canDoMove(IMove move) {

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

    public Piece setDataFrom(Piece piece) {
        this.team = piece.team;
        this.x = piece.x;
        this.y = piece.y;
        this.movementController = piece.movementController;
        this.board = piece.board;
        return this;
    }

    public abstract Piece copy();

    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (Exception ignored) {}
        return null;
    }
}
