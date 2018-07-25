package ait.android.minesweeper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import ait.android.minesweeper.ui.MinesweeperView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newGameButton = findViewById(R.id.newGameButton);
        ToggleButton toggle = findViewById(R.id.togglebutton);
        final MinesweeperView MsView = findViewById(R.id.MsView);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isClicked) {
                if (isClicked) {
                    MsView.setToggle(); // TRY MODE
                } else {
                    MsView.unSetToggle();
                }
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MsView.clearBoard();
                MsView.setNewGame();
            }
        });
    }

    public void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

}

