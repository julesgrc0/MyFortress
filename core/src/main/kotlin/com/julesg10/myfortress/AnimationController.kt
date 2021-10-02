package com.julesg10.myfortress

class DelayController(max: Float) {

    private var time: Float = 0f
    private var max: Float = 0f

    init {
        this.max = max;
    }

    fun getTime() : Float{
        return this.time;
    }

    fun update(delta: Float,change:()->Void): Boolean
    {
        this.time += delta * 100
        if(this.time >= this.max)
        {
            this.time =  0f;
            change()
            return true;
        }

        return false;
    }
}