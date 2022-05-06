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
    private void updatePower() {
        int power = 0;
        int hPower = 0;
        Piece[] pieces = board.getSurroundingSpaces(x, y);
        for (int x = 0; x < 4; x++) {
            Piece piece = pieces[x];
            if (piece != null) {
                System.out.println(piece.getID());
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
                    pieces[x].tick();
                }
            }
        }
    }

    @Override
    public void tick() {
        updatePower();
    }

    @Override
    public int getID() {
        return 2;
    }
}
