package ait.android.minesweeper.model;

import java.util.Random;
import ait.android.minesweeper.Field;
import ait.android.minesweeper.ui.MinesweeperView;

public class MinesweeperModel {

    private static short dimension = 5;
    private static int numMines = 3;
    private static int currentMinesFlagged = 0;
    private int j1;
    private int j2;
    private int j3;
    private int i1;
    private int i2;
    private int i3;

    private Field[][] model = new Field[dimension][dimension];

    private static MinesweeperModel instance;
    public static MinesweeperModel getInstance() {
        if(instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    private MinesweeperModel() {
        makeGameArea();
    }

    public short getDimension() {
        return dimension;
    }

    public void makeRandomNumbers() {
        Random ran = new Random();
        i1 = ran.nextInt(dimension);
        j1 = ran.nextInt(dimension);
        i2 = ran.nextInt(dimension);
        j2 = ran.nextInt(dimension);
        i3 = ran.nextInt(dimension);
        j3 = ran.nextInt(dimension);
    }

    public Field getFieldContent(short x, short y) {
        return model[x][y];
    }

    public void incrementNumberMinesFlagged() {
        currentMinesFlagged += 1;
    }

    private void resetNumberMinesFlagged() {
        currentMinesFlagged = 0;
    }

    public int getCurrentMinesFlagged() {
        return currentMinesFlagged;
    }

    public int getNumMines() {
        return numMines;
    }

    public void makeGameArea() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                model[i][j] = new Field();
            }
        }
        makeMines(model);
        insideMineNeighbors();
        cornerMineNeighbors();
        rowAndColumnMineNeighbors();
    }

    private void makeMines(Field[][] fields) {
        // ***** column j first ***** - hopefully this doesn't change now that I am using a Field[][]
        makeRandomNumbers();
        fields[j1][i1].setMineStatus();
        fields[j2][i2].setMineStatus();
        fields[j3][i3].setMineStatus();
    }

    private void insideMineNeighbors() {
        for (int i = 1; i < dimension - 1; i++) {
            for (int j = 1; j < dimension - 1; j++) {
                if (model[j][i].getMineStatus()) {
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            if (!model[j + a][i + b].getMineStatus()) {
                                model[j + a][i + b].setMinesAround();
                            }
                        }
                    }
                }
            }
        }
    }

    private void rowAndColumnMineNeighbors() {
        topRow();
        leftColumn();
        bottomRow();
        rightColumn();
    }

    private void rightColumn() {
        for (int i = 1; i < dimension - 1; i++) {
            if (model[dimension - 1][i].getMineStatus()) {
                for (int a = -1; a <= 0; a++) {
                    for (int b = -1; b <= 1; b++) {
                        if (!model[dimension-1 + a][i + b].getMineStatus()) {
                            model[dimension-1 + a][i + b].setMinesAround();
                        }
                    }
                }
            }
        }
    }

    private void bottomRow() {
        for (int j = 1; j < dimension - 1; j++) {
            if (model[j][dimension - 1].getMineStatus()) {
                for (int a = -1; a <= 1; a++) {
                    for (int b = -1; b <= 0; b++) {
                        if (!model[j + a][dimension - 1 + b].getMineStatus()) {
                            model[j + a][dimension - 1 + b].setMinesAround();
                        }
                    }
                }
            }
        }
    }

    private void leftColumn() {
        for (int i = 1; i < dimension - 1; i++) {
            if (model[0][i].getMineStatus()) {
                for (int a = 0; a <= 1; a++) {
                    for (int b = -1; b <= 1; b++) {
                        if (!model[a][i + b].getMineStatus()) {
                            model[a][i + b].setMinesAround();
                        }
                    }
                }
            }
        }
    }

    private void topRow() {
        for (int j = 1; j < dimension - 1; j++) {
            if (model[j][0].getMineStatus()) {
                for (int a = -1; a <= 1; a++) {
                    for (int b = 0; b <= 1; b++) {
                        if (!model[j + a][b].getMineStatus()) {
                            model[j + a][b].setMinesAround();
                        }
                    }
                }
            }
        }
    }

    private void cornerMineNeighbors() {
        upperLeftCorner();
        lowerRightCorner();
        lowerLeftCorner();
        upperRightCorner();
    }

    private void lowerRightCorner() {
        if (model[dimension - 1][dimension - 1].getMineStatus()) {
            for (int a = -1; a <= 0; a++) {
                for (int b = -1; b <= 0; b++) {
                    if (!model[dimension - 1 + b][dimension - 1 + a].getMineStatus()) {
                        model[dimension - 1 + b][dimension - 1 + a].setMinesAround();
                    }
                }
            }
        }
    }

    private void lowerLeftCorner() {
        if (model[0][dimension - 1].getMineStatus()) {
            for (int a = -1; a <= 0; a++) {
                for (int b = 0; b <= 1; b++) {
                    if (!model[b][dimension - 1 + a].getMineStatus()) {
                        model[b][dimension - 1 + a].setMinesAround();
                    }
                }
            }
        }
    }

    private void upperRightCorner() {
        if (model[dimension - 1][0].getMineStatus()) {
            for (int a = -1; a <= 0; a++) {
                for (int b = 0; b <= 1; b++) {
                    if (!model[dimension - 1 + a][b].getMineStatus()) {
                        model[dimension - 1 + a][b].setMinesAround();
                    }
                }
            }
        }
    }

    private void upperLeftCorner() {
        if (model[0][0].getMineStatus()) {
            for (int a = 0; a <= 1; a++) {
                for (int b = 0; b <= 1; b++) {
                    if (!model[a][b].getMineStatus()) {
                        model[a][b].setMinesAround();
                    }
                }
            }
        }
    }

    public void resetGame() {
        makeGameArea();
        resetNumberMinesFlagged();
    }
}


