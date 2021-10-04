package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Player(position: Vector2): GameObj(position) {

    val size = Tile.tile_size();

    fun move(position: Vector2,batch:Batch,camera: Camera)
    {
        this.position = position;
        val x = (camera.viewportWidth - this.size.x)/2;
        val y = (camera.viewportHeight - this.size.y)/2;

        camera.position.set(x,y,0f);
        camera.update();

        batch.projectionMatrix = camera.combined;
    }

    override fun render(batch: Batch)
    {

    }

    fun update(delta: Float,tiles: MutableList<Tile>): MutableList<Tile>
    {
        return tiles;
    }
}