package view;

import network.client.MyClient;
import network.Response;
import view.dialog.NotificationDialogWindow;
import view.states.connection.ConnectionPanel;
import view.states.createGame.CreateGamePanel;
import view.states.game.GamePanel;
import view.states.gameVsPlayer.GameVsPlayerPanel;
import view.states.main.MainPanel;
import view.states.setShips.SetShipsPanel;
import view.states.waiting.WaitingPanel;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private final static int width = 600;
    private final static int height = 550;
    public static final int textSize = 24;
    public static final Font textFont = new Font(Font.DIALOG, Font.PLAIN, textSize);

    public final static int mainState = 0;
    public final static int gameVsPlayerState = 1;
    public final static int setShipsState = 2;
    public final static int gameState = 3;
    public final static int waitingState = 4;
    public final static int createGameState = 5;
    public final static int connectionState = 6;

    private Dimension internalSize;
    private String name;
    private MyClient client;
    private boolean connected;

    private MainPanel mainPanel;
    private GameVsPlayerPanel gameVsPlayerPanel;
    private SetShipsPanel setShipsPanel;
    private GamePanel gamePanel;
    private WaitingPanel waitingPanel;
    private CreateGamePanel createGamePanel;
    private ConnectionPanel connectionPanel;

    public MyFrame(String title, MyClient client) {
        super(title);
        setDefaultSize();
        setLocationRelativeTo(null);

        this.client = client;
        connected = false;

        setResizable(false);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container c = getContentPane();

        setVisible(true);

        internalSize = c.getSize();

        c.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;

        mainPanel = new MainPanel(this);
        gameVsPlayerPanel = new GameVsPlayerPanel(this);
        setShipsPanel = new SetShipsPanel(this);
        gamePanel = new GamePanel(this);
        waitingPanel = new WaitingPanel(this);
        createGamePanel = new CreateGamePanel(this);
        connectionPanel = new ConnectionPanel(this);

        //mainPanel.setBackground(Color.YELLOW);
        //gameVsPlayerPanel.setBackground(Color.YELLOW);
        c.add(mainPanel, gbc);
        c.add(gameVsPlayerPanel, gbc);
        c.add(setShipsPanel);
        c.add(gamePanel);
        c.add(waitingPanel);
        c.add(createGamePanel);
        c.add(connectionPanel);

        setVisible(true);
    }

    public Dimension getInternalSize() {
        return internalSize;
    }

    public void setDefaultSize() {
        setSize(width, height);
        //setLocationRelativeTo(null);
    }

    /*@Override
    public void pack() {
        super.pack();
        setLocationRelativeTo(null);
        //setLocationByPlatform(false);
        //setLocation((1920 - getWidth()) / 2, (1080 - getHeight()) / 2);
    }*/

    public void sendRequest(Object request) {
        if (!connected)
            connectToServer();
        client.sendRequest(request);
    }

    /*public Object receiveResponse() {
        return client.receiveResponse();
    }*/

    public void processResponse(Object response) {
        if (response instanceof Response.StartGame) {
            /*if (((Response.StartGame)response).playerVs.equals("RandomPlayer"))
                sendRequest(new Request.OK());*/
            /*if (((Response.StartGame) response).playerVs.equals(Response.StartGame.player)) {

            }*/
            gamePanel.setMyField(setShipsPanel.getPlayer());
            gamePanel.setTurn(((Response.StartGame)response).getTurn());
            setShipsPanel.setVisible(false);
            waitingPanel.setVisible(false);
            gamePanel.setVisible(true);
            pack();
        }
        else if (response instanceof Response.IncorrectPlayer) {
            setShipsPanel.setVisible(false);
            waitingPanel.setVisible(false);
            mainPanel.setVisible(true);
            setDefaultSize();
            leaveServer();
        }
        else if (response instanceof Response.GameEvent) {
            gamePanel.processResponse((Response.GameEvent)response);
        }
        else if (response instanceof Response.GameOver) {
            gamePanel.processResponse((Response.GameOver)response);
            if (((Response.GameOver)response).youWin)
                new NotificationDialogWindow(this, NotificationDialogWindow.win, null);
                //System.out.println("Победа");
            else
                new NotificationDialogWindow(this, NotificationDialogWindow.lose, null);
                //System.out.println("Поражение");
            leaveServer();
        }
        else if (response instanceof Response.WaitOpponent) {
            waitingPanel.setText("Ожидание второго игрока...");
            setShipsPanel.setVisible(false);
            waitingPanel.setVisible(true);
        }
        else if (response instanceof Response.IncorrectRequest) {
            //todo дописать
            gamePanel.setVisible(false);
            setShipsPanel.setVisible(false);
            mainPanel.setVisible(true);
            setDefaultSize();
            leaveServer();
        }
        else if (response instanceof Response.OpponentQuitGame) {
            System.out.println("Противник вышел из игры"); // вместо этого открыть диалоговое окно
            gamePanel.processResponse((Response.OpponentQuitGame)response);
            leaveServer();
        }
        else if (response instanceof Response.CreateResponse) {
            if (((Response.CreateResponse) response).error == null) {
                waitingPanel.setText("Ожидание второго игрока...");
                createGamePanel.setVisible(false);
                waitingPanel.setVisible(true);
            }
            else {
                leaveServer();
                new NotificationDialogWindow(this, NotificationDialogWindow.error, ((Response.CreateResponse) response).error);
            }
        }
        else if (response instanceof Response.ConnectToGameResponse) {
            if (((Response.ConnectToGameResponse) response).error == null) {
                connectionPanel.setVisible(false);
                setShipsPanel.setVisible(true);
                pack();
            }
            else {
                leaveServer();
                new NotificationDialogWindow(this, NotificationDialogWindow.error, ((Response.ConnectToGameResponse) response).error);
            }
        }
        else if (response instanceof Response.Continue) {
            waitingPanel.setVisible(false);
            setShipsPanel.setVisible(true);
            pack();
        }
    }

    public void connectToServer() {
        client.connectToServer();
        connected = true;
    }

    public void leaveServer() {
        client.leaveServer();
        connected = false;
    }
}
