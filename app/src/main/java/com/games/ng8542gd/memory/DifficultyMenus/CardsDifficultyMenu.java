package com.games.ng8542gd.memory.DifficultyMenus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.games.ng8542gd.memory.R;
import com.games.ng8542gd.memory.util.CentralManager;

/*
    This class contains the menu for the cards game, options selected here are stored in variables in the central manager class
 */
public class CardsDifficultyMenu extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    RadioButton matchesEasyRadioButton, matchesMediumRadioButton, matchesHardRadioButton, pairsRadioButton,
            triosRadioButton, flipTimeEasyRadioButton, flipTimeMediumRadioButton, flipTimeHardRadioButton;

    RadioGroup numberOfMatchesRadioGroup;
    RadioGroup pairsOrTriosRadioGroup;
    RadioGroup flipTimeRadioGroup;

    TextView numberOfMatchesLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_menu_cards);

        // get the label for number of matches
        numberOfMatchesLabel = (TextView)findViewById(R.id.grid_size_label);

        // set up radio groups
        numberOfMatchesRadioGroup = (RadioGroup)findViewById(R.id.grid_size_radio_group);
        pairsOrTriosRadioGroup = (RadioGroup)findViewById(R.id.pairs_trios_radio_group);
        flipTimeRadioGroup = (RadioGroup)findViewById(R.id.fliptime_radio_group);

        // set up radio group listeners
        numberOfMatchesRadioGroup.setOnCheckedChangeListener(this);
        pairsOrTriosRadioGroup.setOnCheckedChangeListener(this);
        flipTimeRadioGroup.setOnCheckedChangeListener(this);

        // set up radio buttons
        matchesEasyRadioButton = (RadioButton)findViewById(R.id.easy_grid_radioButton);
        matchesMediumRadioButton = (RadioButton)findViewById(R.id.matches_medium_radiobutton);
        matchesHardRadioButton = (RadioButton)findViewById(R.id.matches_hard_radiobutton);
        pairsRadioButton = (RadioButton)findViewById(R.id.yes_sequence_radioButton);
        triosRadioButton = (RadioButton)findViewById(R.id.trios_radiobutton);
        flipTimeEasyRadioButton = (RadioButton)findViewById(R.id.fliptime_easy_radiobutton);
        flipTimeMediumRadioButton = (RadioButton)findViewById(R.id.fliptime_medium_radiobutton);
        flipTimeHardRadioButton = (RadioButton)findViewById(R.id.fliptime_hard_radiobutton);

        // get the saved state of the radio buttons
       String numberOfMatches = CentralManager.getMatchesDifficultyLevel();
       String pairsOrTrios = CentralManager.getPairsOrTrios();
       float flipTime = CentralManager.getFlipTime();

       // number of matches
       if (numberOfMatches == "easy")
           matchesEasyRadioButton.setChecked(true);
       else if (numberOfMatches == "medium")
           matchesMediumRadioButton.setChecked(true);
       else
           matchesHardRadioButton.setChecked(true);

       // pairs or trios
       if (pairsOrTrios.equals("pairs"))
           pairsRadioButton.setChecked(true);
       else
           triosRadioButton.setChecked(true);

       // flip time
        if (flipTime == 2f)
            flipTimeEasyRadioButton.setChecked(true);
        else if (flipTime == 1f)
            flipTimeMediumRadioButton.setChecked(true);
        else if (flipTime == 0.5f)
            flipTimeHardRadioButton.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        // when a radio button state changes, update the central manager to reflect the change

        if (radioGroup == numberOfMatchesRadioGroup){
            if (checkedId == R.id.easy_grid_radioButton)
                CentralManager.setMatchesDifficultyLevel("easy");
            else if (checkedId == R.id.matches_medium_radiobutton)
                CentralManager.setMatchesDifficultyLevel("medium");
            else if (checkedId == R.id.matches_hard_radiobutton)
                CentralManager.setMatchesDifficultyLevel("hard");
        }

        else if (radioGroup == pairsOrTriosRadioGroup){
            if (checkedId == R.id.yes_sequence_radioButton) {
                CentralManager.setPairsOrTrios("pairs");
                numberOfMatchesLabel.setText("Number of pairs");
                matchesEasyRadioButton.setText("3");
                matchesMediumRadioButton.setText("5");
                matchesHardRadioButton.setText("10");
            }
            else if (checkedId == R.id.trios_radiobutton) {
                CentralManager.setPairsOrTrios("trios");
                numberOfMatchesLabel.setText("Number of trios");
                matchesEasyRadioButton.setText("3");
                matchesMediumRadioButton.setText("4");
                matchesHardRadioButton.setText("6");
            }
        }

        else if (radioGroup == flipTimeRadioGroup){
            if (checkedId == R.id.fliptime_easy_radiobutton)
                CentralManager.setFlipTime(2f);
            else if (checkedId == R.id.fliptime_medium_radiobutton)
                CentralManager.setFlipTime(1f);
            else if (checkedId == R.id.fliptime_hard_radiobutton)
                CentralManager.setFlipTime(0.5f);
        }
    }
}
