package dev.hilligans.chesstone.board;

import dev.hilligans.chesstone.game.MoveResult;
import dev.hilligans.chesstone.game.IGame;

public interface IMove {

    int getType();
    int getPos();
    int getData();
    int getTeam();

    MoveResult makeMove(IGame game);

    void applyMove(IBoard board);

    static int toPos(int x, int y) {
        return y * 8 + x;
    }

}
