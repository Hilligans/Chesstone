package dev.hilligans.board;

import dev.hilligans.board.pieces.Lamp;
import dev.hilligans.board.pieces.RedstoneBlock;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Board implements IBoard {

    public Piece[][] board = new Piece[8][8];
    //-1 game active,  0 draw, 1 player 1 win, 2 player 2 win
    public int gameState = -1;

    public Lamp yellowKing;
    public Lamp blueKing;
    public IMove lastMove;

    @Override
    public void setPiece(int x, int y, Piece piece) {
        board[x][y] = piece;
        if(piece != null) {
            piece.board = this;
            piece.setPos(x, y);
        }
    }

    @Nullable
    @Override
    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    @Override
    public IMove getLastMove() {
        return lastMove;
    }

    @Override
    public void setLastMove(IMove move) {
        this.lastMove = move;
    }

    @Nullable
    public Piece getPieceOutside(int x, int y) {
        if(y != 0 & y != 7) {
            if(x == -1) {
                return redstoneBlock;
            }
            if(x == 8) {
                return redstoneBlock;
            }
        }
        if(validSquare(x,y)) {
            return getPiece(x,y);
        }
        return null;
    }

    public boolean validSquare(int x, int y) {
        return 7 >= x && x >= 0 && 7 >= y && y >= 0;
    }

    static Piece redstoneBlock = new RedstoneBlock();
    public Piece[] getSurroundingSpaces(int x, int y) {
        Piece[] pieces = new Piece[4];
        if(y != 0 & y != 7) {
            if(x == 0) {
                pieces[3] = redstoneBlock;
            }
            if(x == 7) {
                pieces[1] = redstoneBlock;
            }
        }
        if (validSquare(x + 1, y)) {
            pieces[1] = getPiece(x + 1,y);
        }
        if (validSquare(x - 1, y)) {
            pieces[3] = getPiece(x - 1,y);
        }
        if (validSquare(x, y + 1)) {
            pieces[0] = getPiece(x,y + 1);
        }
        if (validSquare(x, y - 1)) {
            pieces[2] = getPiece(x, y - 1);
        }
        return pieces;
    }

    public int getGameState() {
        return gameState;
    }

    public void tick() {
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Piece piece = getPiece(x,y);
                if(piece != null) {
                    piece.tick();
                }
            }
        }
/*
        if(gameState == -1) {
            if (yellowKing.extended && blueKing.extended) {
                gameState = 0;
            } else if (yellowKing.extended) {
                gameState = 2;
            } else if (blueKing.extended) {
                gameState = 1;
            }
        }

 */
    }

    public int gameWin() {
        if (yellowKing.extended && blueKing.extended) {
            return 0;
        } else if (yellowKing.extended) {
            return 2;
        } else if (blueKing.extended) {
            return 1;
        }
        return -1;
    }

    public void update() {
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Piece piece = getPiece(x,y);
                if(piece != null) {
                    piece.update();
                }
            }
        }
    }

    public ArrayList<Move> getAllValidMoves(int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Piece piece = getPiece(x,y);
                if(piece != null) {
                    if(piece.team == player) {
                        piece.getMoveList(moves);
                    }
                }
            }
        }
        return moves;
    }

    public int getPieces(int player) {
        int pieces = 0;
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Piece piece = getPiece(x,y);
                if(piece != null) {
                    if(piece.team == player) {
                        pieces++;
                    }
                }
            }
        }
        return pieces;
    }

    public Board duplicate() {
        Board newBoard = new Board();
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Piece piece = getPiece(x,y);
                if(piece != null) {
                    newBoard.setPiece(x,y,piece.copy());
                }
            }
        }
        newBoard.yellowKing = (Lamp) newBoard.getPiece(yellowKing.x, yellowKing.y);
        newBoard.blueKing = (Lamp) newBoard.getPiece(blueKing.x, blueKing.y);
        return newBoard;
    }

    public short[] getEncodedBoardList() {
        short[] list = new short[64];
        for(int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = getPiece(x,y);
                list[y * 8 + x] = piece != null ? piece.getPacket() : 0;
            }
        }
        return list;
    }

    @Override
    public boolean[] getAlivePlayers() {
        return new boolean[] {!yellowKing.extended, !blueKing.extended};
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----------------").append("\n");
        for(int y = 7; y >= 0; y--) {
            stringBuilder.append("|");
            for(int x = 0; x < 8; x++) {
                String c = board[x][y] == null ? " " : getCode(board[x][y].getID());
                stringBuilder.append(c).append("|");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("----------------").append("\n");
        return stringBuilder.toString();
    }

    private static final String getCode(int a) {
        return switch (a) {
            case 0 -> " ";
            case 1 -> "R";
            case 2 -> "B";
            case 3 -> "I";
            case 4 -> "T";
            case 5 -> "P";
            case 6 -> "C";
            case 7 -> "L";
            default -> " ";
        };
    }
}