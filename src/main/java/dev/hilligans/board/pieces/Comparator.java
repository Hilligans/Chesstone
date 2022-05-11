package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.OtherMove;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;
import dev.hilligans.board.movement.QueenMovementController;

public class Comparator extends Piece {

    public int rotation = 0;
    int powerLevel;
    int delayTimeout = 0;
    public boolean subtract;
    public boolean tick;
    public int newState = 0;

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
    public Direction getFacingDirection() {
        return Direction.directions[1 << rotation];
    }


    @Override
    public int getID() {
        return 6;
    }

    @Override
    public OtherMove[] getRotationMoves() {
        return new OtherMove[] {
                rotation != 0 ? new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 0 << 1 | (subtract ? 1 : 0) << 3)) : null,
                rotation != 1 ? new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 1 << 1 | (subtract ? 1 : 0) << 3)) : null,
                rotation != 2 ? new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 2 << 1 | (subtract ? 1 : 0) << 3)) : null,
                rotation != 3 ? new OtherMove(this,build((powerLevel != 0 ? 1 : 0) | 3 << 1 | (subtract ? 1 : 0) << 3)) : null};
    }

    @Override
    public OtherMove[] getModeMoves() {
        if(subtract) {
            return new OtherMove[] {
                    new OtherMove(this, build((powerLevel != 0 ? 1 : 0) | rotation << 1 | (0) << 3))
                };
        } else {
            return new OtherMove[]{
                    new OtherMove(this, build((powerLevel != 0 ? 1 : 0) | rotation << 1 | (1) << 3))};
        }
    }



    @Override
    public void update() {
        Direction direction = Direction.directions[1 << rotation];
        int poweredLevel = 0;
        int sub = 0;
        Piece piece = board.getPieceOutside(this.x - direction.x, this.y - direction.y);
        if(piece != null) {
            poweredLevel = Math.max(poweredLevel, Math.max(piece.getPowerLevel(), piece.hardPowerLevel()));
        }
        piece = board.getPieceOutside(this.x - direction.y, this.y - direction.x);
        if(piece != null && piece.getFacingDirection().facesTowards(piece.x,piece.y,this.x,this.y) || piece instanceof RedstoneBlock) {
            sub = Math.max(sub,Math.max(piece.getPowerLevel(),piece.hardPowerLevel()));
        }
        piece = board.getPieceOutside(this.x + direction.y, this.y + direction.x);
        if(piece != null && piece.getFacingDirection().facesTowards(piece.x,piece.y,this.x,this.y) || piece instanceof RedstoneBlock) {
            sub = Math.max(sub,Math.max(piece.getPowerLevel(),piece.hardPowerLevel()));
        }
        if(subtract) {
            poweredLevel = Math.max(0, poweredLevel - sub);
        } else {
            if(sub > poweredLevel) {
                poweredLevel = 0;
            }
        }

        if(poweredLevel != this.powerLevel) {
            newState = poweredLevel;
            tick = true;
        }
    }

    @Override
    public int getPowerLevel() {
        return powerLevel;
    }

    @Override
    public int hardPowerLevel() {
        return powerLevel;
    }

    @Override
    public void tick() {
        if(tick) {
            if (++delayTimeout >= 1) {
                delayTimeout = 0;
                tick = false;
                powerLevel = newState;
                updateRedstone();
            }
        }
    }

    public void updateRedstone() {
        Piece[] pieces = board.getSurroundingSpaces(x, y);

        for (int x = 0; x < 4; x++) {
            if (pieces[x] != null) {
                pieces[x].update();
            }
        }
    }

    @Override
    public void onPlace() {
        update();
        delayTimeout = 0;
        tick = false;
        powerLevel = newState;
        updateRedstone();
    }

    private int build(int data) {
        return (short) ((team == 2 ? 1 : 0) | getID() << 1 | data << 4);
    }
    @Override
    public int getExtraData() {
        return (powerLevel != 0 ? 1 : 0) | rotation << 1 | (subtract ? 1 : 0) << 3;
    }

    @Override
    public void decodeData(int data) {
        rotation = (data >> 1) &  0b11;
        subtract = ((data >> 3) & 1) == 1;
    }

    @Override
    public String toString() {
        return "Comparator{" +
                "rotation=" + rotation +
                ", powerLevel=" + powerLevel +
                ", delayTimeout=" + delayTimeout +
                ", subtract=" + subtract +
                ", tick=" + tick +
                ", newState=" + newState +
                '}';
    }
}
