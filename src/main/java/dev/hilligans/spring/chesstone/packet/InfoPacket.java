package dev.hilligans.spring.chesstone.packet;

import dev.hilligans.chesstone.game.IGame;
import dev.hilligans.chesstone.game.IPlayer;
import dev.hilligans.chesstone.game.IViewer;
import dev.hilligans.chesstone.game.Player;
import org.json.JSONArray;
import org.json.JSONObject;

public class InfoPacket implements IPacket {

    public IGame game;
    public IViewer viewer;

    public InfoPacket(IGame game, IViewer viewer) {
        this.game = game;
        this.viewer = viewer;
    }



    @Override
    public String toEncodedPacket() {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        jsonObject.put("data", data);
        jsonObject.put("type", "info");

        JSONArray playerNames = new JSONArray(2);
        JSONArray playerIDs = new JSONArray(2);
        jsonObject.put("names", playerNames);
        jsonObject.put("ids", playerIDs);

        int x = 0;
        for(IPlayer player : game.getPlayers()) {
            if(player != null) {
                if(player instanceof Player pl) {
                    playerNames.put(x, pl.account.username);
                    playerIDs.put(x, pl.account.userID);
                } else {
                    //TODO fix
                    playerNames.put(x, "Bot");
                    playerNames.put(x, 1234567890);
                }
            } else {
                playerNames.put(x, "");
                playerNames.put(x, -1);
            }
            x++;
        }

        jsonObject.put("in_progress", game.isStarted());
        jsonObject.put("player", viewer.playerID() - 1);

        return jsonObject.toString();
    }
}
