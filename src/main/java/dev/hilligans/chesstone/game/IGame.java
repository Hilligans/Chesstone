package dev.hilligans.chesstone.game;

import dev.hilligans.chesstone.board.IBoard;
import dev.hilligans.chesstone.board.IMove;
import dev.hilligans.chesstone.storage.IGameResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

public interface IGame {

    IGameResult getGameResult();

    IViewer addPlayer(Account account, WebSocketSession session);

    void addMove(IMove move);

    ArrayList<IMove> getMovesList();

    IPlayer[] getPlayers();

    IPlayer getCurrentPlayersTurn();

    boolean isStarted();

    String getGameCode();

    IBoard getBoard();

    float getIncrement();

    int getTurn();

    void startGame();

    void endGame(int winner, String reason);

    void swapTurns();

    void sendPacketToAll(String packet);

    boolean isRunning();

    boolean isPublic();

    void setPublic(boolean isPublic);

    String getGameName();

    void setName(String name);

    @Nullable
    IPlayer getPlayer(WebSocketSession session);


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
