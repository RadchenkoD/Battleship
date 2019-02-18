package network;

import model.Player;

import java.io.Serializable;

public class Request {
    public static final String gameVsBot = "0";
    public static final String quickGame = "1";
    public static final String createGame = "2";
    public static final String connectGame = "3";

    public static class PlayVsBot implements Serializable {
        //public final Player player;

        /*public PlayVsBot() {
            player = null;
        }*/
        /*public PlayVsBot(Player player) {
            this.player = player;
        }*/
    }

    public static class DoShot implements Serializable {
        public final int x, y;
        /*public DoShot() {
            x = -1;
            y = -1;
        }*/
        public DoShot(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class PlayVsRandomPlayer implements Serializable {
        //public final Player player;
        /*public final boolean quick;

        public PlayVsRandomPlayer(boolean quick) {
            //this.player = player;
            this.quick = quick;
        }*/
    }

    public static class PlayVsPlayer implements Serializable {
        public final String gameName;
        public final String password;
        private boolean wantToCreate;

        public PlayVsPlayer(String gameName, String password, boolean wantToCreate) {
            this.gameName = gameName;
            this.password = password;
            this.wantToCreate = wantToCreate;
        }

        public boolean isWantToCreate() {
            return wantToCreate;
        }
    }

    public static class StartGame implements Serializable {
        public final Player player;

        public StartGame(Player player) {
            this.player = player;
        }
    }

    public static class QuitGame implements Serializable {

    }

    public static class OK implements Serializable {

    }
}
