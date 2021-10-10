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
    private val viewport = StretchViewport(world_size().x, world_size().y, camera)
    private val batch = SpriteBatch()

    private val hudcamera: Camera = OrthographicCamera()
    private val hudviewport = StretchViewport(world_size().x, world_size().y, hudcamera)
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

    private val playerController : InputController = InputController(100f,true);

    enum class GameStates {
        LOADING_SCREEN,
        MENU,
        LOADING_LEVEL,
        PLAYING_GAME,
        PAUSE_GAME
    }

    companion object {
        fun world_size(): Vector2 = Vector2(160f,208f)

        fun percent_to_worldValue(size: Vector2): Vector2
        {
            return Vector2(size.x * world_size().x/100f,size.y * world_size().y/100f);
        }

        fun percent_to_pixel(size: Vector2): Vector2
        {
            return Vector2(size.x * Gdx.graphics.width/100f,size.y * Gdx.graphics.height/100f);
        }

        fun pixel_to_worldValue(pixel_v: Vector2): Vector2 {
            return Vector2((pixel_v.x * world_size().x / Gdx.graphics.width),(pixel_v.y *  world_size().y / Gdx.graphics.height))
        }

        fun worldValue_to_pixel(world_v: Vector2): Vector2 {
            return Vector2((world_v.x * Gdx.graphics.width/world_size().x ),(world_v.y * Gdx.graphics.height/world_size().y))
        }

        fun default_fontscale() = (world_size().x/world_size().y) / 10f;

        fun camera_center()  = Vector2(world_size().x/2,world_size().y/2);

        fun camera_target(position: Vector2, cameraPosition: Vector3): Vector2 {
            return Vector2(
                position.x + (cameraPosition.x - camera_center().x),
                position.y + (cameraPosition.y - camera_center().y)
            )
        }

        fun camera_target_static(position: Vector2, cameraPosition: Vector3): Vector2 {
            return Vector2(
                (position.x + cameraPosition.x) - camera_center().x,
                (position.y + cameraPosition.y) - camera_center().y
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

            val world_v =  pixel_to_worldValue(touchPosition)
            var tx = (cameraPosition.x - camera_center().x) + world_v.x
            var ty = (cameraPosition.y - camera_center().y) + world_v.y

            val ox = (cameraPosition.x - camera_center().x) + objPosition.x
            val oy = (cameraPosition.y - camera_center().y) + objPosition.y

            if(roundToObj)
            {
                tx  = (tx/objSize.x).roundToInt() * objSize.x
                ty  = (ty/objSize.y).roundToInt() * objSize.y
            }

            if (ox < tx + touchSize.x &&
                ox + objSize.x > tx &&
                oy < ty + touchSize.y &&
                oy + objSize.y > ty)
            {
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



    fun playerCamera(delta: Float)
    {
        val state = this.playerController.isActive(delta) {
            return@isActive Gdx.input.isTouched;
        }
        if(state == InputController.InputStates.CLICK)
        {

        if (Gdx.input.isTouched) {
            val minValue = 2;

            val inpDX = Gdx.input.deltaX
            val inpDY = Gdx.input.deltaY

            var deltaX = inpDX
            var deltaY = inpDY

            if(abs(deltaX) >= minValue)
            {
                deltaX = if(deltaX < 0) Tile.tile_size().x.toInt() else -Tile.tile_size().x.toInt();
            }

            if(abs(deltaY) > minValue)
            {
                deltaY = if(deltaY > 0) Tile.tile_size().y.toInt() else -Tile.tile_size().y.toInt();
            }


            if(abs(inpDX) > abs(inpDY))
            {
                if(deltaX > 0)
                {
                    this.level.player.direction = Direction.LEFT
                }else{
                    this.level.player.direction = Direction.RIGHT
                }
                this.level.player.requestPosition.x += deltaX
            }else{
                this.level.player.requestPosition.y += deltaY
            }
        }



        }

        this.camera.position.set(Vector3( this.level.player.position.x,this.level.player.position.y , this.camera.position.z));
        this.camera.update()
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

            if(isTouch)
            {
                println("TILE (0,0) CLICKED")
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
                val loadingSize = percent_to_worldValue(Vector2(100f,100f)); //pixel_to_worldValue(Vector2(128 * 4f,128 * 4f));

                this.batch.draw(loadingTexture, (world_size().x - loadingSize.x) / 2, (world_size().y - loadingSize.y) / 2, loadingSize.x, loadingSize.y);
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
                    position = Vector2(0f, world_size().y),
                    width = 0f,
                    str = arrayOf(fps, "FPS")
                )
                HudObj.HUDText(
                    this.font,
                    this.hudBatch,
                    fontSize = 0.08f,
                    position = Vector2(0f, world_size().y-10f),
                    width = 0f,
                    str = arrayOf("player:", "(${this.level.player.position.x.roundToInt()};${this.level.player.position.y.roundToInt()})")
                )
                HudObj.HUDText(
                    this.font,
                    this.hudBatch,
                    fontSize = 0.08f,
                    position = Vector2(0f, world_size().y - 20f),
                    width = 0f,
                    str = arrayOf(
                        "camera:",
                        "(${this.camera.position.x.roundToInt() - camera_center().x};${this.camera.position.y.roundToInt() - camera_center().y})",
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