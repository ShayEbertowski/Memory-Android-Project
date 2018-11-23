package com.games.ng8542gd.memory.Controllers;

import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.games.ng8542gd.memory.R;
import com.games.ng8542gd.memory.util.CentralManager;
import com.games.ng8542gd.memory.util.SoundEffects;

/*
    This class manages the logic for the card game in the case that the player has chosen to work with pairs
    of cards rather than trios
 */
public class CardPairsController extends CardGameController implements View.OnClickListener {
    private static final String LOGTAG = "CardPairsController"; // used for testing

    // pairs of cards for each level of difficulty
    String[] easyTags = {"heart", "heart", "rainbow", "rainbow", "star", "star"};
    String[] mediumTags = {"heart", "heart", "rainbow", "rainbow", "star", "star", "balloon", "balloon",
            "clover", "clover", "cupcake", "cupcake"};
    String[] hardTags = {"heart", "heart", "rainbow", "rainbow", "star", "star", "balloon", "balloon",
            "clover", "clover", "cupcake", "cupcake", "island", "island", "smiley", "smiley",
            "snowman", "snowman", "sunshine", "sunshine"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffects = new SoundEffects(this);

        numberOfMatches = CentralManager.getMatchesDifficultyLevel(); // the number of pairs for the current game as chosen in the options menu

        // set the layout according to the options selected by the user
        if (numberOfMatches.equals("easy")) {
            setContentView(R.layout.cards_easy_pairs);
            gridLayout = (GridLayout) findViewById(R.id.easy_grid_pairs);
            tags = easyTags;
        } else if (numberOfMatches.equals("medium")) {
            setContentView(R.layout.cards_medium_pairs);
            gridLayout = (GridLayout) findViewById(R.id.medium_grid_pairs);
            tags = mediumTags;
        } else if (numberOfMatches.equals("hard")) {
            setContentView(R.layout.cards_hard_pairs);
            gridLayout = (GridLayout) findViewById(R.id.hard_grid_pairs);
            tags = hardTags;
        }

        // set up cards
        childCount = gridLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            gridLayout.getChildAt(i).setOnClickListener(this);
            // cards are not visible or clickable until the start button is pressed
            gridLayout.getChildAt(i).setAlpha(0);
            gridLayout.getChildAt(i).setClickable(false);
        }

        // assign buttons
        initGameButton = (Button) findViewById(R.id.shuffle_button);
        initGameButton.setText("Start!");

        // set click listeners
        initGameButton.setOnClickListener(this);

        // init time delay boolean to false
        timeDelayProcessIsRunning = false;
    }

    String firstGuess = "", secondGuess = "";
    boolean playerMadeFirstGuess = false;
    ImageView firstGuessCard, mostRecentGuessCard;

    @Override
    public void onClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY); // makes the device vibrate when a click event is detected
        if (view instanceof ImageView) { // means that whatever was clicked is a card and not some other input (such as a button)
            if (!canGuess){
                Toast.makeText(getApplicationContext(), "Begin game before selecting cards!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else{ // the game is in a state that allows the player to select cards
                executeAccordingToImageViewPressedEvent(view); // making this into a separate method to make this method more readable
            }
        } else if (view instanceof Button) {
            initGameButton.setText("Shuffle!"); // changing the button text from "start" to "shuffle"
            for (int i = 0; i < childCount; i++) {
                gridLayout.getChildAt(i).setAlpha(0);
                gridLayout.getChildAt(i).setClickable(false);
            }
            super.shuffle(tags);
            canGuess = true;
            resetCards();
            resetChildSpecificAttributes(); // specific to either pairs or trios
        }
    }

    private void executeAccordingToImageViewPressedEvent(View view) {
        ImageView card;
        if (!timeDelayProcessIsRunning) { // don't proceed if a time delay thread is currently running
            card = (ImageView) view; // assigning the card to the actual image that the player touches
            String cardTag = (String) view.getTag(); // assigning the tag of the image so to compare with subsequent guesses

            mostRecentGuessCard = (ImageView) view;
            // first guess ---------------------------------------------------------------------
            if (!playerMadeFirstGuess) { // the following logic executes only for the players first guess
                firstGuessCard = (ImageView) view;
                playerMadeFirstGuess = true;
                firstGuess = cardTag;
                firstGuessCard.setClickable(false);
                flipCardFaceForward(cardTag, card);
            }
            // second guess
            else {
                secondGuess = cardTag;
                if (secondGuess.equals(firstGuess)) {
                    // CORRECT GUESS ---------------------------------------------------------------
                    soundEffects.playSound(1);
                    Toast.makeText(getApplicationContext(), "Nice!",
                            Toast.LENGTH_SHORT).show();

                    flipCardFaceForward(cardTag, card);

                    // make the matched cards semi-transparent to indicate that they are inactive
                    firstGuessCard.setAlpha(0.5f);
                    mostRecentGuessCard.setAlpha(0.5f);

                    // prevent the matched cards from being clicked again during the current game
                    firstGuessCard.setClickable(false);
                    mostRecentGuessCard.setClickable(false);

                    //reset guesses after correct guess
                    playerMadeFirstGuess = false;
                    firstGuess = "";
                    firstGuessCard = null;
                    mostRecentGuessCard = null;

                    // check if game is complete
                    if (checkIfGameIsComplete()) {
                        // PLAYER WON THE ROUND ----------------------------------------------------
                        soundEffects.playSound(3);
                        Toast.makeText(getApplicationContext(), "You Win!!!",
                                Toast.LENGTH_LONG).show();
                        initGameButton.setText("Play again!");
                    }
                } else {
                    // INCORRECT GUESS -------------------------------------------------------------
                    soundEffects.playSound(2);
                    Toast.makeText(getApplicationContext(), "Nope!",
                            Toast.LENGTH_SHORT).show();
                    flipCardFaceForward(cardTag, card);

                    // start time delay
                    timeDelayProcessIsRunning = true;

                    // the following block of codes causes the cards to remain face-forward for the determined amount of time and
                    // then flips them back over (and resets thier values for the next turn)
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Wait for n seconds
                            firstGuessCard.setImageResource(R.drawable.blank_card);
                            mostRecentGuessCard.setImageResource(R.drawable.blank_card);
                            // reset guesses after incorrect guess
                            playerMadeFirstGuess = false;
                            firstGuess = "";
                            firstGuessCard.setClickable(true);
                            firstGuessCard = null;
                            mostRecentGuessCard = null;
                            timeDelayProcessIsRunning = false;
                        }
                    }, (long) (flipTime * 1000));
                }
            }
        }
    }

    private void resetChildSpecificAttributes() { // specific to the pairs controller
        // reset guesses
        firstGuess = "";
        firstGuessCard = null;
        mostRecentGuessCard = null;
        playerMadeFirstGuess = false;
    }
}
