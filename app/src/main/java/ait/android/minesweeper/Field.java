package ait.android.minesweeper;

public class Field {
    int minesAround;
    boolean isFlagged;
    boolean isMine;
    boolean  wasClicked;

    public Field() {
        minesAround = 0;
        isFlagged = false;
        wasClicked = false;
        isMine = false;
    }

    public void setMinesAround() {
        minesAround += 1;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public void setFlagStatus() {
        isFlagged = true;
    }

    public boolean getFlagStatus() {
        return isFlagged;
    }

    public void setMineStatus() {
        isMine = true;
    }

    public boolean getMineStatus() {
        return isMine;
    }

    public void setClickedStatus() {
        wasClicked = true;
    }

    public boolean getClickedStatus() {
        return wasClicked;
    }

    public boolean isBlank() {
        return (getMinesAround() == 0);
    }

}
