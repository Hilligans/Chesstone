package dev.hilligans.chesstone.game;

public class MoveResult {

    boolean isValid;
    public String[] packetsToSend;

    public MoveResult(boolean isValid) {
        this.isValid = isValid;
    }

    public MoveResult(String[] packetsToSend) {
        this.isValid = true;
        this.packetsToSend = packetsToSend;
    }

    public boolean isValid() {
        return isValid;
    }

    public String[] packetsToSend() {
        return packetsToSend;
    }
}
