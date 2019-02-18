package view.states.setShips;

import model.Player;
import view.MyButton;
import view.MyFrame;

import javax.swing.*;
import java.awt.*;

public class SetShipsPanel extends JPanel {
    private MyFrame parentFrame;
    private JPanel leftPanel;
    private JButton selectedShip;
    private MyButton orientButton;
    private MyButton removeShipButton;
    private ShipButton[] ships;

    FieldPanel rightPanel;
    //private JPanel rightPanel;
    private JPanel bottomPanel;
    private MyButton readyButton;
    //private Player player;
    //private boolean horizontal;
    //private static final int textSize = 24;
    //public static final Font textFont = new Font(Font.DIALOG, Font.PLAIN, textSize);

    /*public static final int vsBotType = 0;
    public static final int vsRandomPlayerType = 1;
    public static final int vsPlayerType = 2;

    private int gameType;*/

    public SetShipsPanel(MyFrame parentFrame) {
        super(new GridBagLayout());
        setVisible(false);

        this.parentFrame = parentFrame;
        //gameType = -1;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);

        setLeftPanel();
        rightPanel = new FieldPanel(new Player());
        rightPanel.setField(FieldPanel.forShipsSetting);
        //setRightPanel();
        setBottomPanel();

        rightPanel.setRemoveShipButton(removeShipButton);
        rightPanel.setReadyButton(readyButton);

        add(leftPanel, gbc);
        gbc.gridx++;
        add(rightPanel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        add(bottomPanel, gbc);

        //player = new Player();
        //horizontal = true;
        selectedShip = null;
    }

    private void setLeftPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);
        leftPanel = new JPanel(new GridBagLayout());

        JLabel infLabel1 = new JLabel("Разместите на поле");
        infLabel1.setFont(MyFrame.textFont);
        JLabel infLabel2 = new JLabel("ваши корабли:");
        infLabel2.setFont(MyFrame.textFont);
        JLabel orientLabel = new JLabel("Ориентация:");
        orientLabel.setFont(MyFrame.textFont);

        JPanel shipsPanel = new JPanel(new GridBagLayout());

        JLabel[] labels = new JLabel[4];
        ships = new ShipButton[4];
        for (int i = 0; i < 4; i++) {
            labels[i] = new JLabel((4 - i) + "x");
            labels[i].setFont(MyFrame.textFont);
            ships[i] = new ShipButton(this, i + 1, false, true);
            ships[i].setCountLabel(labels[i]);
        }

        shipsPanel.add(labels[3], gbc);
        gbc.gridy++;
        shipsPanel.add(labels[2], gbc);
        gbc.gridy++;
        shipsPanel.add(labels[1], gbc);
        gbc.gridy++;
        shipsPanel.add(labels[0], gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx++;
        shipsPanel.add(ships[3], gbc);
        gbc.gridy++;
        shipsPanel.add(ships[2], gbc);
        gbc.gridy++;
        shipsPanel.add(ships[1], gbc);
        gbc.gridy++;
        shipsPanel.add(ships[0], gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        MyButton.changeDefaultTextSize(MyFrame.textSize);
        orientButton = new MyButton("Горизонтальная", MyButton.orientButton, this, parentFrame);
        removeShipButton = new MyButton("Убрать корабль", MyButton.removeShipButton, this, parentFrame);
        removeShipButton.setEnabled(false);

        gbc.insets.bottom = 0;
        leftPanel.add(infLabel1, gbc);
        gbc.insets.top = 0;
        gbc.insets.bottom = 5;
        gbc.gridy++;
        leftPanel.add(infLabel2, gbc);
        gbc.insets.top = 5;
        gbc.gridy++;
        leftPanel.add(shipsPanel, gbc);
        gbc.gridy++;
        leftPanel.add(orientLabel, gbc);
        gbc.gridy++;
        leftPanel.add(orientButton, gbc);
        gbc.gridy++;
        leftPanel.add(removeShipButton, gbc);
        gbc.gridy++;
    }

    private void setBottomPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);
        bottomPanel = new JPanel(new GridBagLayout());

        MyButton.changeDefaultTextSize(MyFrame.textSize + MyFrame.textSize / 2);
        MyButton exit = new MyButton("Покинуть игру", MyButton.quitGameButton, this, parentFrame);
        readyButton = new MyButton("Готов", MyButton.readyButton, this, parentFrame);
        readyButton.setPreferredSize(new Dimension(exit.getPreferredSize().width, readyButton.getPreferredSize().height));
        readyButton.setEnabled(false);
        MyButton setShipsRandomlyButton = new MyButton("Разместить все корабли", MyButton.setAllShipsButton, this, parentFrame);

        gbc.gridwidth = 2;
        bottomPanel.add(setShipsRandomlyButton, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        bottomPanel.add(exit, gbc);
        gbc.gridx++;
        bottomPanel.add(readyButton, gbc);
    }

    public void changeOrient() {
        rightPanel.changeOrient();
        if (rightPanel.isHorizontal())
            orientButton.setText("Горизонтальная");
        else
            orientButton.setText("Вертикальная");
    }

    public void removeShip() {
        ships[rightPanel.selectedShip.getLength() - 1].increaseCount();
        rightPanel.removeSelection(CellButton.white);
        removeShipButton.setEnabled(false);
    }

    public Player getPlayer() {
        return rightPanel.player;
    }

    public void setAllShips() {
        allShipCountsToNull();
        rightPanel.setAllShips();
    }

    public void allShipCountsToNull() {
        for (int i = 0; i < ships.length; i++) {
            int count = ships[i].getCount();
            while (count > 0) {
                ships[i].decreaseCount();
                count--;
            }
        }
    }

    /*public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getGameType() {
        return gameType;
    }*/
}
