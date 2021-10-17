package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.julesg10.myfortress.GameScreen

class Tile(position: Vector2,type : TileTypes,texture: TextureRegion) : GameObj(position) {

    val type: TileTypes = type;
    private var texture: TextureRegion;
    var rotation: Float = 0f;

    companion object {
        fun tile_size() = GameScreen.percent_to_worldValue(Vector2(10f,10f));
    }

    init {
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