package com.julesg10.myfortress

class InputManager(max: Float,stop: Boolean) {
    var max: Float = 0f
    private var time: Float = 0f;

    private var stop: Boolean = false;
    private var switchStop: Boolean = false;

    init {
        this.max = max;
        this.stop = stop;
    }

    fun setStop(stop: Boolean)
    {
        this.stop = stop;
        this.switchStop = false;
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
                    if(this.switchStop)
                    {
                        return false;
                    }
                    this.switchStop = false;
                }
            }

            return true;
        }else{
            this.switchStop = true;
        }

        return false;
    }
}