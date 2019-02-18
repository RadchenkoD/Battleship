package network;

import java.io.Serializable;
import java.util.ArrayList;

public class Response {
    //public static final String ok = "ok";

    public static class StartGame implements Serializable {
        private boolean turn;
        public final String playerVs;
        public static final String player = "Player"; //todo убрать, т. к. не используется
        public static final String randomPlayer = "RandomPlayer";
        public static final String bot = "Bot";

        public StartGame(boolean turn, String playerVs) {
            this.turn = turn;
            this.playerVs = playerVs;
        }
        public boolean getTurn() {
            return turn;
        }
    }

    public static class IncorrectPlayer implements Serializable {
        //public IncorrectPlayer() {}
    }

    public static class GameEvent implements Serializable {
        private boolean turn;
        public final ArrayList<Integer> destroyedCoordinates;
        public final int x;
        public final int y;
        private boolean hit;

        /*public GameEvent() {
            turn = false;
            destroyedCoordinates = null;
            x = -1;
            y = -1;
            hit = false;
        }*/

        public GameEvent(boolean turn, ArrayList<Integer> coordinates){
            this.turn = turn;
            this.destroyedCoordinates = coordinates;
            x = -1;
            y = -1;
        }

        public GameEvent(boolean turn, int x, int y, boolean hit) {
            this.turn = turn;
            this.x = x;
            this.y = y;
            this.hit = hit;
            destroyedCoordinates = null;
        }

        public void changeTurn() {
            turn = !turn;
        }

        public boolean isTurn() {
            return turn;
        }

        public boolean isHit() {
            return hit;
        }

        public ArrayList<Integer> getDestroyedCoordinates() {
            return destroyedCoordinates;
        }
    }

    public static class GameOver implements Serializable {
        public final boolean youWin;
        /*public GameOver() {
            youWin = false;
        }*/
        public GameOver(boolean youWin) {
            this.youWin = youWin;
        }
    }

    public static class WaitOpponent implements Serializable {

    }

    public static class Continue implements Serializable {

    }

    public static class IncorrectRequest implements Serializable {
        public final String message;
        public IncorrectRequest(String message) {
            this.message = message;
            //Request.PlayVsRandomPlayer.class.getSimpleName();
        }
    }

    public static class OpponentQuitGame implements Serializable {
        public static final int setShipsStage = 0;
        public static final int playGameStage = 1;
        public final int stage;

        public OpponentQuitGame(int stage) {
            this.stage = stage;
        }
    }

    public static class Ping implements Serializable{}

    public static class CreateResponse implements Serializable {
        public final String error;
        public static final String nameAlreadyExist = "Игра с таким названием уже существует";
        //public static final String

        public CreateResponse(String error) {
            this.error = error;
        }
    }

    public static class ConnectToGameResponse implements Serializable {
        public final String error;
        public static final String notExist = "Нет игры с таким названием";
        public static final String incorrectPassword = "Неверный пароль";

        public ConnectToGameResponse(String error) {
            this.error = error;
        }
    }
}
