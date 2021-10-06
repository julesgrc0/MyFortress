package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Align
import com.julesg10.myfortress.AnimationController
import com.julesg10.myfortress.GameScreen
import com.julesg10.myfortress.InputController

class Menu(position: Vector2,font: BitmapFont?) : GameObj(position) {
    private val btnList: MutableList<Pair<Button,String>> = mutableListOf();
    var menuState: MenuState = MenuState.MENU_DEFAULT;

    init{
        this.font = font;
    }

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

    fun updateState(delta: Float,camera: Camera):MenuState {
        when (this.menuState) {
            MenuState.MENU_DEFAULT -> {
                for (pBtn in this.btnList) {
                    if (pBtn.first.isclick(delta,camera) && pBtn.first.eventClick) {
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
