package dev.hilligans.chesstone.ai;

import dev.hilligans.chesstone.board.Board;
import dev.hilligans.chesstone.board.IMove;

public abstract class MoveCalculator {

    public abstract IMove findMove(Board board, int player);

}
