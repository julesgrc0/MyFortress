package com.julesg10.myfortress

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.julesg10.myfortress.gameobjects.Level
import com.julesg10.myfortress.gameobjects.Menu


class GameScreen : Screen {


    private val camera: Camera = OrthographicCamera()
    private val viewport = StretchViewport(world_width(), world_height(), camera)
    private val batch = SpriteBatch()

    private val hudcamera: Camera = OrthographicCamera()
    private val hudviewport = StretchViewport(world_width(), world_height(), hudcamera)
    private val hudBatch = SpriteBatch();

    private var font: BitmapFont? = null

    private val level = Level(batch, camera);
    private var menu: Menu = Menu(Vector2(0f, 0f), font);
    private var gameStates: GameStates = GameStates.LOADING_SCREEN;

    private var textureAtlas: TextureAtlas? = null

    private var loadingAnimation: AnimationController? = null;
    private var loadingTransition: AnimationTimer = AnimationTimer(5000f);
    private val loadingFrames = arrayOfNulls<TextureRegion>(9);

    enum class GameStates {
        LOADING_SCREEN,
        MENU,
        LOADING_LEVEL,
        PLAYING_GAME,
        PAUSE_GAME
    }

    companion object {
        fun world_width(): Float = 160f
        fun world_height(): Float = 160f

        fun height_pixel(pixel: Int): Float {
            return (pixel * 160f / Gdx.graphics.height)
        }

        fun height_world(pixel: Float): Int {
            return (pixel * Gdx.graphics.height / 160).toInt()
        }

        fun width_pixel(pixel: Int): Float {
            return (pixel * 160f / Gdx.graphics.width)
        }

        fun width_world(pixel: Float): Int {
            return (pixel * Gdx.graphics.width / 160).toInt()
        }

        fun default_fontscale() = 0.1f;

        fun camera_startvalue() = 80f;

        fun camera_target(position: Vector2, cameraPosition: Vector3): Vector2 {
            return Vector2(
                position.x + (cameraPosition.x - camera_startvalue()),
                position.y + (cameraPosition.y - camera_startvalue())
            )
        }

        fun camera_target_static(position: Vector2, cameraPosition: Vector3): Vector2 {
            return Vector2(
                (position.x + cameraPosition.x) - camera_startvalue(),
                (position.y + cameraPosition.y) - camera_startvalue()
            )
        }

    }

    init {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        batch.setProjectionMatrix(camera.combined);


        hudBatch.setProjectionMatrix(hudcamera.combined)

        this.loadFont()
        this.loadTextures()

        this.menu.font = this.font;
        this.menu.loadTextures();
    }

    fun loadFont() {
        val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("Roboto.ttf"))
        val fontParameter = FreeTypeFontParameter()

        fontParameter.size = 72
        fontParameter.borderWidth = 3.6f
        fontParameter.color = Color(1f, 1f, 1f, 1f)
        fontParameter.borderColor = Color(0f, 0f, 0f, 0f)

        font = fontGenerator.generateFont(fontParameter)
        font?.data?.setScale(default_fontscale())

    }

    fun loadTextures() {
        this.textureAtlas = TextureAtlas(Gdx.files.internal("sprites.atlas"));
        this.level.loadTextures(this.textureAtlas!!);

        val loadingTextureRegions = TextureRegion.split(Texture(Gdx.files.internal("loading_x32.png")), 32, 32);
        var k = 0;
        for (i in 0 until loadingTextureRegions.size) {
            for (j in 0 until loadingTextureRegions[i].size) {

                this.loadingFrames[k] = loadingTextureRegions[i][j]
                k++;
            }
        }

        this.loadingAnimation = AnimationController(4, this.loadingFrames.size);

    }

    override fun show() {

    }

    fun update(delta: Float) {
        when (this.gameStates) {
            GameStates.LOADING_SCREEN -> {
                if (this.loadingAnimation!!.pause) {
                    if (this.loadingTransition.update()) {
                        this.gameStates = GameStates.MENU;
                    }
                } else {
                    this.loadingAnimation?.update(delta) {
                        return@update true
                    };
                }
            }
            GameStates.MENU -> {
                if (this.menu.updateState(delta, this.camera) == Menu.MenuState.START_GAME) {
                    this.gameStates = GameStates.LOADING_LEVEL
                }
            }
            GameStates.LOADING_LEVEL -> {
                if (this.level.loadLevel(1)) {
                    this.gameStates = GameStates.PLAYING_GAME;
                } else {
                    this.gameStates = GameStates.MENU;
                }
            }
            GameStates.PLAYING_GAME -> {
                this.level.update(delta, this.camera);
            }
        }
    }

    override fun render(delta: Float) {
        this.update(delta);

        batch.projectionMatrix = camera.combined;
        this.batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        when (this.gameStates) {
            GameStates.LOADING_SCREEN -> {
                val loadingTexture = this.loadingFrames[this.loadingAnimation?.getIndex()!!];
                val w = width_pixel(128 * 4);
                val h = height_pixel(128 * 4);

                this.batch.draw(loadingTexture, (160f - w) / 2, (160f - h) / 2, w, h);
            }
            GameStates.MENU -> {
                this.menu.render(batch);

            }
            GameStates.PLAYING_GAME -> {


                this.level.render(this.batch);
            }
            GameStates.PAUSE_GAME -> {
                this.level.render(this.batch, true);
            }
        }

        this.batch.end();

        if (this.gameStates == GameStates.PLAYING_GAME) {
            this.hudBatch.begin();
            val fps = "%d".format((1 / delta).toInt());
            this.font?.draw(this.hudBatch, fps, 0f, 160f);
            this.font?.draw(this.hudBatch, "FPS", 10f + fps.length, 160f);

            this.font?.data?.setScale(0.08f);
            this.font?.draw(
                this.hudBatch,
                "(${this.level.player.position.x};${this.level.player.position.y})",
                0f,
                150f
            );
            this.font?.draw(
                this.hudBatch,
                "(${this.camera.position.x - camera_startvalue()};${this.camera.position.y - camera_startvalue()})",
                0f,
                140f
            );
            this.font?.data?.setScale(default_fontscale());
            this.hudBatch.end();
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);

        hudviewport.update(width, height, true)
        hudBatch.projectionMatrix = hudcamera.combined;
    }

    override fun pause() {
        if (this.gameStates == GameStates.PLAYING_GAME) {
            this.gameStates = GameStates.PAUSE_GAME;
        }

        if (this.gameStates == GameStates.MENU && this.menu.menuState == Menu.MenuState.MENU_DEFAULT) {
            this.menu.menuState = Menu.MenuState.MENU_PAUSE
        }
    }

    override fun resume() {
        if (this.gameStates == GameStates.PAUSE_GAME) {
            this.gameStates = GameStates.PAUSE_GAME;
        }

        if (this.gameStates == GameStates.MENU && this.menu.menuState == Menu.MenuState.MENU_PAUSE) {
            this.menu.menuState = Menu.MenuState.MENU_DEFAULT
        }
    }

    override fun hide() {
    }

    override fun dispose() {
        this.batch.dispose();
        font?.dispose();
    }
}