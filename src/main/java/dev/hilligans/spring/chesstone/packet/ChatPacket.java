package dev.hilligans.spring.chesstone.packet;

import dev.hilligans.chesstone.game.IPlayer;
import org.json.JSONObject;

public class ChatPacket implements IPacket {

    public int author;
    public String name;
    public String message;

    public ChatPacket(IPlayer player, String message) {
        this.author = player.playerID() - 1;
        this.message = message;
    }

    public ChatPacket(int author, String name, String message) {
        this.author = author - 1;
        this.name = name;
        this.message = message;
    }

    @Override
    public String toEncodedPacket() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "chat");
        jsonObject.put("author", author);
        if(name != null) {
            jsonObject.put("name", name);
        }
        jsonObject.put("message", message);
        return jsonObject.toString();
    }
}
