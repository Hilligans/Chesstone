package dev.hilligans.ai;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;

public abstract class MoveCalculator {

    public abstract Move findMove(Board board, int player);

}
