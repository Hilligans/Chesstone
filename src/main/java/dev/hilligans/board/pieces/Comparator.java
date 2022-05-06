package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.OtherMove;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;
import dev.hilligans.board.movement.QueenMovementController;

public class Comparator extends Piece {

    public int rotation = 0;
    int powerLevel;
    boolean subtract;
    public Comparator(int team) {
        super(team, new QueenMovementController());
        rotation = team == 2 ? 2 : 0;
    }

    @Override
    public boolean hardPowers() {
        return true;
    }

    @Override
    public Direction getPullingDirection() {
        return Direction.ALL;
    }

    @Override
    public int getID() {
        return 6;
    }

    @Override
    public OtherMove[] getRotationMoves() {
        return new OtherMove[] {
                new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 0 << 1 | (subtract ? 1 : 0) << 3)),
                new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 1 << 1 | (subtract ? 1 : 0) << 3)),
                new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 2 << 1 | (subtract ? 1 : 0) << 3)),
                new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 3 << 1 | (subtract ? 1 : 0) << 3))};
    }

    @Override
    public OtherMove[] getModeMoves() {
        return new OtherMove[] {
                new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | rotation << 1 | (0) << 3)),
                new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | rotation << 1 | (1) << 3))};
    }

    private int build(int data) {
        return (short) ((team == 2 ? 1 : 0) | getID() << 1 | data << 4);
    }
    @Override
    public int getExtraData() {
        return (powerLevel != 0 ? 1 : 0) | rotation << 1 | (subtract ? 1 : 0) << 3;
    }
}
