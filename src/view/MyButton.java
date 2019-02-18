package view;

import network.Request;
import view.states.connection.ConnectionPanel;
import view.states.createGame.CreateGamePanel;
import view.states.gameVsPlayer.GameVsPlayerPanel;
import view.states.setShips.SetShipsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyButton extends JButton {
    //private static int typeButtonCount = 0;

    public static final int gameVsPlayerButton = 0;
    public static final int gameVsBotButton = 1;
    public static final int helpButton = 2;
    public static final int exitButton = 3;

    public static final int quickGameButton = 4;
    public static final int createGameButton = 5;
    public static final int connectionButton = 6;
    public static final int backButton = 7;

    public static final int orientButton = 8;
    public static final int removeShipButton = 9;
    public static final int readyButton = 10;
    public static final int quitGameButton = 11;
    public static final int setAllShipsButton = 12;

    public static final int sendRequestToCreateGameButton = 13;
    public static final int sendRequestToConnectionButton = 14;

    private static int defaultTextSize = 48;
    private final JPanel parentPanel;

    public MyButton(String text, int thisButton, JPanel parentPanel, MyFrame parentFrame) {
        super(text);
        setFont(new Font(Font.DIALOG, Font.PLAIN, defaultTextSize));
        addMouseListener(new MyButtonMouseListener(thisButton, parentPanel, parentFrame));
        this.parentPanel = parentPanel;
    }

    public static void changeDefaultTextSize(int size) {
        defaultTextSize = size;
    }

    private class MyButtonMouseListener implements MouseListener {

        private final int thisButton;
        private final JPanel parentPanel;
        private final MyFrame parentFrame;
        private boolean entered;

        public MyButtonMouseListener(int thisButton, JPanel parentPanel, MyFrame parentFrame) {
            super();
            this.thisButton = thisButton;
            this.parentPanel = parentPanel;
            this.parentFrame = parentFrame;
            entered = false;
        }

        public void mouseClicked(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}

        public void mouseReleased(MouseEvent e) {
            if (isEnabled() && e.getButton() == MouseEvent.BUTTON1 && entered) {
                switch(thisButton) {
                    case gameVsPlayerButton:
                        parentPanel.setVisible(false);
                        parentPanel.getParent().getComponent(MyFrame.gameVsPlayerState).setVisible(true);
                        break;
                    case gameVsBotButton:
                        /*parentFrame.sendRequest(Request.gameVsBot);
                        String response = parentFrame.receiveResponse();
                        if (response.equals("ok")) {
                            //перейти к SetShipsPanel

                        }
                        else {
                            //say that fail
                        }*/
                        parentFrame.sendRequest(new Request.PlayVsBot());
                        parentPanel.setVisible(false);
                        //((SetShipsPanel)parentPanel.getParent().getComponent(MyFrame.setShipsState)).setGameType(SetShipsPanel.vsBotType);
                        parentPanel.getParent().getComponent(MyFrame.setShipsState).setVisible(true);
                        parentFrame.pack();
                        break;
                    case helpButton:
                        break;
                    case exitButton:
                        System.exit(0); //todo уведомлять сервер о выходе, если есть соединение (его скорее всего нет)
                        break;
                    case quickGameButton:
                        //parentFrame.sendRequest(new Request.PlayVsRandomPlayer());
                        parentFrame.sendRequest(new Request.PlayVsRandomPlayer());
                        parentPanel.setVisible(false);
                        //((SetShipsPanel)parentPanel.getParent().getComponent(MyFrame.setShipsState)).setGameType(SetShipsPanel.vsRandomPlayerType);
                        parentPanel.getParent().getComponent(MyFrame.setShipsState).setVisible(true);
                        parentFrame.pack();
                        break;
                    case createGameButton:
                        parentPanel.setVisible(false);
                        parentPanel.getParent().getComponent(MyFrame.createGameState).setVisible(true);
                        break;
                    case connectionButton:
                        parentPanel.setVisible(false);
                        parentPanel.getParent().getComponent(MyFrame.connectionState).setVisible(true);
                        break;
                    case backButton:
                        parentPanel.setVisible(false);
                        if (parentPanel instanceof GameVsPlayerPanel)
                            parentPanel.getParent().getComponent(MyFrame.mainState).setVisible(true);
                        else if (parentPanel instanceof ConnectionPanel || parentPanel instanceof CreateGamePanel) {
                            parentPanel.getParent().getComponent(MyFrame.gameVsPlayerState).setVisible(true);
                        }
                        else
                            parentPanel.setVisible(true);
                        break;
                    case orientButton:
                        ((SetShipsPanel)parentPanel).changeOrient();
                        break;
                    case readyButton:
                        parentFrame.sendRequest(new Request.StartGame(((SetShipsPanel)parentPanel).getPlayer()));
                        break;
                    case removeShipButton:
                        ((SetShipsPanel)parentPanel).removeShip();
                        break;
                    case quitGameButton:
                        //todo создать диалоговое окно, задав вопрос, действительно ли хочет выйти
                        parentFrame.sendRequest(new Request.QuitGame());
                        parentPanel.setVisible(false);
                        parentPanel.getParent().getComponent(MyFrame.mainState).setVisible(true);
                        parentFrame.setDefaultSize();
                        parentFrame.leaveServer();
                        break;
                    case setAllShipsButton:
                        ((SetShipsPanel)parentPanel).setAllShips();
                        break;
                    case sendRequestToCreateGameButton:
                        String gameName1 = ((CreateGamePanel)parentPanel).getGameName();
                        String password1 = ((CreateGamePanel)parentPanel).getPassword();
                        if (!gameName1.isEmpty())
                           parentFrame.sendRequest(new Request.PlayVsPlayer(gameName1, password1, true));
                        break;
                    case sendRequestToConnectionButton:
                        String gameName2 = ((ConnectionPanel)parentPanel).getGameName();
                        String password2 = ((ConnectionPanel)parentPanel).getPassword();
                        if (!gameName2.isEmpty())
                            parentFrame.sendRequest(new Request.PlayVsPlayer(gameName2, password2, false));
                        break;
                }
            }
        }

        public void mouseEntered(MouseEvent e) {
            entered = true;
        }
        public void mouseExited(MouseEvent e) {
            entered = false;
        }
    }
}
