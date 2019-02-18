package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Serializable {
    protected ArrayList<Cell> field;
    protected ArrayList<Integer> setCoordinates;
    protected boolean canSet;
    //private int width; //10
    //private int SIZE; //10
    protected int n[];
    protected static final int nShipCells = 20;
    protected int nMarkedShipCells;

    protected boolean isSelected;
    protected int selectedCoord;

    protected ArrayList<Integer> shipCoordinates;
    protected ArrayList<Integer> nHits;
    protected boolean ready;
    protected boolean started;

    public static final int SIZE = 12;

    //public static final int error = -2;
    public static final int usedCell = -1;
    public static final int missed = 0;
    public static final int hit = 1;
    public static final int destroyed = 2;
    public static final int gameOver = 3;

    public Player() {
        //opponent = null;
        field = new ArrayList<>(SIZE * SIZE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = new Cell();
                if (i == 0 || j ==0 || i == SIZE - 1 || j == SIZE - 1)
                    cell.use();
                field.add(cell);
            }
        }
        n = new int[5];
        n[0] = 0;
        n[1] = 4;
        n[2] = 3;
        n[3] = 2;
        n[4] = 1;

        /*noUsedCoordinates = new ArrayList<>(100);
        for (int j = 1; j <= 10; j++)
            for (int i = 1; i <= 10; i++)
                noUsedCoordinates.add(j * SIZE + i);*/

        isSelected = false;
        selectedCoord = 0;
        nMarkedShipCells = 0;

        setCoordinates = new ArrayList<>();
        canSet = false;
        shipCoordinates = new ArrayList<>();
        nHits = new ArrayList<>(10);
        for (int i = 0; i < 10; i++)
            nHits.add(0);
        ready = false;
        started = false;
    }

    public boolean canSetShip(int len, int x, int y, boolean horizontal) {
        if (!(1 <= x && x <= 10)) {
            //System.out.println("Invalid x");
            canSet = false;
            throw new IllegalArgumentException("Неверная x координата");
        }
        if (!(1 <= y && y <= 10)) {
            //System.out.println("Invalid y");
            canSet = false;
            throw new IllegalArgumentException("Неверная y координата");
        }
        if (!(0 < len && len <= 4)) {
            //System.out.println("Invalid len");
            canSet = false;
            throw new IllegalArgumentException("Неверная длина корабля");
        }

        if (n[len] == 0) {
            System.out.println("Ships with len = " + len + " ended");
            return canSet = false;
        }
        canSet = true;
        if (!setCoordinates.isEmpty())
            setCoordinates.clear();
        int coord;
        if (horizontal) {
            if (x + len - 1 > 10) {
                //x = 11 - len;
                len = 11 - x;
                /*for (int i = x; i <= 10; i++) {
                    coord = y * SIZE + i;
                    setCoordinates.add(coord);
                }*/
                canSet = false;
            }
            else {
                for (int i = x - 1; i <= x + len; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        coord = j * SIZE + i;
                        if (field.get(coord).isShip())
                            canSet = false;
                    }
                }
            }
            for (int i = x; i < x + len; i++) {
                coord = y * SIZE + i;
                setCoordinates.add(coord);
                //field.get(coord).setShip(len, i - x, true);
            }
        }
        else {
            if (y + len - 1 > 10) {
                /*for (int j = y; j <= 10; j++) {
                    coord = j * SIZE + x;
                    setCoordinates.add(coord);
                }*/
                len = 11 - y;
                canSet = false;
                //y = 11 - len;
            }
            else {
                for (int j = y - 1; j <= y + len; j++) {
                    for (int i = x - 1; i <= x + 1; i++) {
                        coord = j * SIZE + i;
                        if (field.get(coord).isShip())
                            canSet = false;
                    }
                }
            }
            for (int j = y; j < y + len; j++) {
                coord = j * SIZE + x;
                setCoordinates.add(coord);
                //field.get(coord).setShip(len, j - y, false);
            }
        }
        /*shipCoordinates.add(y * SIZE + x);
        n[len]--;
        selectShip(x, y);
        ready = checkReady();*/
        return canSet;
    }

    public void setShip() {
        if (!canSet)
            return;
        int distance = 0;
        int len = setCoordinates.size();
        if (len > 1) {
            distance = setCoordinates.get(1) - setCoordinates.get(0);
        }
        boolean horizontal;
        if (distance / SIZE == 0) {
            horizontal = true;
        }
        else {
            horizontal = false;
        }
        for (int i = 0; i < len; i++) {
            field.get(setCoordinates.get(i)).setShip(len, i, horizontal);
        }
        canSet = false;
        shipCoordinates.add(setCoordinates.get(0));
        setCoordinates.clear();
        n[len]--;
        ready = checkReady();
    }

    /*public boolean setShip(int len, int x, int y, boolean horizontal) {
        if (!(1 <= x && x <= 10)) {
            System.out.println("Invalid x");
            return false;
        }
        if (!(1 <= y && y <= 10)) {
            System.out.println("Invalid y");
            return false;
        }
        if (!(0 < len && len <= 4)) {
            System.out.println("Invalid len");
            return false;
        }

        if (n[len] == 0) {
            System.out.println("Ships with len = " + len + " ended");
            return false;
        }
        int coord;
        if (horizontal) {
            if (x + len - 1 > 10) {
                return false;
                //x = 11 - len;
            }
            for (int i = x - 1; i <= x + len; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    coord = j * SIZE + i;
                    if (field.get(coord).isShip())
                        return false;
                }
            }
            for (int i = x; i < x + len; i++) {
                coord = y * SIZE + i;
                field.get(coord).setShip(len, i - x, true);
            }
        }
        else {
            if (y + len - 1 > 10) {
                return false;
                //y = 11 - len;
            }
            for (int j = y - 1; j <= y + len; j++) {
                for (int i = x - 1; i <= x + 1; i++) {
                    coord = j * SIZE + i;
                    if (field.get(coord).isShip())
                        return false;
                }
            }
            for (int j = y; j < y + len; j++) {
                coord = j * SIZE + x;
                field.get(coord).setShip(len, j - y, false);
            }
        }
        shipCoordinates.add(y * SIZE + x);
        n[len]--;
        selectShip(x, y);
        ready = checkReady();
        return true;
    }*/

    public boolean selectShip(int x, int y) {
        if (!(1 <= x && x <= 10)) {
            //System.out.println("Invalid x");
            throw new IllegalArgumentException("Неверная x координата");
            //return false;
        }
        if (!(1 <= y && y <= 10)) {
            //System.out.println("Invalid y");
            throw new IllegalArgumentException("Неверная y координата");
            //return false;
        }

        int coord = y * SIZE + x;
        if (field.get(coord).isShip()) {
            int orientNumber;
            if (field.get(coord).isHorizontal())
                orientNumber = 1;
            else
                orientNumber = SIZE;
            selectedCoord = coord - field.get(coord).getDistance() * orientNumber;
            isSelected = true;

            /*if (!setCoordinates.isEmpty())
                setCoordinates.clear();
            int len = field.get(selectedCoord).getLength();
            for (int i = 0; i < len; i++) {
                setCoordinates.add(selectedCoord + i * orientNumber);
            }*/
        }
        else
            isSelected = false;
        return isSelected;
    }

    int getN(int len) {
        return n[len];
    }

    public boolean removeShip() {
        if (!isSelected)
            return false;
        int len = field.get(selectedCoord).getLength();
        if (field.get(selectedCoord).isHorizontal()) {
            for (int i = 0; i < len; i++) {
                field.get(selectedCoord + i).removeShip();
            }
        }
        else {
            for (int j = 0; j < len; j++) {
                field.get(selectedCoord + j * SIZE).removeShip();
            }
        }
        isSelected = false;
        n[len]++;
        shipCoordinates.remove(Integer.valueOf(selectedCoord));
        selectedCoord = 0;
        ready = false;
        return true;
    }

    public void removeAllShips() {
        int numberOfShips = shipCoordinates.size();
        for (int i = numberOfShips - 1; i >= 0; i--) {
            int coord = shipCoordinates.get(i);
            int x = coord % SIZE;
            int y = coord / SIZE;
            selectShip(x, y);
            removeShip();
        }
        //shipCoordinates.clear();
        ready = false;
    }

    public void setShipsRandomly() {
        Random rand = new Random(System.currentTimeMillis());
        for (int len = 1; !checkReady(); len = len % 4 + 1) {
            if (n[len] == 0)
                continue;
            int coord = rand.nextInt() % 100;
            if (coord < 0)
                coord = -coord;
            int x = coord % 10 + 1;
            int y = coord / 10 + 1;
            if (canSetShip(len, x, y, rand.nextBoolean()))
                setShip();
            //setShip(len, x, y, rand.nextBoolean());
        }
        ready = true;
    }

    protected boolean checkReady() {
        if (n[1] == 0 && n[2] == 0 && n[3] == 0 && n[4] == 0)
            return true;
        return false;
    }

    public int mark(int x, int y) {
        started = true;
        int coord = y * SIZE + x;
        int res = field.get(coord).use();
        if (res == -1)
            return usedCell;
        else if (res == 0)
            return missed;

        if (field.get(coord).isHorizontal()) {
            coord -= field.get(coord).getDistance();
        }
        else {
            coord -= field.get(coord).getDistance() * SIZE;
        }

        nMarkedShipCells++;
        if (nMarkedShipCells == nShipCells)
            return gameOver;
        int index = shipCoordinates.indexOf(coord);
        nHits.set(index, nHits.get(index) + 1);
        if (nHits.get(index) == field.get(coord).getLength())
            return destroyed;
        return hit;
    }

    public boolean isReady() {
        ready = checkReady();
        return ready && !started;
    }

    /*public boolean isStarted() {
        return started;
    }*/

    public static int getLength() {
        return SIZE;
    }

    public void showField() {
        for (int i = 1; i < SIZE - 1; i++) {
            System.out.print("\t");
            for (int j = 1; j < SIZE - 1; j++) {
                int coord = i * SIZE + j;
                System.out.print(field.get(coord));
            }
            System.out.println();
        }
    }

    public ArrayList<Integer> getSetCoordinates() {
        return setCoordinates;
    }

    public ArrayList<Integer> getSelectedCoordinates() {
        ArrayList<Integer> selectedCoordinates = new ArrayList<>();
        int len = field.get(selectedCoord).getLength();
        int orientNumber;
        if (field.get(selectedCoord).isHorizontal())
            orientNumber = 1;
        else
            orientNumber = SIZE;
        for (int i = 0; i < len; i++) {
            selectedCoordinates.add(selectedCoord + i * orientNumber);
        }
        return selectedCoordinates;
    }

    public boolean getSelectedShipOrient() {
        if (selectedCoord != 0)
            return field.get(selectedCoord).isHorizontal();
        else
            throw new RuntimeException("Корабль не выбран");
    }

    /*public ArrayList<Integer> getNoUsedCoordinates() {

    }*/
    public String toString() {
        StringBuffer str = new StringBuffer(100);

        for (int j = 1; j <= 10; j++) {
            for (int i = 1; i <= 10; i++) {
                str.append(field.get(j * SIZE + i).toString());
            }
        }
        return new String(str);
    }
}
