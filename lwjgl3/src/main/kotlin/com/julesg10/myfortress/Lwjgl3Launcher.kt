package com.julesg10.myfortress

import kotlin.jvm.JvmStatic
import com.julesg10.myfortress.Lwjgl3Launcher
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.julesg10.myfortress.MainGame
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/** Launches the desktop (LWJGL3) application.  */
object Lwjgl3Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        createApplication()
    }

    private fun createApplication(): Lwjgl3Application {
        return Lwjgl3Application(MainGame(), defaultConfiguration)
    }

    private val defaultConfiguration: Lwjgl3ApplicationConfiguration
        private get() {
            val configuration = Lwjgl3ApplicationConfiguration()
            configuration.setTitle("myfortress")
            configuration.setWindowedMode(640, 480)
            configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
            return configuration
        }
}