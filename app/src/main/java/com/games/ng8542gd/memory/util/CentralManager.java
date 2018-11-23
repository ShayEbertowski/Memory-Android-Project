package com.games.ng8542gd.memory.util;

/*
    This class will be a centralized place to contain the values of options as set by the user.
    The options menus will write to this class and the game controllers will read from this class.
 */
public class CentralManager {

    // data members for card game - defaulting each to the easiest option
    public static String matchesDifficultyLevel = "easy";
    public static String pairsOrTrios = "pairs";
    public static float flipTime = 2f;


    // data members for grid game - defaulting each to the easiest option
    public static boolean isInSequence = false;
    public static String gridSize = "easy";
    public static float highlightTime = 2f;

    // main settings
    public static boolean soundIsOn = true;


    // getters for card game
    public static String getMatchesDifficultyLevel(){
        return matchesDifficultyLevel;
    }
    public static String getPairsOrTrios() { return pairsOrTrios; }
    public static float getFlipTime() { return flipTime; }


    // setters for card game
    public static void setMatchesDifficultyLevel(String numberOfMatchesChoice){
        matchesDifficultyLevel = numberOfMatchesChoice;
    }
    public static void setPairsOrTrios(String pairsOrTriosChoice){
        pairsOrTrios = pairsOrTriosChoice;
    }
    public static void setFlipTime(float flipTimeChoice){
        flipTime = flipTimeChoice;
    }


    // getters for grid game
    public static boolean getIsInSequence(){ return isInSequence;};
    public static String getGridSize(){ return gridSize;}
    public static float getHighlightTime() { return highlightTime; }

    // setters for grid game
    public static void setIsInSequence(boolean value){
        isInSequence = value;
    }
    public static void setGridSize(String s){
        gridSize = s;
    }
    public static void setHighlightTime(float f){
        highlightTime = f;
    }


    // getters for main settings
    public static boolean getSoundIsOn(){
        return soundIsOn;
    }

    // setters for main settings
    public static void setSoundIsOn(boolean value){
        soundIsOn = value;
    }
}
