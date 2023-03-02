package dev.hilligans.spring.packet;

import org.json.JSONObject;

public class MoveAckPacket implements IPacket {

    public boolean validMove;

    public MoveAckPacket(boolean validMove) {
        this.validMove = validMove;
    }
    @Override
    public String toEncodedPacket() {
        JSONObject response = new JSONObject();
        response.put("type", "move_ack");
        response.put("data", validMove);
        return response.toString();
    }
}
