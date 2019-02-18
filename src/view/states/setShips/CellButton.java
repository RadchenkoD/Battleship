package view.states.setShips;

import model.Player;
import network.Request;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CellButton extends JButton {
    private int x;
    private int y;
    private FieldPanel field;
    private Color lastColor;
    //private Player player;
    /*private ShipButton selectedShip;
    private ArrayList<Integer> setCoordinates;*/
    //public static final Color myGray = Color.GRAY;
    public static final Color black = Color.BLACK;
    public static final Color myGray = new Color(70, 70, 70);
    public static final Color white = Color.WHITE;
    public static final Color yellow = Color.YELLOW;
    public static final Color blue = Color.BLUE;
    public static final Color green = Color.GREEN;
    public static final Color red = Color.RED;

    public static final int size = 40;

    private void init(int x, int y, FieldPanel field) {
        setPreferredSize(new Dimension(size, size));
        setBackground(white);
        setBorder(new LineBorder(black));
        //setIcon(new ImageIcon("resources\\00.png"));
        //setIcon(imgClosed);

        this.x = x;
        this.y = y;
        this.field = field;
    }

    public CellButton(int x, int y, FieldPanel field) {
        super();
        init(x, y, field);
    }

    public CellButton(int x, int y, FieldPanel field, boolean isPlayingMouseListener) {
        super();
        init(x, y, field);
        if (isPlayingMouseListener) {
            addMouseListener(new PlayingMouseListener());
        }
        else
            addMouseListener(new SetShipsMouseListener());
    }

    public void setColorByChar(char c) {
        if (c == '0')
            setBackground(white);
        else if (c == '1' || c == '2' || c == '3' || c == '4')
            setBackground(myGray);
        else if (c == 'x')
            setBackground(yellow);
        else if (c == '.')
            setBackground(blue);
        setLastColor();
    }

    public void setLastColor() {
        lastColor = getBackground();
        //System.out.println(lastColor);
    }

    public void setLastBackground() {
        setBackground(lastColor);
    }

    //protected void paintBorder(Graphics g) {}

    private class SetShipsMouseListener implements MouseListener {
        private boolean entered;

        public SetShipsMouseListener() {
            super();
            entered = false;
            lastColor = white;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (entered) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    Color saveColor = lastColor;
                    if (field.selectedShip != null) {
                        if (getBackground().equals(green)) {
                            int x1, y1;

                            field.removeSelection(white);
                            for (int i = 0; i < field.setCoordinates.size(); i++) {
                                int coord = field.setCoordinates.get(i);
                                x1 = coord % Player.SIZE;
                                y1 = coord / Player.SIZE;
                                field.getCell(x1, y1).setBackground(myGray);
                                field.getCell(x1, y1).setLastColor();
                            }
                            field.player.setShip();
                            if (field.player.isReady())
                                field.activateReadyButton();
                            field.setEnabledToRemoveShipButton(false);
                        }
                        else if (getBackground().equals(myGray)) {
                            deleteSelectedColor();
                            field.removeSelection(myGray);
                        }
                    }
                    if (saveColor.equals(myGray)) {
                        field.player.selectShip(x, y);
                        field.selectedCoordinates = field.player.getSelectedCoordinates();
                        field.setSelectedShip(new ShipButton(null, field.selectedCoordinates.size(), true, field.player.getSelectedShipOrient()));
                        field.player.removeShip();
                        for (int i = 0; i < field.selectedCoordinates.size(); i++) {
                            int coord = field.selectedCoordinates.get(i);
                            int x1 = coord % Player.SIZE;
                            int y1 = coord / Player.SIZE;
                            field.getCell(x1, y1).setBackground(blue);
                            field.getCell(x1, y1).setLastColor();
                        }
                        //активировать кнопку Убрать корабль
                        field.setEnabledToRemoveShipButton(true);
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            entered = true;
            if (field.selectedShip != null) {
                //lastColor = getBackground();
                boolean can = field.player.canSetShip(field.selectedShip.getLength(), x, y, field.isHorizontal());
                field.setCoordinates = field.player.getSetCoordinates();
                Color color;
                if (can)
                    color = green;
                else
                    color = red;
                for (int i = 0; i < field.setCoordinates.size(); i++) {
                    int coord = field.setCoordinates.get(i);
                    int x1 = coord % Player.SIZE;
                    int y1 = coord / Player.SIZE;
                    field.getCell(x1, y1).setLastColor();
                    if(!field.getCell(x1, y1).getBackground().equals(myGray))
                        field.getCell(x1, y1).setBackground(color);
                }
            }
        }
        @Override
        public void mouseExited(MouseEvent e) {
            entered = false;
            if (field.selectedShip != null) {
                deleteSelectedColor();
                field.setCoordinates.clear();
            }
        }

        private void deleteSelectedColor() {
            for (int i = 0; i < field.setCoordinates.size(); i++) {
                int coord = field.setCoordinates.get(i);
                int x1 = coord % Player.SIZE;
                int y1 = coord / Player.SIZE;
                if(!field.getCell(x1, y1).getBackground().equals(myGray))
                    field.getCell(x1, y1).setLastBackground();
            }
        }
    }

    private class PlayingMouseListener implements MouseListener {
        private boolean entered;
        private boolean used;

        public PlayingMouseListener() {
            super();
            entered = false;
            used = false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println(field.isCanDoShot());
            if (entered && field.isCanDoShot() && !used) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    field.sendRequest(new Request.DoShot(x, y));
                    used = true;
                    field.setCanDoShot(false);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            entered = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            entered = false;
        }
    }
}
