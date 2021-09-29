package com.julesg10.myfortress;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends Game {
	@Override
	public void create() {
		setScreen(new FirstScreen());
	}
}