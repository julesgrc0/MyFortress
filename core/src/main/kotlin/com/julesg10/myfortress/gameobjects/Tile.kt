package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Tile(position: Vector2,type : TileTypes,texture: TextureRegion) : GameObj(position) {

    private var type: TileTypes = TileTypes.FLOOR;
    private var texture: TextureRegion;
    var rotation: Float = 0f;

    companion object {
        fun tile_size() = Vector2(16f, 16f);
    }

    init {
        this.type = type
        this.texture = texture;
    }

    enum class TileTypes {
        FLOOR,
        WALL,
        DOOR,

    }

    override fun render(batch: Batch) {
        batch.draw(this.texture, this.position.x, this.position.y, 0f, 0f, tile_size().x, tile_size().y, 1f, 1f, this.rotation);
    }

    override fun update(delta: Float, camera: Camera) {

    }
}