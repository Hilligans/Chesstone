package dev.hilligans.chesstone.board.pieces;

import dev.hilligans.Main;
import dev.hilligans.chesstone.board.BoardBuilder;
import dev.hilligans.chesstone.board.Piece;
import dev.hilligans.chesstone.board.StateChangeMove;
import dev.hilligans.chesstone.board.movement.MovementController;

public class Block extends Piece {

    public int powerLevel;
    public int hardPower;

    public Block(int team) {
        this(team, BoardBuilder.STANDARD_PAWN_CONTROLLER);
    }

    public Block(int team, MovementController movementController) {
        super(team, movementController);
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
    public StateChangeMove[] getModeMoves() {
        if((team == 1 && y == 7) || (team == 2 && y == 0)) {
            return new StateChangeMove[] {
                build(new Comparator(team)), build(new RedstoneTorch(team)), build(new Repeater(team)), build(new TargetBlock(team))
            };
        } else {
            return new StateChangeMove[0];
        }
    }

    private StateChangeMove build(Piece piece) {
        return new StateChangeMove(this, piece.getPacket());
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
                        hPower = Math.max(hPower, piece.hardPowerLevel());
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
        return "Block{" +
                "powerLevel=" + powerLevel +
                ", hardPower=" + hardPower +
                '}';
    }

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public Piece setDataFrom(Piece piece) {
        return super.setDataFrom(piece);
    }

    @Override
    public Piece copy() {
        Block block = new Block(team);
        block.setDataFrom(this);
        block.hardPower = hardPower;
        block.powerLevel = powerLevel;
        return block;
    }


}
