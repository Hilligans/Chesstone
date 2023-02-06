package dev.hilligans.board;

public interface IMove {

    int getType();
    int getPos();
    int getData();

    static int toPos(int x, int y) {
        return y * 8 + x;
    }

}
