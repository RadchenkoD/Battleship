package view.states.setShips;

import model.Player;
import network.Response;
import view.MyButton;
import view.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FieldPanel extends JPanel {
    private MyFrame parentFrame;
    private static final int fieldSize = 10;
    private boolean horizontal;
    private CellButton[][] cells;
    private boolean canDoShot;
    Player player;
    ShipButton selectedShip;
    ArrayList<Integer> setCoordinates;
    ArrayList<Integer> selectedCoordinates;
    private MyButton removeShipButton;
    private MyButton readyButton;

    public static final int forShipsSetting = 0;
    public static final int forPlaying = 1;
    public static final int forPlayingWithoutListener = 2;

    public FieldPanel(Player player) {
        super(new BorderLayout());
        init(player);
        parentFrame = null;
    }

    public FieldPanel(Player player, MyFrame parentFrame) {
        super(new BorderLayout());
        init(player);
        this.parentFrame = parentFrame;
    }

    private void init(Player player) {
        this.player = player;
        horizontal = true;
        selectedShip = null;
        setCoordinates = new ArrayList<>();
        selectedCoordinates = new ArrayList<>();
        removeShipButton = null;
        readyButton = null;
        canDoShot = false;

        JPanel panelWithNumbers = new JPanel(new GridLayout(10, 1));
        panelWithNumbers.setPreferredSize(new Dimension(CellButton.size, CellButton.size));
        //panelWithNumbers.setLastBackground(yellow);
        JPanel panelWithLetters = new JPanel(new GridLayout(1, 10));

        panelWithLetters.add(new JLabel(" "));
        char letter = 'А';
        for (int i = 1; i <= 10; i++) {
            JLabel numberLabel = new JLabel(String.valueOf(i), JLabel.RIGHT);
            numberLabel.setFont(MyFrame.textFont);
            numberLabel.setVerticalAlignment(JLabel.CENTER);
            JLabel letterLabel = new JLabel(String.valueOf(letter), JLabel.CENTER);
            letterLabel.setFont(MyFrame.textFont);

            panelWithNumbers.add(numberLabel);
            panelWithLetters.add(letterLabel);
            letter++;
            if (letter == 'Ё' || letter == 'Й')
                letter++;
        }

        //int fieldSize = 10;


        add(panelWithLetters, BorderLayout.NORTH);
        add(panelWithNumbers, BorderLayout.WEST);
    }

    public void setField(int purpose) {
        JPanel field = new JPanel(new GridLayout(fieldSize, fieldSize));
        cells = new CellButton[fieldSize][fieldSize];
        String fieldAsString = player.toString();

        for (int j = 0; j < fieldSize; j++) {
            for (int i = 0; i < fieldSize; i++) {
                if (purpose == forShipsSetting)
                    cells[i][j] = new CellButton(i + 1, j + 1, this, false);
                else if (purpose == forPlaying)
                    cells[i][j] = new CellButton(i + 1, j + 1, this, true);
                else if (purpose == forPlayingWithoutListener)
                    cells[i][j] = new CellButton(i + 1, j + 1, this);
                else
                    throw new RuntimeException("Не существует такой цели");
                int index = j * fieldSize + i;
                cells[i][j].setColorByChar(fieldAsString.charAt(index));
                field.add(cells[i][j]);
            }
        }

        add(field, BorderLayout.CENTER);
    }

    public void changeOrient() {
        horizontal = !horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public CellButton getCell(int x, int y) {
        return cells[x - 1][y - 1];
    }

    public void setSelectedShip(ShipButton ship) {
        selectedShip = ship;
    }

    public void removeSelection(Color newColor) {
        if (selectedShip == null)
            return;
        if (selectedShip.isOnField()) {
            for (int i = 0; i < selectedCoordinates.size(); i++) {
                int coord = selectedCoordinates.get(i);
                int x1 = coord % Player.SIZE;
                int y1 = coord / Player.SIZE;
                getCell(x1, y1).setBackground(newColor);
                getCell(x1, y1).setLastColor();
                if (i == 0 && newColor.equals(CellButton.myGray)) {
                    if (player.canSetShip(selectedShip.getLength(), x1, y1, selectedShip.isHorizontal()))
                        player.setShip();
                }
            }
            selectedCoordinates.clear();
            if (readyButton != null)
                readyButton.setEnabled(false);
        }
        else {
            //убрать обозначение выбранного корабля вне поля
            selectedShip.setBackground(CellButton.myGray); // установить соответствующую картинку
            if (!newColor.equals(CellButton.myGray))
                selectedShip.decreaseCount();
        }
        selectedShip = null;
    }

    public void setRemoveShipButton(MyButton removeShipButton) {
        this.removeShipButton = removeShipButton;
    }

    public void setReadyButton(MyButton readyButton) {
        this.readyButton = readyButton;
    }

    public void setAllShips() {
        removeSelection(CellButton.white);
        setEnabledToRemoveShipButton(false);
        player.removeAllShips();
        player.setShipsRandomly();
        String fieldAsString = player.toString();
        for (int j = 0; j < fieldSize; j++) {
            for (int i = 0; i < fieldSize; i++) {
                int index = j * fieldSize + i;
                cells[i][j].setColorByChar(fieldAsString.charAt(index));
            }
        }
        activateReadyButton();
    }

    public void setEnabledToRemoveShipButton(boolean enabled) {
        if (removeShipButton != null)
            removeShipButton.setEnabled(enabled);
    }

    public void activateReadyButton() {
        if (readyButton != null)
            readyButton.setEnabled(true);
    }

    public void processResponse(Response.GameEvent response) {
        if (response.destroyedCoordinates == null) {
            if (response.isHit())
                getCell(response.x, response.y).setBackground(CellButton.yellow);
            else
                getCell(response.x, response.y).setBackground(CellButton.blue);
        }
        else {
            for (int i = 0; i < response.destroyedCoordinates.size(); i++) {
                int coord = response.destroyedCoordinates.get(i);
                int x = coord % Player.SIZE;
                int y = coord / Player.SIZE;
                getCell(x, y).setBackground(CellButton.red);
            }
        }
        canDoShot = response.isTurn();
    }

    public void sendRequest(Object request) {
        if (parentFrame != null)
            parentFrame.sendRequest(request);
    }

    public boolean isCanDoShot() {
        return canDoShot;
    }

    public void setCanDoShot(boolean canDoShot) {
        this.canDoShot = canDoShot;
    }

    /*public void turnToFalse() {
        if (parentFrame != null)
            ((GamePanel)parentFrame.getContentPane().getComponent(MyFrame.gameState)).setCanDoShot(false);
    }*/
}
