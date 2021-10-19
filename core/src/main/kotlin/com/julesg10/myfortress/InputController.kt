package com.julesg10.myfortress

import com.badlogic.gdx.Gdx


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
        HOVER,
        DOUBLE_CLICK
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

class DoubleController(wait: Float,wait_min: Float ,input_max: Float){
    private val itemClickController : InputController = InputController(input_max,true);

    private var waitForNextClickVal = 0f;
    private var waitForNextClick = false;
    private val waitMax = wait;
    private val waitMin = wait_min;

    fun isActive(delta: Float): InputController.InputStates
    {
        val state = this.itemClickController.isActive(delta) {
            return@isActive Gdx.input.isTouched;
        }

        if(state == InputController.InputStates.CLICK) {

            if(!this.waitForNextClick)
            {
                this.waitForNextClick = true;
            }else{
                if(this.waitForNextClickVal >= this.waitMin && this.waitForNextClickVal <= this.waitMax)
                {
                    this.waitForNextClick = false;
                    this.waitForNextClickVal = 0f;
                    return InputController.InputStates.DOUBLE_CLICK
                }
            }
        }else if(state == InputController.InputStates.NONE)
        {
            if(this.waitForNextClick) {
                this.waitForNextClickVal += delta*100;
                if(this.waitForNextClickVal >= this.waitMax)
                {
                    this.waitForNextClickVal = 0f;
                    this.waitForNextClick = false;
                }
            }
        }

        return InputController.InputStates.NONE;
    }

    fun reset() {
        this.waitForNextClick = false;
        this.waitForNextClickVal = 0f;
    }
}