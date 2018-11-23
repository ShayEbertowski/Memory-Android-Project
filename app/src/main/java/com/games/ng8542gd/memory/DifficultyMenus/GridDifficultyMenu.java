package com.games.ng8542gd.memory.DifficultyMenus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.games.ng8542gd.memory.R;
import com.games.ng8542gd.memory.util.CentralManager;

/*
    This class contains the menu for the cards game, options selected here are stored in variables in the central manager class
 */

public class GridDifficultyMenu extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    RadioButton highlightTimeEasyRadioButton, highlightTimeMediumRadioButton, highlightTimeHardRadioButton,
            isInSequenceRadioButton, isNotInSequenceRadioButton;

    RadioGroup inSequenceRadioGroup;
    RadioGroup highlightTimeRadioGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_menu_grid);

        // set up radio groups
        inSequenceRadioGroup = (RadioGroup)findViewById(R.id.in_sequence_radio_group);
        highlightTimeRadioGroup = (RadioGroup)findViewById(R.id.highlighttime_radio_group);

        // set up radio group listeners
        inSequenceRadioGroup.setOnCheckedChangeListener(this);
        highlightTimeRadioGroup.setOnCheckedChangeListener(this);

        // set up radio buttons
        isInSequenceRadioButton = (RadioButton)findViewById(R.id.yes_sequence_radioButton);
        isNotInSequenceRadioButton = (RadioButton) findViewById(R.id.no_sequence_radioButton);
        highlightTimeEasyRadioButton = (RadioButton)findViewById(R.id.highlight_time_easy_radiobutton);
        highlightTimeMediumRadioButton = (RadioButton)findViewById(R.id.highlight_time_medium_radiobutton);
        highlightTimeHardRadioButton = (RadioButton)findViewById(R.id.highlight_time_hard_radiobutton);

        // get the saved state of the radio buttons
        boolean isInSequence = CentralManager.getIsInSequence();
        String gridSize = CentralManager.getGridSize();
        float highlightTime = CentralManager.getHighlightTime();

        // sequence
        if (isInSequence){
            isInSequenceRadioButton.setChecked(true);
        } else{
            isNotInSequenceRadioButton.setChecked(true);
        }

        // highlight time
        if (highlightTime == 1f)
            highlightTimeEasyRadioButton.setChecked(true);
        else if (highlightTime == 0.5f)
            highlightTimeMediumRadioButton.setChecked(true);
        else if (highlightTime == 0.25f)
            highlightTimeHardRadioButton.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        // when a radio button state changes, update the central manager to reflect the change

        if (radioGroup == inSequenceRadioGroup){
            if (checkedId == R.id.yes_sequence_radioButton) {
                CentralManager.setIsInSequence(true);
            }
            else{
                CentralManager.setIsInSequence(false);
            }
        }
        else if (radioGroup == highlightTimeRadioGroup){
            if (checkedId == R.id.highlight_time_easy_radiobutton)
                CentralManager.setHighlightTime(1f);
            else if (checkedId == R.id.highlight_time_medium_radiobutton)
                CentralManager.setHighlightTime(0.5f);
            else if (checkedId == R.id.highlight_time_hard_radiobutton)
                CentralManager.setHighlightTime(0.25f);
        }
    }
}
