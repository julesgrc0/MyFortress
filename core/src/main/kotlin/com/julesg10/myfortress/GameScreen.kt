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
import com.julesg10.myfortress.gameobjects.Direction
import com.julesg10.myfortress.gameobjects.Level
import com.julesg10.myfortress.gameobjects.Tile
import com.julesg10.myfortress.hudobjects.HudObj
import com.julesg10.myfortress.hudobjects.Menu
import kotlin.math.abs
import kotlin.math.roundToInt


class GameScreen : Screen {


    private val camera: Camera = OrthographicCamera()
    private val viewport = StretchViewport(world_width(), world_height(), camera)
    private val batch = SpriteBatch()

    private val hudcamera: Camera = OrthographicCamera()
    private val hudviewport = StretchViewport(world_width(), world_height(), hudcamera)
    private val hudBatch = SpriteBatch();

    private var font: BitmapFont? = null

    private val level = Level();
    private var menu: Menu = Menu(Vector2(0f, 0f), font);
    private var gameStates: GameStates = GameStates.LOADING_SCREEN;

    private var textureAtlas: TextureAtlas? = null
    private var fortressTextures: Array<Array<TextureRegion>>? = null;

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
            return (pixel * world_height() / Gdx.graphics.height)
        }

        fun height_world(pixel: Float): Int {
            return (pixel * Gdx.graphics.height / world_height()).toInt()
        }

        fun width_pixel(pixel: Int): Float {
            return (pixel * world_width() / Gdx.graphics.width)
        }

        fun width_world(pixel: Float): Int {
            return (pixel * Gdx.graphics.width / world_width()).toInt()
        }

        fun size_world(pixel: Vector2): Vector2
        {
            return Vector2(width_pixel(pixel.x.toInt()), height_pixel(pixel.y.toInt()))
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

        fun touchObject(
            objPosition: Vector2,
            objSize: Vector2,
            touchPosition: Vector2,
            touchSize: Vector2,
            cameraPosition: Vector3,
            roundToObj: Boolean
        ): Boolean {
            var roundX: Float = (width_pixel(touchPosition.x.toInt()) / objSize.x).roundToInt() * objSize.x;
            var roundY: Float =
                ((world_height() - height_pixel(touchPosition.y.toInt())) / objSize.y).roundToInt() * objSize.y;

            if (!roundToObj) {
                roundX = width_pixel(touchPosition.x.toInt())
                roundY = world_height() - height_pixel(touchPosition.y.toInt())
            }

            val obj = Vector2((objPosition.x + cameraPosition.x) - 80f, (objPosition.y + cameraPosition.y) - 80f);

            val touch = size_world(camera_target(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()),cameraPosition))
            touch.x -= cameraPosition.x
            touch.y -= cameraPosition.y

            obj.x = obj.x.roundToInt().toFloat()
            obj.y = obj.x.roundToInt().toFloat()

            touch.x = touch.x.roundToInt().toFloat()
            touch.y = touch.x.roundToInt().toFloat()

            println("${obj.x} == ${touch.x} && ${obj.y} == ${touch.y}")

            if (obj.x < touch.x + objSize.x &&
                obj.x + objSize.x > touch.x &&
                obj.y < touch.y + objSize.y &&
                obj.y + objSize.y > touch.y
            ) {
                return true;
            }

            return false;
        }

    }

    init {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);


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
        fontParameter.borderWidth = 8f//3.6f
        fontParameter.borderGamma = 0.5f
        fontParameter.color = Color(1f, 1f, 1f, 1f)
        fontParameter.borderColor = Color(0f, 0f, 0f, 0f)

        font = fontGenerator.generateFont(fontParameter)
        font?.setUseIntegerPositions(false)
        font?.data?.setScale(default_fontscale())

    }

    fun loadTextures() {
        this.textureAtlas = TextureAtlas(Gdx.files.internal("sprites.atlas"));


        this.fortressTextures = TextureRegion.split(Texture(Gdx.files.internal("fortress.png")), 16, 16);
        this.level.loadTextures(this.fortressTextures);

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
                //this.debugCamera(delta)
                this.playerCamera(delta);
                this.level.update(delta, this.camera);
            }
        }
    }

    private val playerController : InputController = InputController(100f,true);

    fun playerCamera(delta: Float)
    {
        val state = this.playerController.isActive(delta) {
            return@isActive Gdx.input.isTouched;
        }
        if(state == InputController.InputStates.HOVER)
        {
        val speed = delta * this.level.player.speed
        if (Gdx.input.isTouched) {
            val maxMove = 20;
            val replaceMoveValue = 15;

            var deltaX = Gdx.input.deltaX
            var deltaY = Gdx.input.deltaY

            if(abs(deltaX) >= maxMove)
            {
                    if(deltaX > 0)
                    {
                        deltaX = replaceMoveValue;
                    }else{
                        deltaX = -replaceMoveValue;
                    }
            }

            if(abs(deltaY) >= maxMove)
            {
                if(deltaY > 0)
                {
                    deltaY = replaceMoveValue;
                }else{
                    deltaY = -replaceMoveValue;
                }
            }



            if(abs(deltaX) > abs(deltaY))
            {
                if(deltaX < 0)
                {
                    this.level.player.direction = Direction.LEFT
                }else{
                    this.level.player.direction = Direction.RIGHT
                }
                this.level.player.position.x -= (deltaX * speed)
            }else{
                this.level.player.position.y += (deltaY * speed)
            }
        }

        /*val deltaXCam = (this.camera.position.x-80f) - (this.level.player.position.x/Tile.tile_size().x).roundToInt()*Tile.tile_size().x;
        val deltaYCam = (this.camera.position.y-80f) - (this.level.player.position.y/Tile.tile_size().y).roundToInt()*Tile.tile_size().y;
        */

        this.camera.position.set(Vector3( this.level.player.position.x,this.level.player.position.y , this.camera.position.z));
        this.camera.update()
        }
    }

    fun debugCamera(delta: Float) {
        if (Gdx.input.isTouched) {
            val speed = delta * 10
            val move = Vector2(
                this.camera.position.x - (Gdx.input.deltaX * speed),
                this.camera.position.y + (Gdx.input.deltaY * speed)
            );

            this.camera.position.set(Vector3(move, this.camera.position.z));
            this.camera.update()

            val isTouch = touchObject(
                Vector2(0f, 0f),
                Tile.tile_size(),
                Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()),
                Tile.tile_size(),
                this.camera.position,
                false
            )

            println("$isTouch")
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
            GameStates.PLAYING_GAME -> {
                this.level.render(this.batch);
            }
            GameStates.PAUSE_GAME -> {

                this.level.render(this.batch, true);
            }
        }
        this.batch.end();

        this.hudBatch.begin();
        when (this.gameStates) {
            GameStates.MENU -> {
                this.menu.render(this.hudBatch);
            }
            GameStates.PLAYING_GAME -> {
                val fps = "%d".format((1 / delta).toInt());
                HudObj.HUDText(
                    this.font,
                    this.hudBatch,
                    fontSize = 0.08f,
                    position = Vector2(0f, 160f),
                    width = 0f,
                    str = arrayOf(fps, "FPS")
                )
                HudObj.HUDText(
                    this.font,
                    this.hudBatch,
                    fontSize = 0.08f,
                    position = Vector2(0f, 150f),
                    width = 0f,
                    str = arrayOf("player:", "(${this.level.player.position.x.roundToInt()};${this.level.player.position.y.roundToInt()})")
                )
                HudObj.HUDText(
                    this.font,
                    this.hudBatch,
                    fontSize = 0.08f,
                    position = Vector2(0f, 140f),
                    width = 0f,
                    str = arrayOf(
                        "camera:",
                        "(${this.camera.position.x.roundToInt() - camera_startvalue()};${this.camera.position.y.roundToInt() - camera_startvalue()})",
                    )
                )
            }
        }
        this.hudBatch.end();
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
            this.gameStates = GameStates.PLAYING_GAME;
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