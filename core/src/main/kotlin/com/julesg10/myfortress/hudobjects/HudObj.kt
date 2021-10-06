package com.julesg10.myfortress.hudobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Align
import com.julesg10.myfortress.GameScreen
import com.julesg10.myfortress.gameobjects.GameObj

open class HudObj(position: Vector2) : GameObj(position) {

    companion object {

        fun text(font: BitmapFont?,batch: Batch, text: String, position: Vector2, width: Float, align: Int = Align.center, wrap: Boolean = false) {
            font?.draw(batch, text, position.x, position.y,width,align,wrap);
        }

        fun HUDText(font: BitmapFont?,hudBatch: Batch,space: Float=5f,Xdirection:Boolean = true,fontSize: Float = GameScreen.default_fontscale(), position: Vector2, width: Float,align: Int = Align.left, wrap: Boolean = false,vararg str:String)
        {
            font?.data?.setScale(fontSize)

            var i:Float = 0.0f;
            for(s in str)
            {
                if(Xdirection)
                {
                    text(font,hudBatch,s,Vector2(position.x+i,position.y),width,align,wrap)
                }else{
                    text(font,hudBatch,s,Vector2(position.x,position.y+i),width,align,wrap)
                }
                i += s.length * space;
            }

            font!!.data.setScale(GameScreen.default_fontscale())
        }
    }

    override fun render(hudBatch: Batch) {

    }

}