package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.math.roundToInt

class Player(position: Vector2): GameObj(position) {

    var speed: Float = 8f;
    val size = Tile.tile_size();
    var textures = mutableListOf<TextureRegion>();
    var direction: Direction = Direction.LEFT
    private var textureDirection: Direction = Direction.LEFT;


    fun move(position: Vector2, camera: Camera) {
        this.position = position;
        val x = (camera.viewportWidth - this.size.x) / 2;
        val y = (camera.viewportHeight - this.size.y) / 2;

        camera.position.set(camera.position.x + x, camera.position.y + y, camera.position.z);
        camera.update();

    }

    override fun render(batch: Batch) {
        var i = this.textures.size;

        val x = (this.position.x/Tile.tile_size().x).roundToInt()*Tile.tile_size().x;
        val y = (this.position.y/Tile.tile_size().y).roundToInt()*Tile.tile_size().y;

        for (texture in textures)
        {
            if(this.direction == Direction.LEFT && this.textureDirection != this.direction)
            {
                texture.flip(true,false);
                this.textureDirection = this.direction;
            }

           else if(this.direction == Direction.RIGHT && this.textureDirection != this.direction)
            {
                texture.flip(true,false);
                this.textureDirection = this.direction;
            }

            i --;
            batch.draw(texture, x , y - (i * Tile.tile_size().y), 0f, 0f, Tile.tile_size().x, Tile.tile_size().y, 1f, 1f, 0f);
        }
    }

    fun update(delta: Float, camera: Camera, tiles: MutableList<Tile>): MutableList<Tile> {
        return tiles;
    }
}