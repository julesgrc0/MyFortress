package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2

open class GameObj(position : Vector2) {

    var position : Vector2 = Vector2(0f,0f);

    init {

        this.position = position;
    }

    open fun render(batch: Batch)
    {

    }

    open fun update(delta: Float)
    {

    }
}