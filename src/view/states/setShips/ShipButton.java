package view.states.setShips;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ShipButton extends JButton {
    //public static final int size = 40;
    private int length;
    private boolean onField;
    private SetShipsPanel parentPanel;
    private boolean horizontal;
    private JLabel countLabel;
    private int count;
    //private int x;
    //private int y;
    private ShipButtonMouseListener listener;

    public ShipButton(SetShipsPanel parentPanel, int length, boolean onField, boolean horizontal) {
        this.length = length;
        this.onField = onField;
        this.parentPanel = parentPanel;
        this.horizontal = horizontal;
        countLabel = null;
        count = 0;
        //this.x = x;
        //this.y = y;
        setPreferredSize(new Dimension(CellButton.size * length, CellButton.size));
        setBackground(CellButton.myGray);

        listener = new ShipButtonMouseListener();
        addMouseListener(listener);
    }

    public int getLength() {
        return length;
    }

    public boolean isOnField() {
        return onField;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setCountLabel(JLabel label) {
        countLabel = label;
        count = Character.digit(countLabel.getText().charAt(0), 10);
    }

    public void increaseCount() {
        if (count == 0)
            addMouseListener(listener);
        countLabel.setText(++count + "x");
    }

    public void decreaseCount() {
        countLabel.setText(--count + "x");
        if (count == 0)
            removeMouseListener(listener);
    }

    public int getCount() {
        return count;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(CellButton.black);
        Dimension size = getPreferredSize();

        for (int i = 1; i < length; i++) {
            int[] xPoints1 = {size.width / length * i - 1, size.width / length * i - 1};
            int[] yPoints = {0, size.height - 1};
            //int[] xPoints2 = {size.width / length * i, size.width / length * i};

            g.drawPolyline(xPoints1, yPoints, 2);
            //g.drawPolyline(xPoints2, yPoints, 2);
        }
    }

    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        g.setColor(CellButton.black);
        Dimension size = getPreferredSize();
        int[] xPoints = {0, size.width - 1, size.width - 1, 0};
        int[] yPoints = {0, 0, size.height - 1, size.height - 1};
        g.drawPolygon(xPoints, yPoints, 4);
    }

    /*public int getXCoord() {
        return x;
    }
    public int getYCoord() {
        return y;
    }*/

    private class ShipButtonMouseListener implements MouseListener {
        private boolean entered;

        public ShipButtonMouseListener() {
            super();
            entered = false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(entered) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    parentPanel.rightPanel.removeSelection(CellButton.myGray); //todo додумать случай с уже выбранным ship
                    setBackground(CellButton.blue);
                    parentPanel.rightPanel.setSelectedShip(ShipButton.this);
                    parentPanel.rightPanel.setEnabledToRemoveShipButton(false);
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
