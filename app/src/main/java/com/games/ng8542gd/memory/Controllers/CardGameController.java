package com.games.ng8542gd.memory.Controllers;

import android.os.Bundle;
import android.widget.ImageView;

import com.games.ng8542gd.memory.R;
import com.games.ng8542gd.memory.util.CentralManager;


/*
    This class is the parent class of both the card pairs and card trios controller, and as such, it contains methods and data members
    that both of these child classes will inherit
 */
public class CardGameController extends Controller{
    // TODO: still haven't added animations to the card flips... might be nice to do so
    float flipTime; // the amount of time that the player can view the cards before they are flipped back over
    String numberOfMatches; // how many pairs or trios of cards that exist in the game
    String[] easyTags, mediumTags, hardTags, tags; // tags used to identify the cards; easy, medium, and hard refer to how many matches there are

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canGuess = false; //
        flipTime = CentralManager.getFlipTime(); // getting the user-selected setting from the options menu
        shuffle(tags); // generate pseudo-random order of cards
        resetCards();
    }

    String firstGuess = "", secondGuess = "";  // these will be assigned as player chooses cards
    boolean playerMadeFirstGuess = false;
    ImageView firstGuessCard, mostRecentGuessCard; // used to identify which cards were touched by the player

    protected boolean checkIfGameIsComplete(){
        Boolean result = true; // defaulting to true, logic of this method will determine if this changes
        for (int i = 0; i < childCount; i++) {
            if (gridLayout.getChildAt(i).isClickable()) {
                // if there are any active cards left then the game is not complete
                result = false;
            }
        }
        // if isComplete returns true then all matches have been found, otherwise, the game is still going
        return result;
    }

    // the following method visually flips the touched card face-forward based on the tag passed in,
    // allowing the player to brielfy view the cards face
    protected void flipCardFaceForward(String cardTag, ImageView card){
        switch ((String)cardTag) {
            case "heart":
                card.setImageResource(R.drawable.heart_card);
                break;
            case "rainbow":
                card.setImageResource(R.drawable.rainbow_card);
                break;
            case "star":
                card.setImageResource(R.drawable.star_card);
                break;
            case "balloon":
                card.setImageResource(R.drawable.balloon_card);
                break;
            case "clover":
                card.setImageResource(R.drawable.clover_card);
                break;
            case "cupcake":
                card.setImageResource(R.drawable.cupcake_card);
                break;
            case "island":
                card.setImageResource(R.drawable.island_card);
                break;
            case "smiley":
                card.setImageResource(R.drawable.smiley_card);
                break;
            case "snowman":
                card.setImageResource(R.drawable.snowman_card);
                break;
            case "sunshine":
                card.setImageResource(R.drawable.sunshine_card);
                break;
        }
    }


    protected void resetCards(){
        for (int i = 0; i < childCount; i++){
            // saving the current card in a temporary variable
            ImageView card = (ImageView)gridLayout.getChildAt(i);

            // assign tags
            card.setTag(tags[i]);

            // set images to blank cards
            card.setImageResource(R.drawable.blank_card);

            // reset opacity to fully opaque
            card.setAlpha(1f);

            // make cards clickable again since they were made unclickable when thier matches were found
            card.setClickable(true);
        }
    }
}
