package com.julesg10.myfortress.hudobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Align
import com.julesg10.myfortress.AnimationController
import com.julesg10.myfortress.GameScreen
import com.julesg10.myfortress.InputController
import com.julesg10.myfortress.gameobjects.GameObj


class Button(position: Vector2,
             size: Vector2,
             textDefault: String,
             textActive:String,
             default: List<TextureRegion>,
             active: List<TextureRegion>,
             font: BitmapFont?,
             fontSize:Float = 0.08f)
    : HudObj(position)
{

    private val size: Vector2 = size;
    private val tDefault: String = textDefault;
    private val tActive: String = textActive;

    private val default: List<TextureRegion> = default;
    private val active: List<TextureRegion> = active;

    private val defaultAnimation = AnimationController(10, default.size, -1);
    private val activeAnimation = AnimationController(10, active.size, -1)

    private var clickCount: Int = 0;
    private val clickController = InputController(200f, true);

    private var activeState: Boolean = false;
    private var fontScale: Float = 0f;
    private var fontSize: Float = fontSize;
    private var yText: Float = 0f;

    var eventClick = false;

    companion object {
        fun getSize(text: String, font: BitmapFont?): Vector2 {
            if (font != null) {
                return Vector2((text.length + 20f) * 2f, (font.capHeight + 5f) * 2)
            }

            return Vector2(text.length + 20f * 2f, 25f);
        }

        fun center(size: Vector2): Vector2 {
            return Vector2(centerX(size.x), centerY(size.y));
        }

        fun centerX(width: Float): Float {
            return (GameScreen.world_width() / 2) - width
        }

        fun centerY(height: Float): Float {
            return (GameScreen.world_height() / 2) - height
        }
    }



    init {
        this.font = font;
        this.fontScale = this.font?.data?.scaleX!!;

        this.font?.data?.setScale(this.fontSize)
        this.yText = this.position.y + (this.size.y / 2) + (this.font!!.capHeight / 2);
        this.font?.data?.setScale(this.fontScale);
    }

    fun clickCount(): Int {
        return this.clickCount;
    }

    override fun render(hudBatch: Batch) {
        if (this.activeState) {
            hudBatch.draw(
                this.active[this.activeAnimation.getIndex()],
                this.position.x,
                this.position.y,
                this.size.x,
                this.size.y
            );
            HUDText(this.font,hudBatch,position = Vector2(this.position.x,this.yText),width = this.size.x,align = Align.center,str = arrayOf(this.tActive),fontSize = this.fontSize)
        } else {
            hudBatch.draw(
                this.default[this.defaultAnimation.getIndex()],
                this.position.x,
                this.position.y,
                this.size.x,
                this.size.y
            );
            HUDText(this.font,hudBatch,position = Vector2(this.position.x,this.yText),width = this.size.x,align = Align.center,str = arrayOf(this.tDefault),fontSize = this.fontSize)
        }
    }

    fun isclick(delta: Float, cameraPosition: Vector2): Boolean {
        val lastState = this.activeState;
        val rtn = this.clickController.isActive(delta) {
            if (Gdx.input.isTouched && this.click(cameraPosition)) {
                this.eventClick = true;
                this.activeState = true;
            } else {
                this.activeState = false;
            }

            return@isActive this.activeState;
        };

        if (lastState != this.activeState) {
            if (!this.activeState) {
                this.clickCount += 1;
            }

            this.activeAnimation.restart();
            this.defaultAnimation.restart();
        }

        if (this.activeState) {
            this.activeAnimation.update(delta)
            {
                return@update true;
            };
        } else {
            this.defaultAnimation.update(delta)
            {
                return@update true
            };
        }

        return rtn;
    }

    private fun click(camera: Vector2): Boolean {
        var touch =
            Vector2(GameScreen.width_pixel(Gdx.input.x), GameScreen.height_pixel(Gdx.graphics.height - Gdx.input.y));
        touch = GameScreen.camera_target(touch, Vector3(camera, 0f));

        val touchSize = Vector2(10f, 10f)

        if (this.position.x < touch.x + touchSize.x &&
            this.position.x + this.size.x > touch.x &&
            this.position.y < touch.y + touchSize.y &&
            this.position.y + this.size.y > touch.y
        ) {
            return true;
        }

        return false;
    }
}
