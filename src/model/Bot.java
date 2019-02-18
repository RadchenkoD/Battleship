package model;

import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player {
    private ArrayList<Integer> noUsedCoordinates;
    private ArrayList<Integer> priorityCoordinates;
    private ArrayList<Integer> hitCoordinates;
    private int lastCoord;
    public Bot() {
        super();
        noUsedCoordinates = new ArrayList<>(100);
        for (int j = 1; j <= 10; j++) {
            for (int i = 1; i <= 10; i++)
                noUsedCoordinates.add(j * SIZE + i);
        }
        priorityCoordinates = new ArrayList<>();
        hitCoordinates = new ArrayList<>();
        lastCoord = -1;
    }

    public int getCoordinate() {
        int coord;
        Random rand = new Random(System.currentTimeMillis());
        int index;
        if (!priorityCoordinates.isEmpty()) {
            index = rand.nextInt() % priorityCoordinates.size();
            if (index < 0)
                index = -index;
            coord = priorityCoordinates.get(index);
            priorityCoordinates.remove(index);
            noUsedCoordinates.remove(Integer.valueOf(coord)); //TODO не обязательно
        }
        else {
            index = rand.nextInt() % noUsedCoordinates.size();
            if (index < 0)
                index = -index;
            coord = noUsedCoordinates.get(index);
            noUsedCoordinates.remove(index);
        }

        lastCoord = coord;
        return coord;
    }

    void setResult(int res) {
        switch (res) {
            case hit:
                if (hitCoordinates.isEmpty()) {
                    hitCoordinates.add(lastCoord);
                    int index;
                    if ((index = noUsedCoordinates.indexOf(lastCoord - 1)) != -1)
                        priorityCoordinates.add(noUsedCoordinates.get(index));
                    if ((index = noUsedCoordinates.indexOf(lastCoord + 1)) != -1)
                        priorityCoordinates.add(noUsedCoordinates.get(index));
                    if ((index = noUsedCoordinates.indexOf(lastCoord - SIZE)) != -1)
                        priorityCoordinates.add(noUsedCoordinates.get(index));
                    if ((index = noUsedCoordinates.indexOf(lastCoord + SIZE)) != -1)
                        priorityCoordinates.add(noUsedCoordinates.get(index));
                }
                else {
                    hitCoordinates.add(lastCoord);
                    int orientNum;
                    int distance = lastCoord - hitCoordinates.get(0);
                    if (distance / SIZE == 0) // horizontal
                        orientNum = 1;
                    else                        // vertical
                        orientNum = SIZE;

                    priorityCoordinates.clear();
                    int index;
                    if (distance < 0) {
                        if ((index = noUsedCoordinates.indexOf(lastCoord - orientNum)) != -1)
                            priorityCoordinates.add(noUsedCoordinates.get(index));
                        if ((index = noUsedCoordinates.indexOf(lastCoord + orientNum * hitCoordinates.size())) != -1)
                            priorityCoordinates.add(noUsedCoordinates.get(index));
                    } else {
                        if ((index = noUsedCoordinates.indexOf(lastCoord + orientNum)) != -1)
                            priorityCoordinates.add(noUsedCoordinates.get(index));
                        if ((index = noUsedCoordinates.indexOf(lastCoord - orientNum * hitCoordinates.size())) != -1)
                            priorityCoordinates.add(noUsedCoordinates.get(index));
                    }
                }
                break;
            case destroyed:
                if (!priorityCoordinates.isEmpty())
                    priorityCoordinates.clear();
                if (!hitCoordinates.isEmpty()) {
                    int min = 1000;
                    int max = 0;
                    for (int i = 0; i < hitCoordinates.size(); i++) {
                        if (hitCoordinates.get(i) < min)
                            min = hitCoordinates.get(i);
                        if (hitCoordinates.get(i) > max)
                            max = hitCoordinates.get(i);
                    }
                    int distance = max - min;
                    int orientNum1, orientNum2;
                    if (distance / SIZE == 0) {
                        orientNum1 = 1;
                        orientNum2 = SIZE;
                    }
                    else {
                        orientNum1 = SIZE;
                        orientNum2 = 1;
                    }

                    for (int i = min - orientNum1; i <= max + orientNum1; i += orientNum1) {
                        noUsedCoordinates.remove(Integer.valueOf(i - orientNum2));
                        noUsedCoordinates.remove(Integer.valueOf(i + orientNum2));
                    }
                    noUsedCoordinates.remove(Integer.valueOf(min - orientNum1));
                    noUsedCoordinates.remove(Integer.valueOf(max + orientNum1));

                    hitCoordinates.clear();
                }
                else {
                    for (int i = lastCoord - 1; i <= lastCoord + 1; i++) {
                        noUsedCoordinates.remove(Integer.valueOf(i - SIZE));
                        noUsedCoordinates.remove(Integer.valueOf(i + SIZE));
                    }
                    noUsedCoordinates.remove(Integer.valueOf(lastCoord - 1));
                    noUsedCoordinates.remove(Integer.valueOf(lastCoord + 1));
                }
                break;
        }
    }

    @Override
    public void showField() {
        for (int i = 1; i < SIZE - 1; i++) {
            System.out.print("\t");
            for (int j = 1; j < SIZE - 1; j++) {
                int coord = i * SIZE + j;
                String str = field.get(coord).toString();
                if (!(str.charAt(0) == 'x' || str.charAt(0) == '.'))
                    str = "?";
                System.out.print(str);
            }
            System.out.println();
        }
    }

    public ArrayList<Integer> getNoUsedCoordinates() {
        return noUsedCoordinates;
    }
}
