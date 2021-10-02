package com.julesg10.myfortress

class AnimationController(max: Float) {

    private var time: Float = 0f
    private var max: Float = 0f

    init {
        this.max = max;
    }

    fun update(delta: Float,animation:()->Boolean): Boolean
    {
        this.time += delta * 100
        if(this.time >= this.max)
        {
            this.time =  0f;
            return animation();
        }

        return false;
    }
}