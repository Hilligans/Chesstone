package dev.hilligans.board;

import dev.hilligans.game.Game;

public interface IMove {

    int getType();
    int getPos();
    int getData();

    boolean makeMove(Game game);

    void applyMove(Board board);

    static int toPos(int x, int y) {
        return y * 8 + x;
    }

}
