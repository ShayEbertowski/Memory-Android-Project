package com.games.ng8542gd.memory.Controllers;

import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.games.ng8542gd.memory.R;

/*
    This class contains methods and data members for the grid games.  More specific child classes for the in-sequence and
    not-in-sequence options inherit this class
 */

public class GridGameController extends Controller implements View.OnClickListener {

    GridGameController child; // used to differ between in-sequence and not-in-sequence
    boolean timeDelayThreadIsRunning = false;
    float highlightTime;

    GridLayout layout;
    int childCount;
    ImageView[] imageViews;

    Button goButton;

    int numberOfCellsToHighlight = 1; // begin with one cell, this number increments as the player makes correct guesses

    // the following are arrays for the total number of cells in the game in order, the randomized version of those cells,
    // and the array of those random cells limited by the number of cells to be highlighted for the current turn, respectively
    int[] totalOrderedCells, totalRandomizedCells, randomSequenceLimitedByNumberToHightlight;

    boolean playerCanSelect = false;

    public void onClick(View view) {
       // must be here in order to implement the interface, although only using specifically in child classes
    }

    protected void generateRandomCells(View view) {
        // TODO: should prevent player from re-shuffling during a turn both for grid and sequence games  ......
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY); // causes device to vibrate

        // the following just gets all of the cells and puts them in an array
        totalOrderedCells = new int[childCount];
        for (int i = 0; i < childCount; i++) {
            totalOrderedCells[i] = i;
        }

        totalRandomizedCells = shuffle(totalOrderedCells); // randomize all of the cells in the previously created array

        // trim the randomized array to reflect the number of cells to be highlighted in the current turn
        randomSequenceLimitedByNumberToHightlight = new int[numberOfCellsToHighlight];
        for (int i = 0; i < numberOfCellsToHighlight; i++) {
            randomSequenceLimitedByNumberToHightlight[i] = totalRandomizedCells[i];
        }
    }

    // the following turns all of the cells back to thier blank state and lifts the restriction so that the player can make selections
    protected void clearCellsAfterPreview() {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setImageResource(R.drawable.white_square);
        }
        playerCanSelect = true;
    }

    // if the guess is correct, this method clears the cells and increments the difficulty, otherwise it clears them and retains the
    // current level of difficulty
    protected void clearCellsAfterGuess(final boolean guessWasCorrect, int childCount, GridGameController child) {
        this.childCount = childCount;
        this.child = child;
        // start time delay
        timeDelayThreadIsRunning = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[i].setImageResource(R.drawable.white_square);
                }
                if (guessWasCorrect) {
                    Toast.makeText(getApplicationContext(), "Excellent!",
                            Toast.LENGTH_SHORT).show();
                    numberOfCellsToHighlight++;
                    checkProgress();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, (long) (250 * highlightTime));
    }

    // the following method increases the number of cells in the grid if the player has advanced far enough in the previous grid
    private void checkProgress(){
        if (numberOfCellsToHighlight >= childCount){
            if (childCount == 9){
                setContentView(R.layout.memory_grid_4x4);
                layout = (GridLayout) findViewById(R.id.medium_memory_grid);
            }
            else if (childCount == 16){
                setContentView(R.layout.memory_grid_5x5);
                layout = (GridLayout) findViewById(R.id.hard_memory_grid);
            }
            else{
                // TODO: indicate that the player won
            }
            soundEffects.playSound(3);
            resetListeners();
        }
    }

    // resets the listeners and prepares for the next round
    private void resetListeners(){
        numberOfCellsToHighlight = 1;
        if(childCount != 0){
            imageViews = new ImageView[childCount];
            for (int i = 0; i < childCount; i++) {
                layout.getChildAt(i).setOnClickListener(child);
                layout.getChildAt(i).setTag(i);
                imageViews[i] = (ImageView) layout.getChildAt(i);
                imageViews[i].setTag(i);
            }
            // set these in child classes
            goButton = (Button) findViewById(R.id.go_button);
            goButton.setOnClickListener(child);
        }
    }
}
