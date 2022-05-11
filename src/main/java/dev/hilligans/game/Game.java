package dev.hilligans.game;

import dev.hilligans.board.Board;
import dev.hilligans.board.Move;
import dev.hilligans.board.OtherMove;
import dev.hilligans.board.Piece;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.ArrayList;

public class Game {

    public Board board;
    public GameImplementation gameImplementation = new StandardGame();
    public GamePlayer player1;
    public GamePlayer player2;
    public ArrayList<GamePlayer> spectators = new ArrayList<>();

    public boolean gameRunning = false;

    public boolean started;
    public boolean gamePublic = true;
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

    public void sendPacketToPlayers(JSONObject jsonObject) {
        try {
            player1.sendPacket(jsonObject);
            player2.sendPacket(jsonObject);
            for(GamePlayer gamePlayer : spectators) {
                gamePlayer.sendPacket(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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

    public boolean makeMove(Move move) {
        Piece piece = board.getPiece(move.startX,move.startY);
        ArrayList<Move> moves = new ArrayList<>();
        if(piece == null) {
            return false;
        }
        piece.getMoveList(moves);
        for(Move newMove : moves) {
            if(newMove.equals(move)) {
                board.applyMove(move);
                piece.onPlace();
                board.update();
                for(int x = 0; x < 8; x++) {
                    board.tick();
                }
                return true;
            }
        }

        return false;
    }

    public boolean makeMove(OtherMove move) {
        Piece piece = board.getPiece(move.x,move.y);
        if(piece == null) {
            return false;
        }
        OtherMove[] moves;
        if(move.type == 1) {
            moves = piece.getRotationMoves();
        } else {
            moves = piece.getModeMoves();
        }
        for(OtherMove newMove : moves) {
            if(move.equals(newMove)) {
                board.applyMove(move);
                piece.onPlace();
                handlerAfterMove();
                return true;
            }
        }
        return false;
    }

    public void handlerAfterMove() {
        board.update();
        for(int x = 0; x < 8; x++) {
            board.tick();
            int state = board.gameWin();
            if(state == 1 || state == 2) {
                endGame(state, "Player " + (state == 1 ? 2 : 1) + " has checkmated Player " + state);
                return;
            }
        }
    }

    public void swapTurn() {
        turn = turn == 1 ? 2 : 1;
    }

    public boolean addSpectator(GamePlayer gamePlayer) {
        if(gameRunning) {
            if (gamePublic) {
                spectators.add(gamePlayer);
                return true;
            }
        }
        return false;
    }

    public void resign(int player) {
        if(gameRunning || !started) {
            if (player == 1) {
                endGame(2, "Player 1 resigned.");
            } else if (player == 2) {
                endGame(1, "Player 2 resigned.");
            }
        }
    }

    public void endGame(int winner, String reason) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "game_over");
        JSONObject data = new JSONObject();
        data.put("winner", winner);
        data.put("reason", reason);
        sendPacketToPlayers(jsonObject);

        gameRunning = false;
    }

    public void startGame() {
        gameRunning = true;
        started = true;
        JSONObject game_start = new JSONObject();
        game_start.put("type", "game_start");
        sendPacketToPlayers(game_start);
        sendMoveList();
    }
}
