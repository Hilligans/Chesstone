package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;

public class Redstone extends Piece {

    int powerLevel;
    int shape;

    public Redstone() {
        super(0, new MovementController());
    }

    @Override
    public boolean canBeCapturedBy(Piece piece) {
        return true;
    }

    @Override
    public void tick() {
        updateDirection();
        updatePower();
    }

    @Override
    public Direction getPullingDirection() {
        return Direction.ALL;
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public Direction getFacingDirection() {
        return Direction.directions[shape];
    }

    @Override
    public int getExtraData() {
        return powerLevel | shape << 4;
    }

    @Override
    public int getPowerLevel() {
        return powerLevel;
    }

    private void updateDirection() {
        shape = 0;
        Piece[] pieces = board.getSurroundingSpaces(x,y);
        for(int x = 0; x < 4; x++) {
            if(pieces[x] != null) {
                Direction direction1 = pieces[x].getPullingDirection();
                if((direction1.redstoneShape & 1 << x) != 0) {
                    shape |= 1 << x;
                }
            }
        }
        if(shape == 1 || shape == 4) {
            shape = 5;
        }
        if(shape == 2 || shape == 8) {
            shape = 10;
        }
        if(shape == 0) {
            shape = 0b1111;
        }
    }

    private void updatePower() {
        int power = powerLevel;
        Piece[] pieces = board.getSurroundingSpaces(x, y);
        for (int x = 0; x < 4; x++) {
            Piece piece = pieces[x];
            if (piece != null) {
                if (piece instanceof Redstone) {
                    power = Math.max(power, pieces[x].getPowerLevel() - 1);
                } else if (piece instanceof Block || piece instanceof TargetBlock) {

                    if (piece.hardPowerLevel() != 0) {
                        power = Math.max(power, piece.hardPowerLevel());
                    }
                } else if(piece.getFacingDirection().facesTowards(this.x,this.y,piece.x,piece.y)) {
                    power = Math.max(power, piece.getPowerLevel());
                } else {
                    power = Math.max(power, piece.getPowerLevel());
                }
            }
        }
        int old = powerLevel;
        powerLevel = power;

        if(power != old) {
            for(int x = 0; x < 4; x++) {
                if(pieces[x] != null) {
                    pieces[x].tick();
                }
            }
        }
    }
}
