package com.games.ng8542gd.memory.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridLayout;

import com.games.ng8542gd.memory.util.SoundEffects;

import java.util.Random;

/*
    This class contains general methods and data members that all other game controllers will inherit and use
 */
public class Controller extends AppCompatActivity{
    SoundEffects soundEffects;

    float previewTime;
    GridLayout gridLayout;
    int childCount;
    Button initGameButton;
    Random random;
    boolean timeDelayProcessIsRunning = false, canGuess;

    // shuffle strings
    protected String[] shuffle(String[] array)
    {
        if (array != null){
            int index;
            String temp;
            Random random = new Random();
            for (int i = array.length - 1; i > 0; i--)
            {
                index = random.nextInt(i + 1);
                temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
        }
        return array;
    }

    // overloading for int rather than string
    protected int[] shuffle(int[] array) {
        int index;
        int temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }

}
