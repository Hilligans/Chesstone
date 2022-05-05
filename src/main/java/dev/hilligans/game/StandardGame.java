package dev.hilligans.game;

import dev.hilligans.board.Move;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StandardGame implements GameImplementation{
    @Override
    public void sendDataToPlayer(short[] board, Game game, GamePlayer player) {
        JSONObject jsonObject = new JSONObject();
        JSONArray data = new JSONArray();
        data.putAll(board);
        jsonObject.put("type", "game_state");
        jsonObject.put("game_state",data);
        jsonObject.put("turn",game.turn);
    }

    @Override
    public void sendMoveListToPlayer(Game game, GamePlayer player) {
        if(player.playerID != 0) {
            ArrayList<Move> moves = game.board.getAllValidMoves(player.playerID);
        }
    }
}
