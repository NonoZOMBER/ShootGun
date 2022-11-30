package com.example.Screens.score.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.example.load.skin.ShootGun;

/**
 * Created by Nono on 24/11/2022.
 */
public class AddScoreScreen extends ScreenAdapter {
    private ShootGun game;
    private int score;
    public AddScoreScreen(ShootGun game, int score) {
        this.game = game;
        this.score = score;
    }


}
