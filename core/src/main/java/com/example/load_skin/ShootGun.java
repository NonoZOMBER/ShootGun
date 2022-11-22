package com.example.load_skin;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.game_interfaz.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ShootGun extends Game {

	public Skin gameSkin;

	@Override
	public void create() {
		gameSkin = new Skin();
	}

	@Override
	public void dispose() {
		gameSkin.dispose();
	}
}