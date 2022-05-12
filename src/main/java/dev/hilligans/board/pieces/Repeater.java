package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.OtherMove;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.BishopMovementController;
import dev.hilligans.board.movement.MovementController;

public class Repeater extends Piece {
    
    public int rotation = 0;
    boolean powered = false;
    public int delay = 0;
    public int delayTimeout;
    boolean tick = false;
    public boolean newState;

    public boolean locked;

    public Repeater(int team) {
        super(team, new BishopMovementController());
        rotation = team == 2 ? 2 : 0;
    }

    @Override
    public OtherMove[] getRotationMoves() {
        return new OtherMove[] {
                rotation != 0 ? new OtherMove(this,build((powered ? 1 : 0) | 0 << 1 | delay << 3)) : null,
                rotation != 1 ? new OtherMove(this,build((powered ? 1 : 0) | 1 << 1 | delay << 3)) : null,
                rotation != 2 ? new OtherMove(this,build((powered ? 1 : 0) | 2 << 1 | delay << 3)) : null,
                rotation != 3 ? new OtherMove(this,build((powered ? 1 : 0) | 3 << 1 | delay << 3)) : null};
    }

    @Override
    public OtherMove[] getModeMoves() {
        return new OtherMove[] {
                delay != 0 ? new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 0 << 3)) : null,
                delay != 1 ? new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 1 << 3)) : null,
                delay != 2 ? new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 2 << 3)) : null,
                delay != 3 ? new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 3 << 3)) : null};
    }

    private int build(int data) {
        return (short) ((team == 2 ? 1 : 0) | getID() << 1 | data << 4);
    }

    @Override
    public boolean hardPowers() {
        return true;
    }

    @Override
    public int getID() {
        return 5;
    }

    @Override
    public Direction getFacingDirection() {
        return Direction.directions[1 << rotation];
    }

    @Override
    public Direction getPullingDirection() {
        return Direction.directions[1 << rotation].getJoinedInverse();
    }

    @Override
    public void update() {
        Direction direction = Direction.directions[1 << rotation];
        boolean pow = false;
        Piece piece = board.getPieceOutside(this.x - direction.x, this.y - direction.y);
        if(piece != null) {
            if(piece.getPowerLevel() != 0 || piece.hardPowerLevel() != 0) {
                pow = true;
            }
        }
        locked = false;
        piece = board.getPieceOutside(this.x - direction.y, this.y - direction.x);
        if(piece != null && piece.getFacingDirection().facesTowards(piece.x,piece.y,this.x,this.y) && (piece instanceof Comparator || piece instanceof Repeater)) {
            if(piece.getPowerLevel() != 0) {
                locked = true;
            }
        }
        piece = board.getPieceOutside(this.x + direction.y, this.y + direction.x);
        if(piece != null && piece.getFacingDirection().facesTowards(piece.x,piece.y,this.x,this.y) && (piece instanceof Comparator || piece instanceof Repeater)) {
            if(piece.getPowerLevel() != 0) {
                locked = true;
            }
        }

        if(pow != powered) {
            newState = pow;
            tick = true;
        }
    }

    @Override
    public int getPowerLevel() {
        return powered ? 15 : 0;
    }

    @Override
    public int hardPowerLevel() {
        return powered ? 15 : 0;
    }

    @Override
    public void tick() {
        if(tick) {
            if (delayTimeout++ >= delay + 1) {
                delayTimeout = 0;
                tick = false;
                updateRedstone(newState);
            }
        }
    }

    public void updateRedstone(boolean powered) {
        Direction direction = Direction.directions[1 << rotation];
        boolean pow = false;
        Piece piece = board.getPieceOutside(this.x - direction.x, this.y - direction.y);
        if(piece != null) {
            if(piece.getPowerLevel() != 0 || piece.hardPowerLevel() != 0) {
                pow = true;
            }
        }
        this.powered = powered || pow;
        Piece[] pieces = board.getSurroundingSpaces(x, y);
        for (int x = 0; x < 4; x++) {
            if (pieces[x] != null) {
                pieces[x].update();
            }
        }

    }

    @Override
    public void onPlace() {
        delayTimeout = 0;
        tick = false;
        updateRedstone(false);
    }

    @Override
    public int getExtraData() {
        return (powered ? 1 : 0) | (rotation) << 1 | delay << 3 | (locked ? 1 : 0) << 5;
    }

    @Override
    public void decodeData(int data) {
        rotation = (data >> 1) & 0b11;
        delay = (data >> 3) & 0b11;
    }

    @Override
    public String toString() {
        return "Repeater{" +
                "rotation=" + rotation +
                ", powered=" + powered +
                ", delay=" + delay +
                ", delayTimeout=" + delayTimeout +
                ", tick=" + tick +
                ", newState=" + newState +
                ", locked=" + locked +
                '}';
    }
}
