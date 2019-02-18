import model.*;
import network.Request;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*Player player1 = new Player();
        //player1.setShipsRandomly();
        Scanner scan = new Scanner(System.in);
        while (!player1.isReady()) {
            player1.showField();
            System.out.println("Enter len, x, y, orient to set ship: ");
            int len = scan.nextInt();
            int x = scan.nextInt();
            int y = scan.nextInt();
            String orient = scan.next();
            boolean o;
            if (orient.charAt(0) == 'h')
                o = true;
            else if (orient.charAt(0) == 'v')
                o = false;
            else {
                System.out.println("Wrong orient, enter all again.");
                continue;
            }
            if (player1.canSetShip(len, x, y, o))
                player1.setShip();
            //player1.setShip(len, x, y, o);
        }
        //player1.setShipsRandomly();
        /*player1.showField();
        System.out.println("Player1 ready.\n");
        Player player2 = new Player();
        player2.setShipsRandomly();
        player2.showField();/

        Bot bot = new Bot();
        bot.setShipsRandomly();
        Battleship game = new Battleship();
        game.setPlayers(player1, bot);

        System.out.println("Player: ");
        player1.showField();
        System.out.println("Bot: ");
        bot.showField();
        int x = 1;
        int y = 1;
        while(true) {

            boolean turn = game.getTurn();
            int res = Player.usedCell;
            if (turn) {
                if (x == 0) {
                    x++;
                    y++;
                }
                System.out.println("Your turn: ");
                while (res == Player.usedCell) {
                    //int x = scan.nextInt();
                    //int y = scan.nextInt();

                    res = game.doShotAtBot(x, y);
                    switch (res) {
                        case Player.usedCell:
                            System.out.println("Enter coordinates again: ");
                            break;
                        case Player.missed:
                            System.out.println("You missed");
                            break;
                        case Player.hit:
                            System.out.println("You hit ship");
                            break;
                        case Player.destroyed:
                            System.out.println("You destroy ship");
                            break;
                    }
                }
                x = (x + 1) % 11; // TODO убрать
            }
            else {
                System.out.print("Bot turn: ");
                res = game.doShotAtPlayer();
            }

            System.out.println("Player: ");
            player1.showField();
            System.out.println("Bot: ");
            bot.showField();
            System.out.println(bot.getNoUsedCoordinates());
            if (res == Player.gameOver) {
                if (turn)
                    System.out.println("You win!");
                else
                    System.out.println("You lose :(");
                break;
            }
        }*/
        /*while(true) {
            boolean turn = game.getTurn();
            if (turn)
                System.out.println("First player's turn: ");
            else
                System.out.println("Second player's turn: ");
            int res = Player.usedCell;
            while (res == Player.usedCell) {
                int x = scan.nextInt();
                int y = scan.nextInt();
                res = game.doShotPvsP(x, y);
                switch (res) {
                    case Player.usedCell:
                        System.out.println("Enter coordinates again: ");
                        break;
                    case Player.missed:
                        System.out.println("You missed");
                        break;
                    case Player.hit:
                        System.out.println("You hit ship");
                        break;
                    case Player.destroyed:
                        System.out.println("You destroy ship");
                        break;
                }
            }
            System.out.println("Player1: ");
            player1.showField();
            System.out.println("Player2: ");
            player2.showField();
            if (res == Player.gameOver) {
                if (turn)
                    System.out.println("First player win!");
                else
                    System.out.println("Second player win!");
                break;
            }
        }*/
        /*Player player1 = new Player();
        for (int i = 0; i < 5; i++) {
            player1.removeAllShips();
            player1.showField();
            System.out.println();
            player1.setShipsRandomly();
            player1.showField();
            System.out.println();

            Thread thread = Thread.currentThread();
            try {
                thread.join(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
