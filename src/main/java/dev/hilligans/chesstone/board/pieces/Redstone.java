package dev.hilligans.chesstone.board.pieces;

import dev.hilligans.Main;
import dev.hilligans.chesstone.board.BoardBuilder;
import dev.hilligans.chesstone.board.Direction;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.chesstone.board.movement.MovementController;

public class Redstone extends Piece {

    public int powerLevel;
    public int shape;

    public Redstone() {
        this(0, BoardBuilder.EMPTY_MOVEMENT_CONTROLLER);
    }

    public Redstone(int team, MovementController movementController) {
        super(team, movementController);
    }


    @Override
    public boolean canBeCapturedBy(Piece piece) {
        return true;
    }

    @Override
    public void update() {
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
    public Piece copy() {
        Redstone redstone = new Redstone();
        redstone.setDataFrom(this);
        redstone.powerLevel = powerLevel;
        redstone.shape = shape;
        return redstone;
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
                if((direction1.redstoneShape & (1 << x)) != 0) {
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
        int power = 0;
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
                } else if(!(piece instanceof Comparator || piece instanceof Repeater)) {
                    power = Math.max(power, piece.getPowerLevel());
                }
            }
        }
        int old = powerLevel;
        powerLevel = power;

        if(power != old) {
            if(Main.gameHandler.logUpdates) {
                System.out.println("(" + x + "," + y + ") " + this);
            }
            for(int x = 0; x < 4; x++) {
                if(pieces[x] != null) {
                    pieces[x].update();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Redstone{" +
                "powerLevel=" + powerLevel +
                ", shape=" + shape +
                '}';
    }
}
