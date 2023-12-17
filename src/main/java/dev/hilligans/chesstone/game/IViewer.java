package dev.hilligans.chesstone.game;

public interface IViewer {

    IGame getGame();

    int playerID();

    void endTurn(int nextTurn);

    void sendPacketToViewer(String packet);
}
