package dev.hilligans.board.pieces;

import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.PawnMovementController;

public class Block extends Piece {

    public int powerLevel;
    public int hardPower;

    public Block(int team) {
        super(team, new PawnMovementController());
    }

    @Override
    public int hardPowerLevel() {
        return hardPower;
    }

    @Override
    public int getPowerLevel() {
        return powerLevel;
    }

    @Override
    public void update() {
        int power = 0;
        int hPower = 0;
        Piece[] pieces = board.getSurroundingSpaces(x, y);
        for (int x = 0; x < 4; x++) {
            Piece piece = pieces[x];
            if (piece != null) {
                if(piece.getFacingDirection().facesTowards(this.x,this.y,piece.x,piece.y)) {
                    if(piece.hardPowers()) {
                        hPower = Math.max(power, piece.hardPowerLevel());
                    } else {
                        power = Math.max(power, piece.getPowerLevel());
                    }
                }
            }
        }
        int old = powerLevel;
        int old1 = hardPower;
        powerLevel = power;
        hardPower = hPower;

        if(power != old || old1 != hPower) {
            for(int x = 0; x < 4; x++) {
                if(pieces[x] != null) {
                    pieces[x].update();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Block{" +
                "powerLevel=" + powerLevel +
                ", hardPower=" + hardPower +
                '}';
    }

    @Override
    public int getID() {
        return 2;
    }
}
