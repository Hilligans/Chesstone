package dev.hilligans.board.movement;

import dev.hilligans.board.Move;
import dev.hilligans.board.Piece;
import dev.hilligans.board.bounds.IBoardBounds;
import dev.hilligans.board.bounds.StandardBoardBounds;
import dev.hilligans.game.Game;

import java.util.ArrayList;

public class MovementController {

    public Piece piece;
    public IBoardBounds bounds = new StandardBoardBounds();

    public void getMoveList(ArrayList<Move> moves) {}

    public void performMove(int startX, int startY, int endX, int endY, Piece endPiece) {}

}
