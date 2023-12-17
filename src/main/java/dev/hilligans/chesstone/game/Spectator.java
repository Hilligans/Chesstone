package dev.hilligans.chesstone.game;

import dev.hilligans.spring.chesstone.ChesstoneWebSocket;
import dev.hilligans.spring.chesstone.packet.BoardPacket;
import org.springframework.web.socket.WebSocketSession;

public class Spectator implements IViewer {

    public IGame game;
    public WebSocketSession session;

    public Spectator(IGame game, WebSocketSession session) {
        this.game = game;
        this.session = session;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public int playerID() {
        return 0;
    }

    @Override
    public void endTurn(int nextTurn) {
        sendPacketToViewer(new BoardPacket(game.getBoard().getEncodedBoardList()).toEncodedPacket());
    }

    @Override
    public void sendPacketToViewer(String packet) {
        ChesstoneWebSocket.sendPacket(session, packet);
    }
}
