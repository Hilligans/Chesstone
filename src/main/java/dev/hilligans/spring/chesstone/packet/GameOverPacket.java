package dev.hilligans.spring.chesstone.packet;

import org.json.JSONObject;

public class GameOverPacket implements IPacket {

    public int winner;
    public String reason;

    public GameOverPacket(int winner, String reason) {
        this.winner = winner - 1;
        this.reason = reason;
    }

    @Override
    public String toEncodedPacket() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "game_over");
        JSONObject data = new JSONObject();
        data.put("winner", winner);
        data.put("reason", reason);
        jsonObject.put("data", data);

        return jsonObject.toString();
    }
}
