package com.games.ng8542gd.memory.Menus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.games.ng8542gd.memory.R;
import com.games.ng8542gd.memory.util.CentralManager;

/*
    This class contains the app-wide general options
 */

public class MainOptions extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    RadioButton soundOnRadioButton, soundOffRadioButton;

    RadioGroup soundRadioGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_options);

        // set up radio groups
        soundRadioGroup = (RadioGroup)findViewById(R.id.sound_radioGroup);

        // set up radio group listeners
        soundRadioGroup.setOnCheckedChangeListener(this);

        // set up radio buttons
        soundOnRadioButton = (RadioButton)findViewById(R.id.sound_on_radioButton);
        soundOffRadioButton = (RadioButton) findViewById(R.id.sound_off_radioButton);

        // get the saved state of the radio buttons
        boolean soundIsOn = CentralManager.getSoundIsOn();

        // sequence
        if (soundIsOn){
            soundOnRadioButton.setChecked(true);
        } else{
            soundOffRadioButton.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (radioGroup == soundRadioGroup){
            if (checkedId == R.id.sound_on_radioButton) {
                CentralManager.setSoundIsOn(true);
            }
            else{
                CentralManager.setSoundIsOn(false);
            }
        }
    }
}
