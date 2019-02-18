package model;

import java.io.Serializable;

public class Cell implements Serializable {
    private boolean used;   //true - в неё уже попадали, false - ещё не попадали
    private Ship ship;

    public Cell() {
        used = false;
        ship = null;
    }

    public void setShip(int length, int distance, boolean isHorizontal) {
        ship = new Ship(length, distance, isHorizontal);
    }

    public void removeShip() {
        ship = null;
    }

    public boolean isShip() {
        if (ship == null)
            return false;
        return true;
    }

    public int getLength() {
        return ship.getLen();
    }

    public boolean isHorizontal() {
        return ship.getOrient();
    }

    public int getDistance() {
        return ship.getDistance();
    }

    public int use() {
        if (used)
            return -1;
        used = true;
        if (ship == null)
            return 0;
        return 1;
    }

    public String toString() {
        String res;
        if (!used) {
            if (ship == null)
                res = "0";
            else
                res = ship.toString();
        }
        else {
            if (ship == null)
                res = ".";
            else
                res = "x";
        }
        return res;
    }

    private class Ship implements Serializable {
        private int len;
        private int distance;
        private boolean isHorizontal;
        //private int nHits;

        /*public Ship() {
            this.len = 0;
            this.distance = 0;
            this.isHorizontal = true;
        }*/

        public Ship(int len, int distance, boolean isHorizontal) {
            this.len = len;
            this.distance = distance;
            this.isHorizontal = isHorizontal;
            //nHits = 0;
        }

        public int getLen(){
            return len;
        }

        public boolean getOrient() {
            return isHorizontal;
        }

        public int getDistance() {
            return distance;
        }

        public String toString() {
            return Integer.toString(len);
        }
    }
}
