package dev.hilligans.board.pieces;

import dev.hilligans.Main;
import dev.hilligans.board.BoardBuilder;
import dev.hilligans.board.Direction;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.KnightMovementController;
import dev.hilligans.board.movement.MovementController;

public class TargetBlock extends Piece {

    public int powerLevel;
    public int hardPower;

    public TargetBlock(int team) {
        this(team, BoardBuilder.STANDARD_KNIGHT_CONTROLLER);
    }

    public TargetBlock(int team, MovementController movementController) {
        super(team, movementController);
    }

    public int hardPowerLevel() {
        return hardPower;
    }

    @Override
    public int getPowerLevel() {
        return powerLevel;
    }

    @Override
    public Direction getFacingDirection() {
        return Direction.NONE;
    }

    @Override
    public Direction getPullingDirection() {
        return Direction.ALL;
    }

    @Override
    public void update() {
        int power = 0;
        int hPower = 0;
        Piece[] pieces = board.getSurroundingSpaces(x, y);
        for (int x = 0; x < 4; x++) {
            Piece piece = pieces[x];
            if (piece != null) {
                if(piece.getFacingDirection().facesTowards(piece.x,piece.y,this.x,this.y)) {
                    if(piece.hardPowers()) {
                        hPower = Math.max(hPower, piece.hardPowerLevel());
                    }
                    power = Math.max(power, piece.getPowerLevel());
                }
            }
        }
        int old = powerLevel;
        int old1 = hardPower;
        powerLevel = power;
        hardPower = hPower;

        if(power != old || old1 != hPower) {
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
    public void onPlace() {
        update();
    }

    @Override
    public Piece copy() {
        TargetBlock targetBlock = new TargetBlock(team);
        targetBlock.setDataFrom(this);
        targetBlock.powerLevel = powerLevel;
        targetBlock.hardPower = hardPower;

        return targetBlock;
    }

    @Override
    public int getID() {
        return 4;
    }

    @Override
    public String toString() {
        return "TargetBlock{" +
                "powerLevel=" + powerLevel +
                ", hardPower=" + hardPower +
                '}';
    }
}
