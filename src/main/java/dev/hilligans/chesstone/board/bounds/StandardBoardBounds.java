package dev.hilligans.chesstone.board.bounds;

public class StandardBoardBounds implements IBoardBounds {

    @Override
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    @Override
    public int getSize() {
        return 8;
    }
}
