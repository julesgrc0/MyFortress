package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.julesg10.myfortress.GameScreen

open class HudObj(position: Vector2) : GameObj(position) {

    fun text(batch: Batch, text: String, position: Vector2) {
        this.font?.draw(batch, text, position.x, position.y);
    }

    fun HUDtext(hudBatch: Batch,space: Float=0.3f,Xdirection:Boolean = true,fontSize: Float = -1f,vararg str:String)
    {
        var oldSize = this.font?.data?.scaleX!!;
        if(fontSize > 0)
        {
            this.font!!.data.setScale(fontSize)
        }
        
        var i:Float = 0.0f;
        for(s in str)
        {
            if(Xdirection)
            {
                this.text(hudBatch,s,Vector2(position.x+i,position.y))
            }else{
                this.text(hudBatch,s,Vector2(position.x,position.y+i))
            }
            i+=space;
        }

        this.font!!.data.setScale(oldSize)
    }
}