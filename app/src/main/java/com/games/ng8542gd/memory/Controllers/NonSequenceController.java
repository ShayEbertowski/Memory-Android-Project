package com.games.ng8542gd.memory.Controllers;

import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.games.ng8542gd.memory.R;
import com.games.ng8542gd.memory.util.CentralManager;
import com.games.ng8542gd.memory.util.SoundEffects;

/*
    This class inherits the grid game controller and contains the logic for the case that the player has chosen
    the non-sequence grid game
 */

// TODO: note that in both the grid and the sequence classes the player can choose MORE cells than were previewed without penalty, which might not be a big deal...?
public class NonSequenceController extends GridGameController implements OnClickListener {
    private static final String LOGTAG = "NonSequenceController";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffects = new SoundEffects(this);
        setContentView(R.layout.memory_grid_3x3);  // this will change as player advances

        TextView titleLabel = (TextView)findViewById(R.id.title_text_view); // indicating that the sequence mode is not active
        titleLabel.setText("Memory Grid (Sequence Mode Not Active)");

        highlightTime = CentralManager.getHighlightTime(); // the time that the cells are highlighted from the player to view before they are cleared

        // set up layout
        layout = (GridLayout) findViewById(R.id.easy_memory_grid);
        childCount = layout.getChildCount();

        imageViews = new ImageView[childCount];
        for (int i = 0; i < childCount; i++) {
            layout.getChildAt(i).setOnClickListener(this);
            layout.getChildAt(i).setTag(i);
            imageViews[i] = (ImageView) layout.getChildAt(i);
            imageViews[i].setTag(i);
        }

        // set up buttons
        goButton = (Button) findViewById(R.id.go_button);
        // set up button listeners - note that this must come after setting up the view
        goButton.setOnClickListener(this);
    }

    int correctlyChosenCellCount = 0;
    // TODO: make an option to automate the go button so that player doesn't have to keep pressing it between turns?
    public void onClick(View view) {
        if (view instanceof Button) { // go button was pressed
            generateRandomCells(view);
            highlightCells();
        }
        else if (view instanceof ImageView) { // player selected a cell and not some other input (such as a button)
            checkPlayerSelections(view);
        }
    }

    private void highlightCells(){
        // highlight the randomized cells
        for(int i = 0; i < randomSequenceLimitedByNumberToHightlight.length; i++){
            for (int j = 0; j < imageViews.length; j++){
                if ((Integer)imageViews[j].getTag() == randomSequenceLimitedByNumberToHightlight[i]){
                    imageViews[j].setImageResource(R.drawable.blue_highlight_square);
                }
            }
        }

        // start time delay
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  wait for n seconds
                // un-highlight the layout
                clearCellsAfterPreview();
            }
        }, (long) (1000 * highlightTime));
    }

    private void checkPlayerSelections(View view){
        if (!playerCanSelect) { // player hasn't pressed the go button yet
            Toast.makeText(getApplicationContext(), "Hit the \"Go!\" button to begin!",
                    Toast.LENGTH_SHORT).show();
            return; // must have return statement or cell will still visibly highlight
        } else {
            // player has pressed the go button and thus moved into the game-active state
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

            boolean containedInList = false;
            for (int i = 0; i < randomSequenceLimitedByNumberToHightlight.length; i++){
                if (randomSequenceLimitedByNumberToHightlight[i] == (Integer)view.getTag()){
                    containedInList = true;
                }
            }
            if (!containedInList) { // player selected cell NOT contained in system-selected layout
                soundEffects.playSound(2);
                playerCanSelect = false; // always put this first to avoid additional touch inputs that shouldn't be registered
                ((ImageView) view).setImageResource(R.drawable.x_square);
                clearCellsAfterGuess(false, layout.getChildCount(), this);
                playerCanSelect = false;
                correctlyChosenCellCount = 0;
                return;
            }
            else{
                // player selected cell that IS contained in system-selected layout
                ((ImageView) view).setImageResource(R.drawable.check_mark_square);
                correctlyChosenCellCount++;

                // the player has guessed all of the correct cells
                if (correctlyChosenCellCount == numberOfCellsToHighlight) {
                    soundEffects.playSound(1);
                    playerCanSelect = false; // always put this first to avoid additional touch inputs that shouldn't be registered
                    clearCellsAfterGuess(true, layout.getChildCount(), this);
                    playerCanSelect = false;
                    correctlyChosenCellCount = 0;
                }
            }
        }
    }

}
