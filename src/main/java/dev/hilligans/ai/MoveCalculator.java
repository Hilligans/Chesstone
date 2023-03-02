package dev.hilligans.ai;

import dev.hilligans.board.Board;
import dev.hilligans.board.IMove;
import dev.hilligans.board.Move;

public abstract class MoveCalculator {

    public abstract IMove findMove(Board board, int player);

}
