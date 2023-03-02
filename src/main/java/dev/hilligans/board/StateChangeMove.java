package dev.hilligans.board;

import dev.hilligans.game.Game;
import dev.hilligans.game.IGame;

import java.util.Objects;

public class StateChangeMove implements IMove {

    public int newID;
    public int type;

    public int x;
    public int y;

    public StateChangeMove(Piece piece, int newID) {
        this.x = piece.x;
        this.y = piece.y;
        this.newID = newID;
    }

    public StateChangeMove(int x, int y, int newID, int type) {
        this.x = x;
        this.y = y;
        this.newID = newID;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateChangeMove move = (StateChangeMove) o;
        return newID == move.newID && x == move.x && y == move.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(newID, x, y);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getPos() {
        return IMove.toPos(x,y);
    }

    @Override
    public int getData() {
        return newID;
    }

    @Override
    public boolean makeMove(IGame game) {
        Piece piece = game.getBoard().getPiece(x, y);
        if(piece == null) {
            return false;
        }
        StateChangeMove[] moves;
        if(type == 1) {
            moves = piece.getRotationMoves();
        } else {
            moves = piece.getModeMoves();
        }
        for(StateChangeMove newMove : moves) {
            if(this.equals(newMove)) {
                applyMove(game.getBoard());
                piece.onPlace();
                Piece[] pieces = piece.getSurroundingPieces();
                for(Piece piece1 : pieces) {
                    if(piece1 != null) {
                        piece1.update();
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void applyMove(IBoard board) {
        board.setLastMove(this);
        Piece piece = board.getPiece(this.x,this.y);
        piece.decodeData(this.newID >> 4);
    }

    @Override
    public String toString() {
        return "StateChangeMove{" +
                "newID=" + newID +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
