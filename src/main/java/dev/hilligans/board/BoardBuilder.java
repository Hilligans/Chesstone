package dev.hilligans.board;

import dev.hilligans.board.movement.*;
import dev.hilligans.board.pieces.*;

public class BoardBuilder {

    public static final BishopMovementController STANDARD_BISHOP_CONTROLLER = new BishopMovementController();
    public static final KnightMovementController STANDARD_KNIGHT_CONTROLLER = new KnightMovementController();
    public static final PawnMovementController STANDARD_PAWN_CONTROLLER = new PawnMovementController();
    public static final QueenMovementController STANDARD_QUEEN_CONTROLLER = new QueenMovementController();
    public static final RookMovementController STANDARD_ROOK_CONTROLLER = new RookMovementController();
    public static final MovementController EMPTY_MOVEMENT_CONTROLLER = new MovementController();




    public static Board buildStandardBoard() {
        Board board = new Board();
        for(int x = 0; x < 8; x++) {
            board.setPiece(x,1,new Block(1));
            board.setPiece(x,6,new Block(2));
        }
        board.setPiece(0,0,new RedstoneTorch(1));
        board.setPiece(1,0,new TargetBlock(1));
        board.setPiece(2,0,new Repeater(1));
        board.setPiece(3,0,new Comparator(1));
        Lamp king1 = new Lamp(1);
        board.setPiece(4,0,king1);
        board.setPiece(5,0,new Repeater(1));
        board.setPiece(6,0,new TargetBlock(1));
        board.setPiece(7,0,new RedstoneTorch(1));

        board.setPiece(0,7,new RedstoneTorch(2));
        board.setPiece(1,7,new TargetBlock(2));
        board.setPiece(2,7,new Repeater(2));
        board.setPiece(3,7,new Comparator(2));
        Lamp king2 = new Lamp(2);
        board.setPiece(4,7,king2);
        board.setPiece(5,7,new Repeater(2));
        board.setPiece(6,7,new TargetBlock(2));
        board.setPiece(7,7,new RedstoneTorch(2));

        board.yellowKing = king2;
        board.blueKing = king1;

        return board;
    }



}
