package com.example.load.skin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.Screens.options.screen.menu.screen.OptionsMenu;
import com.example.game.interfaz.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ShootGun extends Game {

	public Skin gameSkin;

	@Override
	public void create() {
		gameSkin = new Skin(Gdx.files.internal("orange/skin/uiskin.json"));
		setScreen(new OptionsMenu(this));
	}

	@Override
	public void dispose() {
		gameSkin.dispose();
	}
}