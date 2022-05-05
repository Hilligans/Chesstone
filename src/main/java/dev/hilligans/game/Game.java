package dev.hilligans.game;

import dev.hilligans.board.Board;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;

public class Game {

    public Board board;
    public GameImplementation gameImplementation;
    public GamePlayer player1;
    public GamePlayer player2;
    public ArrayList<GamePlayer> spectators = new ArrayList<>();



    public boolean started;
    public boolean gamePublic;
    public int turn = 1;
    public String gameCode;

    public Game(Board board) {
        gameCode = RandomStringUtils.random(6);
        this.board = board;
    }

    public void setPlayer1(GamePlayer player1) {
        this.player1 = player1;
    }

    public void setPlayer2(GamePlayer player2) {
        this.player2 = player2;
    }

    public void sendBoard(short[] board) {

        gameImplementation.sendDataToPlayer(board,this,player1);
        gameImplementation.sendDataToPlayer(board,this,player2);
        for(GamePlayer spectator : spectators) {
            gameImplementation.sendDataToPlayer(board,this,spectator);
        }
    }

    public void update() {
        ArrayList<GamePlayer> removeList = new ArrayList<>();
        for(GamePlayer spectator : spectators) {
            if(!spectator.player.alive) {
                removeList.add(spectator);
            }
        }
        spectators.removeAll(removeList);
    }

    public void sendMoveList() {
        if(turn == 1) {
            gameImplementation.sendMoveListToPlayer(this,player1);
        } else {
            gameImplementation.sendMoveListToPlayer(this,player2);
        }
    }


    public void finishGame() {
        if(player1.player.callback != null) {
            player1.player.callback.run();
        }
        if(player2.player.callback != null) {
            player2.player.callback.run();
        }
    }
}
