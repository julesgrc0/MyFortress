package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.math.roundToInt

class Player(position: Vector2): GameObj(position) {

    var speed: Float = 40f;
    var textures = mutableListOf<TextureRegion>();
    var direction: Direction = Direction.LEFT
    private var textureDirection: Direction = Direction.LEFT;
    val requestPosition = position;

    private fun flip()
    {
        if(this.textureDirection != this.direction)
        {
            for(texture in textures)
            {
                texture.flip(true,false);
            }
            this.textureDirection = this.direction;
        }
    }

    override fun render(batch: Batch) {
        var i = this.textures.size;

        /*
        val x = (this.position.x/Tile.tile_size().x).roundToInt()*Tile.tile_size().x;
        val y = (this.position.y/Tile.tile_size().y).roundToInt()*Tile.tile_size().y;
        */
        val x = this.position.x
        val y = this.position.y

        for (texture in textures)
        {
            this.flip()

            i --;
            batch.draw(texture, x , y - (i * Tile.tile_size().y), 0f, 0f, Tile.tile_size().x, Tile.tile_size().y, 1f, 1f, 0f);
        }
    }

    fun update(delta: Float, camera: Camera, tiles: MutableList<Tile>): MutableList<Tile> {
        if(this.requestPosition != this.position)
        {
            val margin = 1f;
            val move =  delta * this.speed;
            if(this.requestPosition.x > this.position.x + margin)
            {
                this.position.x += move
            }else if(this.requestPosition.x + margin < this.position.x)
            {
                this.position.x -= move
            }else{
                this.position.x = this.requestPosition.x;
            }

            if(this.requestPosition.y > this.position.y + margin)
            {
                this.position.y += move
            }else if(this.requestPosition.y + margin < this.position.y)
            {
                this.position.y -= move
            }else{
                this.position.y = this.requestPosition.y;
            }
        }
        return tiles;
    }
}