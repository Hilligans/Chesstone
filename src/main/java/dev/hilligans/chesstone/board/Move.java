package dev.hilligans.chesstone.board;

import dev.hilligans.chesstone.board.pieces.Block;
import dev.hilligans.chesstone.game.IGame;
import dev.hilligans.chesstone.game.MoveResult;
import dev.hilligans.spring.chesstone.packet.RequiredMovePacket;

import java.util.ArrayList;
import java.util.Objects;

public class Move implements IMove {

    public int startX, startY;
    public int endX, endY;

    public int team;

    public Move(Piece piece, int endX, int endY) {
        startX = piece.x;
        startY = piece.y;
        this.endX = endX;
        this.endY = endY;
        this.team = piece.team;
    }

    public Move(int startX, int startY, int endX, int endY, int team) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return startX == move.startX && startY == move.startY && endX == move.endX && endY == move.endY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startX, startY, endX, endY);
    }

    @Override
    public String toString() {
        return "Move{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", team=" + team +
                '}';
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getPos() {
        return IMove.toPos(startX,startY);
    }

    @Override
    public int getData() {
        return IMove.toPos(endX,endY);
    }

    @Override
    public int getTeam() {
        return team;
    }

    @Override
    public MoveResult makeMove(IGame game) {
        Piece piece = game.getBoard().getPiece(startX,startY);
        ArrayList<Move> moves = new ArrayList<>();
        if(piece == null) {
            return new MoveResult(false);
        }
        piece.getMoveList(moves);
        for(Move newMove : moves) {
            if(this.equals(newMove)) {
                applyMove(game.getBoard());
                piece.onPlace();
                Piece[] pieces = piece.getSurroundingPieces();
                for(Piece piece1 : pieces) {
                    if(piece1 != null) {
                        piece1.update();
                    }
                }
                if(piece instanceof Block) {
                    if((piece.team == 1 && endY == 0) || (piece.team == 2 && endY == 0)) {
                        RequiredMovePacket movesPacket = new RequiredMovePacket("mode_changes", piece.getModeMoves());
                        return new MoveResult(new String[]{movesPacket.toEncodedPacket()});
                    }
                }
                return new MoveResult(true);
            }
        }
        return new MoveResult(false);
    }



    public void applyMove(IBoard board) {
        board.setLastMove(this);
        Piece piece = board.getPiece(this.startX,this.startY);
        Piece endPiece = board.getPiece(this.endX, this.endY);
        int startX = piece.x;
        int startY = piece.y;
        board.setPiece(this.endX,this.endY,piece);
        board.setPiece(startX,startY,null);
        piece.movementController.performMove(piece, startX,startY,this.endX,this.endY, endPiece);
    }
}
