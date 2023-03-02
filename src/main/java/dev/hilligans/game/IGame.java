package dev.hilligans.game;

import dev.hilligans.board.Board;
import dev.hilligans.board.IBoard;
import dev.hilligans.storage.IGameResult;
import org.springframework.web.socket.WebSocketSession;

public interface IGame {

    IGameResult getGameResult();

    int addPlayer(GamePlayer gamePlayer);

    IViewer addPlayer(Account account, WebSocketSession session);

    GamePlayer[] getGamePlayers();

    IPlayer[] getPlayers();

    IPlayer getCurrentPlayersTurn();

    boolean isStarted();

    String getGameCode();

    IBoard getBoard();

    float getIncrement();

    int getTurn();

    void startGame();

    void swapTurns();

    void sendPacketToAll(String packet);

    //void endGame(int winner, String reason);

    static int getWinner(boolean[] alivePlayers) {
        int count = 0;
        int index = 0;
        for(int x = 0; x < alivePlayers.length; x++) {
            if(!alivePlayers[x]) {
                count++;
                index = x + 1;
            }
        }
        if(count == 1) {
            return index;
        } else if(count == 0) {
            return 0;
        }
        return -1;
    }
}
