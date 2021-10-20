package com.julesg10.myfortress.hudobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Align
import com.julesg10.myfortress.AnimationController
import com.julesg10.myfortress.GameScreen
import com.julesg10.myfortress.InputController


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
    private val hoverAnimation = AnimationController(10, active.size, -1)

    private var clickCount: Int = 0;
    private val clickController = InputController(0f, true);

    private var hoverState: Boolean = false;

    private var fontScale: Float = 0f;
    private var fontSize: Float = fontSize;
    private var yText: Float = 0f;

    var eventClick = false;

    companion object {
        fun positionManagement(position: Vector2, size: Vector2, sizeX: Int = 0, sizeY: Int = 0): Vector2 {
            if (sizeX == 1) {
                position.x -= size.x / 2
            } else if (sizeX == 2) {
                position.x += size.x / 2
            }
            if (sizeY == 1) {
                position.y -= size.y / 2
            } else if (sizeY == 2) {
                position.y += size.y / 2
            }
            return position
        }

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
            return (GameScreen.world_size().x / 2) - width
        }

        fun centerY(height: Float): Float {
            return (GameScreen.world_size().y / 2) - height
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
        if (this.hoverState) {
            hudBatch.draw(
                this.active[this.hoverAnimation.getIndex()],
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

    private var lastTouch = false;

    fun isclick(delta: Float, cameraPosition: Vector2): Boolean {

        val rtn = this.clickController.isActive(delta) {

            if (Gdx.input.isTouched) {
                if(!this.lastTouch)
                {
                    if(this.click(cameraPosition))
                    {
                        return@isActive true;
                    }
                    this.lastTouch = true;
                }
            }else{
                this.lastTouch = false;
            }

            return@isActive false;
        };

        if(rtn == InputController.InputStates.CLICK)
        {
            this.eventClick = true;
        }else if(rtn == InputController.InputStates.NONE)
        {
            this.hoverState = false;
        }else if(rtn == InputController.InputStates.HOVER)
        {
            this.hoverState = true;
        }

        if (this.hoverState) {
            this.hoverAnimation.update(delta)
            {
                return@update true;
            };
        } else {
            this.defaultAnimation.update(delta)
            {
                return@update true
            };
        }

        return rtn == InputController.InputStates.CLICK;
    }

    private fun click(camera: Vector2): Boolean {
        var touch = GameScreen.pixel_to_worldValue(Vector2(Gdx.input.x.toFloat(), (Gdx.graphics.height - Gdx.input.y).toFloat()))
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
