package dev.hilligans.board;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface IBoard {

    void setPiece(int x, int y, Piece piece);

    @Nullable
    Piece getPiece(int x, int y);

    ArrayList<Move> getAllValidMoves(int player);

    short[] getEncodedBoardList();

    boolean[] getAlivePlayers();

    IMove getLastMove();

    void setLastMove(IMove move);

    void update();

    void tick();
}

