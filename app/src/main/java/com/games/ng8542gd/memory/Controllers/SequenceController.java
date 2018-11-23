

package com.games.ng8542gd.memory.Controllers;

import android.os.AsyncTask;
import android.os.Bundle;
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
    This class inherits the grid game controller and contains the logic for the case that the player
    has chosen the sequence grid game option
 */

public class SequenceController extends GridGameController implements OnClickListener {
    private static final String LOGTAG = "SequenceController";

    SequentialHighlighter sequentialHighlighter; // this is a subclass (at the bottom of this file)
    int currentSpotInSequence = 0; // must keep track of the order of selections

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffects = new SoundEffects(this);

        // start separate thread
        sequentialHighlighter = new SequentialHighlighter();
        sequentialHighlighter.execute();

        setContentView(R.layout.memory_grid_3x3); // will change as player advances

        TextView titleLabel = (TextView)findViewById(R.id.title_text_view); // indicating that the sequence mode is active
        titleLabel.setText("Memory Grid (Sequence Mode Active)");

        // this is the time that the cells are available for the player to view before turning blank
        // note that this is distinct from the time between each cell highlighting in sequence
        highlightTime = CentralManager.getHighlightTime();

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

    @Override
    protected void onStart(){
        super.onStart();

        // TODO: go button doesn't work in sequence mode if activity is left and returned to, tried onResume and onStart so far and neither work
        // set up button listeners - note that this must come after setting up the view
        goButton.setOnClickListener(this);
    }

    int correctlyChosenCellCount = 0; // counts the number of correct guesses that the player has made for the current turn

    public void onClick(View view) {
        if (view instanceof Button) { // go button was pressed
            generateRandomCells(view);
            // the following passes information to the subclass
            sequentialHighlighter.setCellSequence(randomSequenceLimitedByNumberToHightlight);
            sequentialHighlighter.highlightCells();

        } else if (view instanceof ImageView) { // a cell was pressed
            checkPlayerSelections(view);
        }
    }

    private void checkPlayerSelections(View view) {
        if (!playerCanSelect) { // player hasn't pressed the go button yet
            Toast.makeText(getApplicationContext(), "Hit the \"Go!\" button to begin!",
                    Toast.LENGTH_SHORT).show();
            return; // must have return statement or cell will still visibly highlight
        }
        // player has pressed the go button and thus moved into the game-active state
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


        boolean containedInList = false;
        for (int i = 0; i < randomSequenceLimitedByNumberToHightlight.length; i++) {
            // comparing the selected cell to all cells in the system-selected cells to see if it exists
            if (randomSequenceLimitedByNumberToHightlight[i] == (Integer) view.getTag()) {
                containedInList = true;
            }
        }

        if (!containedInList) { // player selected cell NOT contained in system-selected cells
            soundEffects.playSound(2);
            playerCanSelect = false; // always put this first to avoid additional touch inputs that shouldn't be registered
            ((ImageView) view).setImageResource(R.drawable.x_square);
            clearCellsAfterGuess(false, layout.getChildCount(), this);
            correctlyChosenCellCount = 0;
            currentSpotInSequence = 0;
            return;
        } else { // player selected cell contained in system selected cells
            // TODO: fix: sometimes when choosing a cell that is not included in the sequence, the currentSpot is thrown off (although not when just choosing a cell thats out of order)

            if ((Integer) view.getTag() == randomSequenceLimitedByNumberToHightlight[currentSpotInSequence]) { // cell chosen is contained in list
                ((ImageView) view).setImageResource(R.drawable.check_mark_square);
                correctlyChosenCellCount++;

                // the player has guessed all of the correct cells and in the correct order
                if (correctlyChosenCellCount == numberOfCellsToHighlight) {
                    soundEffects.playSound(1);
                    playerCanSelect = false; // always put this first to avoid additional touch inputs that shouldn't be registered
                    clearCellsAfterGuess(true, layout.getChildCount(), this);
                    correctlyChosenCellCount = 0;
                    currentSpotInSequence = 0;
                } else { // the guess was correct but there are still more cells in the sequence
                    currentSpotInSequence++;
                }
            } else {  // player selected a cell that is in the sequence but not in the correct order
                soundEffects.playSound(2);
                playerCanSelect = false; // always put this first to avoid additional touch inputs that shouldn't be registered
                ((ImageView) view).setImageResource(R.drawable.x_square);
                clearCellsAfterGuess(false, layout.getChildCount(), this);
                correctlyChosenCellCount = 0;
                currentSpotInSequence = 0;
                return;
            }
        }
    }


    // subclass /////////////////////////////////////////////////////////////////////////////////////
    private class SequentialHighlighter extends AsyncTask {

        boolean running = false, shouldHighlightCells = false;

        int[] cellSequence; // get the sequence in which cells should be highlighted from the main class above
        public void setCellSequence(int[] cellSequence) {
            this.cellSequence = cellSequence;
        }

        public void highlightCells() {
            shouldHighlightCells = true;
        }

        @Override
        protected Object doInBackground(Object[] objects) { // run a separate thread
            running = true;
            run();
            return null;
        }

        void run() {
            while (running) {
                if (shouldHighlightCells) {  // on execute the following if the main thread permits it
                    for (int i = 0; i < cellSequence.length; i++) {
                        //Log.i(LOGTAG, "i " + cellSequence[i]);
                        final int currentCell = cellSequence[i];
                        try {
                            // sequentially highlight the cells in the correct order
                            Thread.sleep(500); // amount of time between each cell in the sequence highlighting
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //update main ui thread
                                    for (int i = 0; i < imageViews.length; i++) {
                                        if ((Integer) imageViews[i].getTag() == currentCell) {
                                            imageViews[i].setImageResource(R.drawable.blue_highlight_square);
                                        }
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //Log.i(LOGTAG, "---");
                    // clear the cells after sequential highlighting
                    try {
                        Thread.sleep(500); // need to sleep before clearing or the last cell in the sequence is cleared before it can be seen
                        shouldHighlightCells = false;
                        cellSequence = null;
                        clearCells();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        void clearCells() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //update main ui thread
                    clearCellsAfterPreview();
                }
            });
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

}

