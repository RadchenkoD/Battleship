package model;

import java.util.ArrayList;

public class Battleship {
    private Player player1;
    private Player player2;
    //private Bot bot;
    //private int lastHitCoord;
    private boolean turn; //очередь: true - player1, false - player2
    private int noPlayers;

    private ArrayList<Integer> destroyedCoordinates;
    private int x;
    private int y;

    /*public Battleship(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        bot = null;
        moveNumber = 0;
        turn = true;
        noPlayers = 3;
    }

    public Battleship(Player player1) {
        this.player1 = player1;
        player2 = null;
        bot = new Player();
        bot.setShipsRandomly();
    }*/

    public Battleship() {
        this.player1 = null;
        this.player2 = null;
        //bot = new Bot();
        //bot.setShipsRandomly();
        //lastHitCoord = -1;
        turn = true;
        noPlayers = 3;
        destroyedCoordinates = new ArrayList<>();
        x = -1;
        y = -1;
    }

    public int setPlayers(Player player1, Player player2) {
        int res = 3;
        if (player1.isReady()) {
            if (this.player1 == null)
                noPlayers--;
            this.player1 = player1;
            res--;
        }
        if (player2.isReady()) {
            if (this.player2 == null)
                noPlayers -= 2;
            this.player2 = player2;
            res -= 2;
        }
        return res;
    }

    public boolean setPlayer(int playerNumber, Player player) {
        if (!player.isReady())
            return false;
        if (playerNumber == 1 && player1 == null) {
            player1 = player;
            noPlayers--;
            return true;
        }
        if (playerNumber == 2 && player2 == null) {
            player2 = player;
            noPlayers -= 2;
            return true;
        }
        return false;
    }

    public boolean changePlayer(int playerNumber, Player player) {
        if (!player.isReady())
            return false;
        if (playerNumber == 1 && player1 != null) {
            player1 = player;
            return true;
        }
        if (playerNumber == 2 && player2 != null) {
            player2 = player;
            return true;
        }
        return false;
    }

    public int doShotPvsP(int x, int y) {
        if (noPlayers != 0)
            return -2;
        this.x = x;
        this.y = y;
        int res;
        if (turn) {
            res = player2.mark(x, y);
            if (res == Player.destroyed || res == Player.gameOver) {
                if (player2.selectShip(x, y))
                    destroyedCoordinates = player2.getSelectedCoordinates();
            }
        }
        else {
            res = player1.mark(x, y);
            if (res == Player.destroyed || res == Player.gameOver) {
                if (player1.selectShip(x, y))
                    destroyedCoordinates = player1.getSelectedCoordinates();
            }
        }
        if (res == Player.missed)
            turn = !turn;
        return res;
    }

    public int doShotAtBot(int x, int y) {
        if (noPlayers != 0)
            return -2;
        this.x = x;
        this.y = y;
        int res = player2.mark(x, y);
        if (res == Player.destroyed || res == Player.gameOver) {
            if (player2.selectShip(x, y))
                destroyedCoordinates = player2.getSelectedCoordinates();
        }
        if (res == Player.missed)
            turn = !turn;
        return res;
    }

    public int doShotAtPlayer() {
        if (noPlayers != 0)
            return -2;
        if (!(player2 instanceof Bot)) //нет бота, есть игрок
            return -3;
        int coord = ((Bot)player2).getCoordinate();
        x = coord % Player.SIZE;
        y = coord / Player.SIZE;
        //System.out.println(x + " " + y); //TODO убрать
        int res = player1.mark(x, y);
        ((Bot)player2).setResult(res);

        if (res == Player.destroyed || res == Player.gameOver) {
            if (player1.selectShip(x, y))
                destroyedCoordinates = player1.getSelectedCoordinates();
        }
        if (res == Player.missed)
            turn = !turn;
        if (res == Player.usedCell)
            System.out.println("Всё плохо");
        return res;
    }

    public boolean getTurn() {
        return turn;
    }

    public ArrayList<Integer> getDestroyedCoordinates() {
        return destroyedCoordinates;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
