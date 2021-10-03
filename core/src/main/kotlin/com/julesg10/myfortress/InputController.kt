package com.julesg10.myfortress

class InputController(max: Float,stop: Boolean) {
    var max: Float = 0f
    private var time: Float = 0f;
    private var stop = stop;
    private var waitStop = false;
    init {
        this.max = max;
    }


    fun isActive(delta: Float, input:() -> Boolean): Boolean
    {
        if(input())
        {
            this.time += delta * 1000;
            if(this.time >= this.max)
            {
                this.time = 0f;
                if(this.stop)
                {
                    if(this.waitStop)
                    {
                        return false;
                    }
                    this.waitStop = true;
                }
                return true;
            }
        }else{
            this.waitStop = false;
            this.time = 0f;
        }

        return false;
    }
}