package com.games.ng8542gd.memory.Menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.games.ng8542gd.memory.R;

/*
    This class is the main navigation scene that the user sees when first opening the game, essentially, its the "home page"
 */
public class MainActivity extends AppCompatActivity implements OnClickListener{
    private static final String LOGTAG = "MainActivity";

    Button cardsButton, gridButton, mainOptionsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign the buttons
        cardsButton = findViewById(R.id.cards_menu_button);
        gridButton = findViewById(R.id.grid_menu_button);
        mainOptionsButton = findViewById(R.id.main_options_button);

        // set up click listeners
        cardsButton.setOnClickListener(this);
        gridButton.setOnClickListener(this);
        mainOptionsButton.setOnClickListener(this);
    }

    // link to individual game menus / main options
    @Override
    public void onClick(View view){
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        if (view ==  cardsButton) {
            startActivity(new Intent(this, CardsMenu.class));
        }
        if (view ==  gridButton) {
            startActivity(new Intent(this, GridMenu.class));
        }
        if (view ==  mainOptionsButton) {
            startActivity(new Intent(this, MainOptions.class));
        }
    }
}
