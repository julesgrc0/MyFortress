package com.julesg10.myfortress

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.julesg10.myfortress.gameobjects.Level


class GameScreen : Screen {


    private var camera: Camera = OrthographicCamera()
    private var viewport = StretchViewport(world_width(), world_height(), camera)
    private var batch = SpriteBatch()

    private var font: BitmapFont? = null

    private val level = Level()
    private var gameStates: GameStates = GameStates.LOADING_SCREEN;

    private var textureAtlas: TextureAtlas? = null

    private var loadingAnimation:  AnimationController? = null;
    private var loadingTransition : AnimationTimer = AnimationTimer(10000f);
    private val loadingFrames =  arrayOfNulls<TextureRegion>(9);

    enum class GameStates
    {
        LOADING_SCREEN,
        MENU,
        LOADING_LEVEL,
        PLAYING_GAME,
        PAUSE_GAME
    }

    companion object {
        fun world_width(): Float = 160f
        fun world_height(): Float = 160f
    }

    init
    {
        camera = OrthographicCamera()
        viewport = StretchViewport(world_width(), world_height(), camera)
        batch = SpriteBatch()

        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f);
        batch.setProjectionMatrix(camera.combined);



        this.loadFont()
        this.loadTextures()
    }

    fun loadFont()
    {
        val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("Roboto.ttf"))
        val fontParameter = FreeTypeFontParameter()

        fontParameter.size = 72
        fontParameter.borderWidth = 3.6f
        fontParameter.color = Color(1f, 1f, 1f, 1f)
        fontParameter.borderColor = Color(0f, 0f, 0f, 0f)

        font = fontGenerator.generateFont(fontParameter)
        font?.data?.setScale(0.1f)

    }

    fun loadTextures()
    {
        this.textureAtlas =  TextureAtlas(Gdx.files.internal("sprites.atlas"));
        this.level.loadTextures(this.textureAtlas!!);

        val loadingTextureRegions = TextureRegion.split(Texture(Gdx.files.internal("loading_x32.png")), 32, 32);
        var k = 0;
        for (i in 0 until loadingTextureRegions.size) {
            for (j in 0 until loadingTextureRegions[i].size) {

                this.loadingFrames[k] = loadingTextureRegions[i][j]
                k++;
            }
        }

        this.loadingAnimation = AnimationController(5,this.loadingFrames.size);

    }

    override fun show() {

    }

    fun update(delta: Float)
    {
        when(this.gameStates)
        {
            GameStates.LOADING_SCREEN ->{
                if(this.loadingAnimation!!.pause)
                {
                    if(this.loadingTransition.update(delta))
                    {
                        this.gameStates = GameStates.MENU;
                    }
                }else{
                    this.loadingAnimation?.update(delta) {
                        return@update true
                    };
                }


            }
            GameStates.PLAYING_GAME ->{
                this.level.update(delta);
            }
        }
    }

    override fun render(delta: Float) {
        this.update(delta);

        this.batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        when(this.gameStates)
        {
            GameStates.LOADING_SCREEN -> {
                val loadingTexture = this.loadingFrames[this.loadingAnimation?.getIndex()!!];
                this.batch.draw(loadingTexture,30f,30f,100f,100f);
            }

            GameStates.PLAYING_GAME -> {
                this.level.render(this.batch);
            }

            GameStates.PAUSE_GAME ->{
                this.level.render(this.batch,true);
            }
        }

        this.font?.draw(batch, "%d".format((1/delta).toInt()), 10f, 10f);
        this.font?.draw(batch, "FPS", 20f, 10f);

        this.batch.end();
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    override fun pause() {
        if(this.gameStates == GameStates.PLAYING_GAME)
        {
            this.gameStates = GameStates.PAUSE_GAME;
        }
    }

    override fun resume() {
        if(this.gameStates == GameStates.PAUSE_GAME)
        {
            this.gameStates = GameStates.PAUSE_GAME;
        }
    }

    override fun hide() {
    }

    override fun dispose() {
        this.batch.dispose();
        font?.dispose();
    }
}