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
    This class manages the logic for the card game in the case that the player has chosen to work with trios
    of cards rather than pairs.

    Much of the logic in this class is the same as in the pairs controller, but differences will be commented
 */

public class CardTriosController extends CardGameController implements View.OnClickListener {
    private static final String LOGTAG = "CardPairsController"; // used for testing

    // trios of cards for each level of difficulty
    String[] easyTags = {"heart", "heart", "heart", "rainbow", "rainbow", "rainbow", "star", "star", "star"};
    String[] mediumTags = {"heart", "heart", "heart", "rainbow", "rainbow", "rainbow", "star", "star", "star", "balloon", "balloon", "balloon"};
    String[] hardTags = {"heart", "heart", "heart", "rainbow", "rainbow", "rainbow", "star", "star", "star", "balloon", "balloon", "balloon",
            "clover", "clover", "clover", "cupcake", "cupcake", "cupcake"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffects = new SoundEffects(this);

        numberOfMatches = CentralManager.getMatchesDifficultyLevel(); // getting the number of trios for the game from the players selection in the options menu

        // set the view according to the options selected by the user
        if (numberOfMatches.equals("easy")) {
            setContentView(R.layout.cards_easy_trios);
            gridLayout = (GridLayout) findViewById(R.id.easy_grid_trios);
            tags = easyTags;
        } else if (numberOfMatches.equals("medium")) {
            setContentView(R.layout.cards_medium_trios);
            gridLayout = (GridLayout) findViewById(R.id.medium_grid_trios);
            tags = mediumTags;
        } else if (numberOfMatches.equals("hard")) {
            setContentView(R.layout.cards_hard_trios);
            gridLayout = (GridLayout) findViewById(R.id.hard_grid_trios);
            tags = hardTags;
        }

        // set up cards
        childCount = gridLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            gridLayout.getChildAt(i).setOnClickListener(this);
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


    String firstGuess = "", secondGuess = "",thirdGuess = "", mostRecentGuess = "";
    boolean playerMadeFirstGuess = false, playerMadeSecondGuess = false, playerCanGuess = true;
    ImageView firstGuessCard, secondGuessCard, thirdGuessCard;

    @Override
    public void onClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        if (view instanceof ImageView) {
            if (!canGuess){
                Toast.makeText(getApplicationContext(), "Begin game before selecting cards!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                executeAccordingToImageViewPressedEvent(view);
            }
        } else if (view instanceof Button) {
            initGameButton.setText("Shuffle!");
            for (int i = 0; i < childCount; i++) {
                gridLayout.getChildAt(i).setAlpha(0);
                gridLayout.getChildAt(i).setClickable(false);
            }
            super.shuffle(tags);
            canGuess = true;
            resetCards();
            resetChildSpecificAttributes();
        }

    }

    private void executeAccordingToImageViewPressedEvent(View view) {
        ImageView card;
        if (!timeDelayProcessIsRunning){
            card = (ImageView) view;
            String cardTag = (String)view.getTag();

            //thirdGuessCard = (ImageView)view;
            // first guess ---------------------------------------------------------------------
            if (!playerMadeFirstGuess) { // this logic is executed for the players first guess
                firstGuessCard = (ImageView)view;
                playerMadeFirstGuess = true;
                firstGuess = cardTag;
                firstGuessCard.setClickable(false);
                flipCardFaceForward(firstGuess, card);
            }
            // second guess --------------------------------------------------------------------
            else if (!playerMadeSecondGuess){
                // TODO: note that not resetting cards after second guess has been made even though we know at this point that guess is wrong
                /*
                    consider making an option to reset if second card is wrong so that player doesn't
                    get to preview a third card

                 */
                secondGuessCard = (ImageView)view;
                playerMadeSecondGuess = true;
                secondGuess = cardTag;
                secondGuessCard.setClickable(false);
                flipCardFaceForward(secondGuess, card);
            }
            // third guess ---------------------------------------------------------------------
            else {
                thirdGuessCard = (ImageView)view;
                thirdGuess = cardTag;
                if (firstGuess.equals(secondGuess) && secondGuess.equals(thirdGuess)){ // all 3 guesses are equal
                    // CORRECT GUESS ---------------------------------------------------------------
                    soundEffects.playSound(1);
                    Toast.makeText(getApplicationContext(), "Nice!",
                            Toast.LENGTH_SHORT).show();
                    flipCardFaceForward(thirdGuess, card);

                    firstGuessCard.setAlpha(0.5f);
                    secondGuessCard.setAlpha(0.5f);
                    thirdGuessCard.setAlpha(0.5f);

                    firstGuessCard.setClickable(false);
                    secondGuessCard.setClickable(false);
                    thirdGuessCard.setClickable(false);

                    //reset guesses after correct guess
                    playerMadeFirstGuess = false;
                    playerMadeSecondGuess = false;
                    firstGuess = "";
                    secondGuess = "";
                    thirdGuess = "";
                    firstGuessCard = null;
                    secondGuessCard = null;
                    thirdGuessCard = null;

                    // check if game is complete
                    if (checkIfGameIsComplete()){
                        // PLAYER WON ROUND --------------------------------------------------------
                        soundEffects.playSound(3);
                        Toast.makeText(getApplicationContext(), "You Win!!!",
                                Toast.LENGTH_LONG).show();
                        initGameButton.setText("Play again!");
                    }
                }
                else{
                    // INCORRECT GUESS -------------------------------------------------------------
                    soundEffects.playSound(2);
                    Toast.makeText(getApplicationContext(), "Nope!",
                            Toast.LENGTH_SHORT).show();
                    flipCardFaceForward(thirdGuess, card);
                    thirdGuessCard.setClickable(false);

                    // start time delay
                    timeDelayProcessIsRunning = true;

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Wait for 1 second
                            firstGuessCard.setImageResource(R.drawable.blank_card);
                            secondGuessCard.setImageResource(R.drawable.blank_card);
                            thirdGuessCard.setImageResource(R.drawable.blank_card);

                            // reset guesses after incorrect guess
                            playerMadeFirstGuess = false;
                            playerMadeSecondGuess = false;
                            firstGuessCard.setClickable(true);
                            secondGuessCard.setClickable(true);
                            thirdGuessCard.setClickable(true);

                            timeDelayProcessIsRunning = false;
                        }
                    }, (long)(flipTime*1000));
                }
            }
        }
    }

    private void resetChildSpecificAttributes() { // specific to trios of cards
        // reset guesses
        firstGuess = "";
        firstGuessCard = null;
        mostRecentGuessCard = null;
        playerMadeFirstGuess = false;
    }
}
