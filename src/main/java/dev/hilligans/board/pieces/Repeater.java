package dev.hilligans.board.pieces;

import dev.hilligans.board.Direction;
import dev.hilligans.board.OtherMove;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.BishopMovementController;
import dev.hilligans.board.movement.MovementController;

public class Repeater extends Piece {

    public int rotation = 0;
    boolean powered = false;
    int delay = 0;
    int delayTimeout;

    public Repeater(int team) {
        super(team, new BishopMovementController());
        rotation = team == 2 ? 2 : 0;
    }

    @Override
    public OtherMove[] getRotationMoves() {
        return new OtherMove[] {
                new OtherMove(this,build((powered ? 1 : 0) | 0 << 1 | delay << 3)),
                new OtherMove(this,build((powered ? 1 : 0) | 1 << 1 | delay << 3)),
                new OtherMove(this,build((powered ? 1 : 0) | 2 << 1 | delay << 3)),
                new OtherMove(this,build((powered ? 1 : 0) | 3 << 1 | delay << 3))};
    }

    @Override
    public OtherMove[] getModeMoves() {
        return new OtherMove[] {
                new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 0 << 3)),
                new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 1 << 3)),
                new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 2 << 3)),
                new OtherMove(this,build((powered ? 1 : 0) | rotation << 1 | 3 << 3))};
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

    private void updatePower() {
        Direction direction = Direction.directions[1 << rotation];
        Piece[] pieces = board.getSurroundingSpaces(x, y);
        boolean pow = false;
        for (int x = 0; x < 4; x++) {
            Piece piece = pieces[x];
            if (piece != null) {
                //if (direction.facesTowards(piece.x,piece.y,this.x,this.y)) {
                    if(piece.getPowerLevel() != 0) {
                        pow = true;
                    }
             //   }
            }
        }
        if(pow != powered) {
            powered = pow;
            for(int x = 0; x < 4; x++) {
                if(pieces[x] != null) {
                    pieces[x].tick();
                }
            }
        }
        powered = pow;
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
        updatePower();
    }

    @Override
    public int getExtraData() {
        return (powered ? 1 : 0) | rotation << 1 | delay << 3;
    }
}
