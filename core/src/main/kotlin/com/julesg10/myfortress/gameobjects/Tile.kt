package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

class Tile(position: Vector2,type : TileTypes,texture: TextureRegion) : GameObj(position) {

    private var type: TileTypes = TileTypes.FLOOR;
    private var texture: TextureRegion;

    init {
        this.type = type
        this.texture = texture;
    }

    enum class TileTypes{
        FLOOR,
        WALL
    }

    override fun render(batch: Batch)
    {
        batch.draw(this.texture ,0f,0f,0f,0f,16f,16f,1f,1f,0f);
    }

    override fun update(delta: Float)
    {

    }
}