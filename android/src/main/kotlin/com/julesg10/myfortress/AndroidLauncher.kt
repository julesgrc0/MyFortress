package com.julesg10.myfortress

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.julesg10.myfortress.MainGame

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {

    private val game:MainGame = MainGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = AndroidApplicationConfiguration()
        initialize(this.game, configuration)
    }


}