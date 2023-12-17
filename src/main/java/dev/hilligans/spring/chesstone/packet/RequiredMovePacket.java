package dev.hilligans.spring.chesstone.packet;

import dev.hilligans.chesstone.board.StateChangeMove;
import org.json.JSONArray;
import org.json.JSONObject;

public class RequiredMovePacket implements IPacket {

    public String name;
    public StateChangeMove[] moves;

    public RequiredMovePacket(String name, StateChangeMove[] moves) {
        this.name = name;
        this.moves = moves;
    }

    @Override
    public String toEncodedPacket() {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        jsonObject.put("type", "moves");
        jsonObject.put("data", data);
        JSONArray arr = new JSONArray();
        for (StateChangeMove move : moves) {
            if (move != null) {
                arr.put(move.newID);
            }
        }
        data.put(name, arr);
        return jsonObject.toString();
    }
}
