package com.games.ng8542gd.memory.Menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.games.ng8542gd.memory.DifficultyMenus.GridDifficultyMenu;
import com.games.ng8542gd.memory.Controllers.NonSequenceController;
import com.games.ng8542gd.memory.Controllers.SequenceController;
import com.games.ng8542gd.memory.R;
import com.games.ng8542gd.memory.util.CentralManager;

/*
    This class is simply the navigation place for the grid games
 */

public class GridMenu extends AppCompatActivity implements OnClickListener {
    Button startButton, optionsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_menu);

        // assign buttons
        startButton = (Button)findViewById(R.id.start_button);
        optionsButton = (Button)findViewById(R.id.options_button);

        //set up click listeners
        startButton.setOnClickListener(this);
        optionsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        if (view == startButton) {
            if (CentralManager.getIsInSequence()){
                startActivity(new Intent(this, SequenceController.class));
            }
            else{
                startActivity(new Intent(this, NonSequenceController.class));
            }
        }
        else if (view == optionsButton) {
            startActivity(new Intent(this, GridDifficultyMenu.class));
        }
    }
}