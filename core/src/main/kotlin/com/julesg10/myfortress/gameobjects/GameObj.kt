package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.julesg10.myfortress.GameScreen
import kotlin.math.round

open class GameObj(position : Vector2) {

    var position: Vector2 = position;
    var font: BitmapFont? = null


    open fun render(batch: Batch) {

    }

    open fun update(delta: Float, camera: Camera) {

    }
}