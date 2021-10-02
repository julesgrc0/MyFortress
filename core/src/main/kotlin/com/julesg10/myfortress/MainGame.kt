package com.julesg10.myfortress

import com.badlogic.gdx.Game

class MainGame : Game() {
    override fun create() {
        setScreen(GameScreen())
    }
}