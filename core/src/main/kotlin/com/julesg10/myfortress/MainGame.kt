package com.julesg10.myfortress

import com.badlogic.gdx.Game

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class MainGame : Game() {
    override fun create() {
        setScreen(GameScreen())
    }
}