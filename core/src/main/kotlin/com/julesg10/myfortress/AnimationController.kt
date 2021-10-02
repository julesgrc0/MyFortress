package com.julesg10.myfortress

import com.badlogic.gdx.graphics.g2d.TextureRegion


class AnimationTimer(ms: Float){
    private var time: Float = 0f
    private var waitvalue: Float = 0f
    private var stop = false;

    init {
        this.waitvalue = ms/10;
    }

    fun update(delta: Float): Boolean
    {
        if(this.stop)
        {
            return true;
        }

        this.time += delta * 1000;
        if(this.time >= this.waitvalue)
        {
            this.stop = true;
            return true;
        }

        return false;
    }

    fun restart()
    {
        this.time = 0f;
        this.stop = false;
    }
}

class AnimationController(framesPerSeconde: Int,framesLength: Int,loop : Int=0) {

    private var time: Float = 0f
    private var loop: Int = 0
    private var index: Int = 0

    private var max: Float = 0f
    private var max_index: Int = 0;
    private var max_loop: Int = 0;

    var pause: Boolean = false;


    init {
        this.max = 600f/framesPerSeconde;
        this.max_index = framesLength;
        this.max_loop = loop;
    }

    fun getTime() : Float{
        return this.time;
    }

    fun getIndex() : Int
    {
        return this.index;
    }

    fun getLoop(): Int
    {
        return this.loop;
    }

    fun update(delta: Float,change:()->Boolean): Boolean
    {
        if(this.pause)
        {
            return false;
        }

        this.time += delta * 1000
        if(this.time >= this.max)
        {
            this.time =  0f;


            if(this.index+1 >= this.max_index)
            {
                this.index = 0;
                this.loop++;
                if(this.loop >= this.max_loop)
                {
                    this.loop = 0;
                    this.index = this.max_index-1;
                    this.pause = true;
                }
            }else{
                this.index++;
            }

            return change();
        }

        return false;
    }
}