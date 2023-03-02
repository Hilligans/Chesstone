package dev.hilligans.ai;


import dev.hilligans.board.IMove;
import dev.hilligans.board.Move;
import dev.hilligans.storage.DataBaseMoveContainer;
import dev.hilligans.storage.Database;

public class MoveBook {


    public static void setupOpenings() {
        Database database = Database.getInstance();
        database.clearOpenings();

        Builder builder = new Builder(new DataBaseMoveContainer());

        builder.withMoveChild(new Move(0, 1, 0, 3))
                .withMoveChild(new Move(6, 7, 5, 5))
                    .withMoveChild(new Move(0, 0, 2, 0))
                        .withMoveChild(new Move(5, 5, 3, 4)).build();

        DataBaseMoveContainer container = new DataBaseMoveContainer();
        DataBaseMoveContainer moves = new DataBaseMoveContainer();
        moves.addMove(new Move(0, 1, 0, 3));

        DataBaseMoveContainer newMoves = new DataBaseMoveContainer();
        newMoves.addMove(new Move(6, 7, 5, 5));
       // newMoves.addMove()



        moves.addMove(new Move(7, 1, 7, 3));
        moves.addMove(new Move(4, 1, 4, 2));





        database.addOpenings(container, moves);
    }


    static class Builder {

        DataBaseMoveContainer sequence;
        DataBaseMoveContainer moves = new DataBaseMoveContainer();

        public Builder(DataBaseMoveContainer sequence) {
            this.sequence = sequence;
        }

        Builder withMove(IMove move) {
            moves.addMove(move);
            return this;
        }

        Builder withMoveChild(IMove move) {
            moves.addMove(move);
            DataBaseMoveContainer newContainer = new DataBaseMoveContainer();
            newContainer.moves.addAll(sequence.moves);
            newContainer.addMove(move);
            withMove(move);
            return new Builder(newContainer);
        }

        Builder build() {
            Database.getInstance().addOpenings(sequence, moves);
            return this;
        }
    }
}
