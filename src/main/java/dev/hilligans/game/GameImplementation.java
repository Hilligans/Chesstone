package dev.hilligans.game;

public interface GameImplementation {

    void sendDataToPlayer(short[] board, Game game, GamePlayer player);

    void sendMoveListToPlayer(Game game, GamePlayer player);

}
