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

    var position: Vector2 = Vector2(0f, 0f);
    var font: BitmapFont? = null

    init {

        this.position = position;
    }

    open fun text(batch: Batch, text: String, position: Vector2, camera: Camera? = null, hud: Boolean = false) {
        if (camera != null && hud) {
            val static =
                GameScreen.camera_target_static(position, Vector3(camera.position.x ?: 0f, camera.position.y ?: 0f, 0f))
            this.font?.draw(batch, text, static.x, static.y);
        } else {

            this.font?.draw(batch, text, position.x, position.y);
        }
    }


    open fun render(batch: Batch) {

    }

    open fun update(delta: Float, camera: Camera) {

    }
}