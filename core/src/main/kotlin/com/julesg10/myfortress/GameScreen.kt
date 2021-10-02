package com.julesg10.myfortress

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport


class GameScreen : Screen {
    private var camera: Camera = OrthographicCamera()
    private var viewport = StretchViewport(world_width(), world_height(), camera)
    private var batch = SpriteBatch()
    private var font: BitmapFont? = null

    companion object {
        fun world_width(): Float = 100f
        fun world_height(): Float = 100f
    }

    init
    {
        //this.loadFont()
    }

    fun loadFont()
    {
        val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal(""))
        val fontParameter = FreeTypeFontParameter()

        fontParameter.size = 72
        fontParameter.borderWidth = 3.6f
        fontParameter.color = Color(1f, 1f, 1f, 0.3f)
        fontParameter.borderColor = Color(0f, 0f, 0f, 0.3f)

        font = fontGenerator.generateFont(fontParameter)
    }

    override fun show() {
        // Prepare your screen here.
    }

    fun update(delta: Float)
    {

    }

    override fun render(delta: Float) {
        this.update(delta);
        this.batch.begin();

        this.batch.end();
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    override fun pause() {
        // Invoked when your application is paused.
    }

    override fun resume() {
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        // This method is called when another screen replaces this one.
    }

    override fun dispose() {
        this.batch.dispose();
    }
}