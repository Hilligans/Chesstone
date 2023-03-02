package dev.hilligans.spring.packet;

import org.json.JSONObject;

public class GameStartPacket implements IPacket {


    @Override
    public String toEncodedPacket() {
        JSONObject game_start = new JSONObject();
        game_start.put("type", "game_start");
        return game_start.toString();
    }
}
