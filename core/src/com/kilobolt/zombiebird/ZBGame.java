package com.kilobolt.zombiebird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kilobolt.screens.GameScreen;
import com.kilobolt.screens.SplashScreen;
import com.kilobolt.zbHelpers.AssetLoader;

public class ZBGame extends Game {
	public ZBGame() {
	}

	public void create() {
		AssetLoader.load();
		this.setScreen(new SplashScreen(this));
	}

	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}
