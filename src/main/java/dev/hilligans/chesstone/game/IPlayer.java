package dev.hilligans.chesstone.game;

public interface IPlayer extends IViewer {

    IGame getGame();

    boolean isAlive();

    void resign(String reason);

    int getRemainingTime();

    String getName();

}
