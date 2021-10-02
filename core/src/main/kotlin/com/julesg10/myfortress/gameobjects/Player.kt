package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

class Player(position: Vector2): GameObj(position) {

    override fun render(batch: Batch)
    {

    }

    fun update(delta: Float,tiles: MutableList<Tile>): MutableList<Tile>
    {
        return tiles;
    }
}