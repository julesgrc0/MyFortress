package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.julesg10.myfortress.AnimationController
import com.julesg10.myfortress.GameScreen
import com.julesg10.myfortress.InputController

class Menu(position: Vector2,font: BitmapFont?) : GameObj(position) {
    var font: BitmapFont? = font;
    private val btnList: MutableList<Pair<Button,String>> = mutableListOf();
    var menuState: MenuState = MenuState.MENU_DEFAULT;

    enum class MenuState{
        START_GAME,
        MENU_DEFAULT,
        MENU_PAUSE
    }

    fun loadTextures()
    {
        val btnTextures = TextureRegion.split(Texture(Gdx.files.internal("btn.png")), 100, 50);
        val btnTexturesList : MutableList<TextureRegion> = mutableListOf();


        for (i in 0 until btnTextures.size) {
            for (j in 0 until btnTextures[i].size) {
                btnTexturesList.add(btnTextures[i][j])
            }
        }
        btnTexturesList.removeLast();
        val activeBtnTextures = mutableListOf<TextureRegion>()
        activeBtnTextures.addAll(btnTexturesList)

        this.btnList.add(
            Pair(
            Button(Vector2(60f,10f),
                Button.getSize("Start",this.font),
                "Start",
                "GO",
                btnTexturesList,
                activeBtnTextures.subList(1,btnTexturesList.size).toList(),
                font),
            "start_btn")
        );
    }

    override fun render(batch: Batch) {
        when(this.menuState)
        {
            MenuState.MENU_DEFAULT->{
                for(pBtn in this.btnList)
                {
                    pBtn.first.render(batch);
                }
            }
            MenuState.MENU_PAUSE->{

            }
        }
    }

    fun updateState(delta: Float):MenuState {
        when (this.menuState) {
            MenuState.MENU_DEFAULT -> {
                for (pBtn in this.btnList) {
                    if (pBtn.first.isclick(delta) && pBtn.first.eventClick) {
                        pBtn.first.eventClick = false;
                        when (pBtn.second) {
                            "start_btn" -> {
                                println(pBtn.first.clickCount())
                                this.menuState = MenuState.START_GAME;
                            }
                        }
                    }
                }
            }
            MenuState.MENU_PAUSE->{

            }
        }

        return this.menuState;
    }
}

class Button(position: Vector2,size: Vector2,textDefault: String,textActive:String,default: List<TextureRegion>,active: List<TextureRegion>,font: BitmapFont?,fontSize:Float = 0.08f) : GameObj(position) {
    private val size: Vector2 = size;
    private val tDefault: String = textDefault;
    private val tActive: String = textActive;

    private val default: List<TextureRegion> = default;
    private val active: List<TextureRegion> = active;
    private val font: BitmapFont? = font;

    private val defaultAnimation = AnimationController(10,default.size,-1);
    private val activeAnimation = AnimationController(10,active.size,-1)

    private var clickCount: Int =0;
    private val clickController = InputController(200f,true);

    private var activeState: Boolean = false;
    private var fontScale: Float = 0f;
    private var fontSize: Float = fontSize;
    private var yText: Float = 0f;

    companion object
    {
        fun getSize(text: String,font: BitmapFont?): Vector2
        {
            if (font != null) {
                return Vector2((text.length + 20f) * 2f,(font.capHeight+5f)*2)
            }

            return Vector2(text.length + 20f * 2f,25f);
        }

        fun center(size: Vector2): Vector2
        {
            return Vector2(centerX(size.x), centerY(size.y));
        }

        fun centerX(width: Float):Float
        {
            return (GameScreen.world_width()/2) - width
        }

        fun centerY(height: Float):Float
        {
            return (GameScreen.world_height()/2) - height
        }
    }

    var eventClick = false;
    init{
        this.fontScale = this.font?.data?.scaleX!!;

        this.font?.data?.setScale(this.fontSize)
        this.yText = this.position.y+(this.size.y/2)+(this.font.capHeight/2);
        this.font?.data?.setScale(this.fontScale);
    }

    fun clickCount(): Int
    {
        return this.clickCount;
    }

    override fun render(batch: Batch) {
        this.font?.data?.setScale(this.fontSize)

        if(this.activeState)
        {
            batch.draw(this.active[this.activeAnimation.getIndex()],this.position.x,this.position.y,this.size.x,this.size.y);
            font?.draw(batch,this.tActive,this.position.x,this.yText,this.size.x,Align.center,false);
        }else{
            batch.draw(this.default[this.defaultAnimation.getIndex()],this.position.x,this.position.y,this.size.x,this.size.y);
            font?.draw(batch,this.tDefault,this.position.x,this.yText,this.size.x,Align.center,false);
        }
        this.font?.data?.setScale(this.fontScale);
    }

    fun isclick(delta: Float):Boolean {
        val lastState = this.activeState;
        val rtn = this.clickController.isActive(delta) {
            if (Gdx.input.isTouched && this.click()) {
                this.eventClick = true;
                this.activeState = true;
            }else{
                this.activeState = false;
            }

            return@isActive this.activeState;
        };

        if(lastState != this.activeState)
        {
            if(!this.activeState)
            {
                this.clickCount += 1;
            }

            this.activeAnimation.restart();
            this.defaultAnimation.restart();
        }

        if(this.activeState)
        {
            this.activeAnimation.update(delta)
            {
                return@update true;
            };
        }else{
            this.defaultAnimation.update(delta)
            {
                return@update true
            };
        }

        return rtn;
    }

    private fun click(): Boolean
    {
        val touch = Vector2(GameScreen.width_pixel(Gdx.input.x),GameScreen.height_pixel(Gdx.graphics.height -Gdx.input.y));
        val touchSize = Vector2(10f,10f)

        if(this.position.x < touch.x + touchSize.x &&
            this.position.x + this.size.x > touch.x &&
            this.position.y < touch.y + touchSize.y &&
            this.position.y + this.size.y > touch.y)
        {
            return true;
        }

        return false;
    }
}
