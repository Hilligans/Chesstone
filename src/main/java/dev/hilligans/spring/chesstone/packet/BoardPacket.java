package dev.hilligans.spring.chesstone.packet;

import org.json.JSONArray;
import org.json.JSONObject;

public class BoardPacket implements IPacket {

    public short[] board;

    public BoardPacket(short[] board) {
        this.board = board;
    }

    @Override
    public String toEncodedPacket() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "board");
        JSONArray data = new JSONArray();
        data.putAll(board);
        jsonObject.put("board", data);
        return jsonObject.toString();
    }
}
