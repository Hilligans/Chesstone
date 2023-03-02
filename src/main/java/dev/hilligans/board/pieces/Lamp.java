package dev.hilligans.board.pieces;

import dev.hilligans.Main;
import dev.hilligans.board.BoardBuilder;
import dev.hilligans.board.Piece;
import dev.hilligans.board.movement.MovementController;

public class Lamp extends Piece {

    public boolean extended = false;

    public Lamp(int team) {
        this(team, BoardBuilder.EMPTY_MOVEMENT_CONTROLLER);
    }

    public Lamp(int team, MovementController movementController) {
        super(team, movementController);
    }

    @Override
    public void update() {
        extended = false;
        Piece[] pieces = board.getSurroundingSpaces(x,y);
        for(Piece piece : pieces) {
            if(piece != null) {
                if (piece.getFacingDirection().facesTowards(this.x, this.y, piece.x, piece.y)) {
                    if (piece.getPowerLevel() != 0) {
                        extended = true;
                    }
                } else if(piece instanceof Block) {
                    if(piece.getPowerLevel() != 0 || piece.hardPowerLevel() != 0) {
                        extended = true;
                    }
                }
            }
        }
        if(extended) {
            if(Main.gameHandler.logUpdates) {
                System.out.println("(" + x + "," + y + ") " + this);
            }
        }
    }

    @Override
    public int getExtraData() {
        return extended ? 1 : 0;
    }

    @Override
    public Piece copy() {
        Lamp lamp = new Lamp(team);
        lamp.setDataFrom(this);
        lamp.extended = extended;
        return lamp;
    }

    @Override
    public int getID() {
        return 7;
    }

    @Override
    public String toString() {
        return "Lamp{" +
                "extended=" + extended +
                '}';
    }
}
