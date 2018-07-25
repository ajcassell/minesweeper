package ait.android.minesweeper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ait.android.minesweeper.MainActivity;
import ait.android.minesweeper.R;
import ait.android.minesweeper.model.MinesweeperModel;

public class MinesweeperView extends View {

    private Paint paintBackground;
    private Paint paintGrid;
    private Paint paintFont1;
    private Paint paintFont2;
    private Paint paintFont3;
    private Paint paintDot;
    private Paint paintFont;
    private Paint paintCircle;
    private Paint paintFlag;
    private Paint paintMine;
    private Paint paintRevealedBlanks;
    private PointF tempPlayer = null;
    private Bitmap bitmapFlag;
    private Bitmap bitmapMine;

    private boolean flagMode = true;
    private boolean gameOver = false;

    public MinesweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmapFlag = BitmapFactory.decodeResource(getResources(),R.drawable.flag);
        bitmapMine = BitmapFactory.decodeResource(getResources(),R.drawable.mine);

        makePaints();
    }

    private void makePaints() {
        paintBackground = new Paint();
        paintBackground.setColor(Color.LTGRAY);
        paintBackground.setStyle(Paint.Style.FILL);

        paintGrid = new Paint();
        paintGrid.setColor(Color.WHITE);
        paintGrid.setStyle(Paint.Style.STROKE);

        paintFont = new Paint();
        paintFont.setColor(Color.RED);
        paintFont.setTextSize(30);

        paintDot = new Paint();
        paintDot.setColor(Color.BLACK);
        paintDot.setStyle(Paint.Style.FILL);

        paintMine = new Paint();
        paintMine.setColor(Color.RED);
        paintMine.setStyle(Paint.Style.STROKE);

        paintFont1 = new Paint();
        paintFont1.setColor(Color.BLUE);
        paintFont1.setTextSize(30);

        paintFont2 = new Paint();
        paintFont2.setColor(Color.GREEN);
        paintFont2.setTextSize(30);

        paintFont3 = new Paint();
        paintFont3.setColor(Color.RED);
        paintFont3.setTextSize(30);

        paintCircle = new Paint();
        paintCircle.setColor(Color.YELLOW);
        paintCircle.setStyle(Paint.Style.FILL);

        paintFlag = new Paint();
        paintFlag.setColor(Color.GREEN);
        paintFlag.setStyle(Paint.Style.FILL);

        paintRevealedBlanks = new Paint();
        paintRevealedBlanks.setColor(Color.GRAY);
        paintRevealedBlanks.setStyle(Paint.Style.FILL);
    }

    public void setNewGame() {
        gameOver = false;
    }

    public void setToggle() {
        flagMode = false;
    }

    public void unSetToggle() {
        flagMode = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, getWidth() / 5, getHeight() / 5, false);
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, getWidth() / 5, getHeight() / 5, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBackground);
        drawGameArea(canvas);
        drawPlayers(canvas);
        drawTempPlayer(canvas);
    }

    private void drawGameArea(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintGrid);
        for (int i = 1; i <= 4; i++) {
            canvas.drawLine(0, i * getHeight() / 5, getWidth(), i * getHeight() / 5, paintGrid);
            canvas.drawLine(i * getWidth() / 5, 0, i * getWidth() / 5, getHeight(), paintGrid);
        }
    }

    private void drawPlayers(Canvas canvas) {
        int count = 0;
        for (short i = 0; i < MinesweeperModel.getInstance().getDimension(); i++) {
            for (short j = 0; j < MinesweeperModel.getInstance().getDimension(); j++) {
                if (MinesweeperModel.getInstance().getFieldContent(i, j).getClickedStatus()) {
                    if (MinesweeperModel.getInstance().getFieldContent(i, j).getFlagStatus()) {
                        canvas.drawBitmap(bitmapFlag, i * getWidth() / 5, j * getHeight() / 5, null);
                        count++;
                    } else if (MinesweeperModel.getInstance().getFieldContent(i, j).getMineStatus()) {
                        canvas.drawBitmap(bitmapMine, i * getWidth() / 5, j * getHeight() / 5, null);
                        count++;
                    } else if (!MinesweeperModel.getInstance().getFieldContent(i, j).isBlank()) {
                        // draw the reveal numbers (color dependent on number)
                        String str = String.valueOf(MinesweeperModel.getInstance().getFieldContent(i, j).getMinesAround());
                        float centerX = i * getWidth() / 5 + getWidth() / 10;
                        float centerY = j * getHeight() / 5 + getHeight() / 10;
                        if (MinesweeperModel.getInstance().getFieldContent(i,j).getMinesAround() == 1) {
                            canvas.drawText(str, centerX, centerY, paintFont1);
                        } else if (MinesweeperModel.getInstance().getFieldContent(i,j).getMinesAround() == 2) {
                            canvas.drawText(str, centerX, centerY, paintFont2);
                        } else {
                            canvas.drawText(str, centerX, centerY, paintFont3);
                        }
                        count++;
                    } else {
                        // empty clicked spaces: change color so user doesn't confuse with clickable squares
                        canvas.drawRect(i * getWidth() / 5, j * getHeight() / 5, (i * getWidth() / 5) + (getWidth() / 5), (j * getHeight() / 5) + (getHeight() / 5), paintRevealedBlanks);
                        count++;
                    }
                }
            }
        }
        if (count == MinesweeperModel.getInstance().getDimension() * MinesweeperModel.getInstance().getDimension()) {
            gameOver = true;
            ((MainActivity) getContext()).showMessage(getContext().getString(R.string.last_mine_game_over_win));
        }
    }

    private void drawTempPlayer(Canvas canvas) {
        if (tempPlayer != null) {
            if (!flagMode) {
                canvas.drawCircle(tempPlayer.x, tempPlayer.y, getHeight() / 10, paintMine);
            } else {
                canvas.drawCircle(tempPlayer.x, tempPlayer.y, getHeight() / 10, paintCircle);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!gameOver) {

            if (event.getAction() == MotionEvent.ACTION_MOVE) {

                tempPlayer = new PointF(event.getX(), event.getY());

                invalidate();

            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                tempPlayer = null;

                int tX = ((int) event.getX()) / (getWidth() / 5);
                int tY = ((int) event.getY()) / (getHeight() / 5);

                if (!MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).getClickedStatus()) {
                    if (!flagMode) {
                        if (MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).getMineStatus()) {
                            MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setClickedStatus();
                            MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setMineStatus();
                            ((MainActivity) getContext()).showMessage(getContext().getString(R.string.mine_game_over));
                            gameOver = true;
                        } else {
                            // reveal appropriate blocks around (tX,tY)
                            reveal((short) tX, (short) tY);
                            invalidate();
                        }
                    } else {
                        if (MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).getMineStatus()) {
                            MinesweeperModel.getInstance().incrementNumberMinesFlagged();
                            MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setClickedStatus();
                            MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setFlagStatus();
                            if (MinesweeperModel.getInstance().getCurrentMinesFlagged() == MinesweeperModel.getInstance().getNumMines()) {
                                // board is full or last mine is flagged... you win
                                ((MainActivity) getContext()).showMessage(getContext().getString(R.string.last_mine_game_over_win));
                                gameOver = true;
                            } else {
                                // continue to next turn
                                MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setClickedStatus();
                                MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setFlagStatus();
                                invalidate();
                            }
                        } else {
                            // game over
                            MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setClickedStatus();
                            MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY).setFlagStatus();
                            ((MainActivity) getContext()).showMessage(getContext().getString(R.string.no_mine_game_over));
                            gameOver = true;
                        }
                    }
                } else {
                    // Clicking on a non-empty space
                    ((MainActivity) getContext()).showMessage(getContext().getString(R.string.not_empty));
                }
            }
        }
        return true;
    }

    public void reveal(short x, short y) {
        if (MinesweeperModel.getInstance().getFieldContent(x, y).isBlank()) {
            // reveal all touching 0s and all their respective first neighbors
            revealRecurse(x, y);
        } else {
            // reveal JUST that square
            MinesweeperModel.getInstance().getFieldContent(x, y).setClickedStatus();
        }
    }

    // recursively reveals the 8 first neighbors of any 0 squares and so on
    public void revealRecurse(short x, short y) {
        MinesweeperModel.getInstance().getFieldContent(x, y).setClickedStatus();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (inBounds((short) (x + i), (short) 0, MinesweeperModel.getInstance().getDimension()) && inBounds((short) (y + j), (short) 0, MinesweeperModel.getInstance().getDimension())) {
                    if ((i != 0 && j != 0) || (j != -1 * i) || (j != i)) { // don't want to change the center position at question nor diagonals
                        if (MinesweeperModel.getInstance().getFieldContent((short) (x + i), (short) (y + j)).isBlank() && !MinesweeperModel.getInstance().getFieldContent((short) (x + i), (short) (y + j)).getClickedStatus()) {
                            revealRecurse((short) (x + i), (short) (y + j));
                        } else {
                            if (!MinesweeperModel.getInstance().getFieldContent((short) (x + i), (short) (y + j)).getClickedStatus()) {
                                MinesweeperModel.getInstance().getFieldContent((short) (x + i), (short) (y + j)).setClickedStatus();
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean inBounds(short q, short l, short u) {
        // question, lower, upper bounds (less than - inclusive)
        if (q >= l && q < u) {
            return true;
        } else {
            return false;
        }
    }

    public void clearBoard() {
        MinesweeperModel.getInstance().resetGame();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }
}