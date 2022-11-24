package com.example.Screens.game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.example.load.skin.ShootGun;

/**
 * Created by Nono on 24/11/2022.
 */
public class GameScreen extends ScreenAdapter {
    private ShootGun game;
    public GameScreen(ShootGun game) {
        this.game = game;
    }
}
