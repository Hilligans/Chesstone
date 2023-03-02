package dev.hilligans.spring.packet;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClockPacket implements IPacket {

    int player1;
    int player2;

    public ClockPacket(int player1, int player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public String toEncodedPacket() {
        JSONObject packet = new JSONObject();
        packet.put("type", "clock");
        JSONArray array = new JSONArray(2).put(0, player1).put(1, player2);
        packet.put("data", array);
        return packet.toString();
    }

}
