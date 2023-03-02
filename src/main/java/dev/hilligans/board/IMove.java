package dev.hilligans.board;

import dev.hilligans.game.Game;
import dev.hilligans.game.IGame;

public interface IMove {

    int getType();
    int getPos();
    int getData();

    boolean makeMove(IGame game);

    void applyMove(IBoard board);

    static int toPos(int x, int y) {
        return y * 8 + x;
    }

}
