package com.julesg10.myfortress




class InputController(max: Float,stop: Boolean) {
    var max: Float = 0f
    private var time: Float = 0f;
    private var stop = stop;
    private var waitStop = false;
    private var startState :Boolean = true;

    init {
        this.max = max;
    }

    enum class InputStates{
        NONE,
        CLICK,
        HOVER
    }

    fun isActive(delta: Float, input:() -> Boolean): InputStates
    {
        val inp = input();
        if(this.startState && inp)
        {
            this.waitStop = true;
            this.startState = false;
        }

        if(inp)
        {
            this.time += delta * 1000;
            if(this.time >= this.max)
            {
                this.time = 0f;
                if(this.stop)
                {
                    if(this.waitStop)
                    {
                        return InputStates.HOVER;
                    }
                    this.waitStop = true;
                }
                return InputStates.CLICK;
            }

            return InputStates.HOVER;
        }else{
            this.waitStop = false;
            this.time = 0f;
            return InputStates.NONE;
        }
    }
}