package com.games.ng8542gd.memory.util;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;

import com.games.ng8542gd.memory.R;

/*
    This class manages the sound effect in a single pool so that they don't to be loaded separately for each class that needs to use them
 */

/*
 *   Note that since this app is not allowing android versions prior to lollipop that this class is
 *   not checking for build versions.  If the app allowed older build versions then this class would
 *   need another different sound pool to accommodate earlier devices.
 */

public class SoundEffects {
    public static String LOGTAG = "SoundEffects";

    SoundPool soundPool;
    AppCompatActivity context;

    // sound effects
    int correctGuessSound, incorrectGuessSound, applauseSound;

    public SoundEffects(AppCompatActivity context){
        this.context = context;

        // note that most of this doesn't need to be specified - if not specified then will use default values
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
        loadSounds();
    }

    private void loadSounds(){
        correctGuessSound = soundPool.load(context, R.raw.match, 1);
        incorrectGuessSound = soundPool.load(context, R.raw.no_match, 1);
        applauseSound = soundPool.load(context, R.raw.applause, 1);
    }

    public void playSound(int id){
        switch(id){
            case 1:
                soundPool.play(correctGuessSound, 1, 1, 1, 0, 1);
                break;
            case 2:
                soundPool.play(incorrectGuessSound, 1, 1, 1, 0, 1);
                break;
            case 3:
                soundPool.play(applauseSound, 1, 1, 1, 0, 1);
                break;
        }
    }
}
