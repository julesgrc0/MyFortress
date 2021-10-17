package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.julesg10.myfortress.GameScreen

class Item(position: Vector2,type: ItemTypes,textures: List<TextureRegion>) : GameObj(position) {
    val type: ItemTypes = type;
    private var textures: List<TextureRegion> = textures;
    private var size: Vector2;
    var rotation: Float = 0f;


    init {
       //GameScreen.pixel_to_worldValue(Vector2(texture.regionWidth.toFloat(), texture.regionHeight.toFloat()))
        this.size = Vector2(Tile.tile_size().x * 0.8f,Tile.tile_size().y* 0.8f)
    }

    enum class ItemTypes {
        COLLECT,
        FIX,
        EFFECT
    }

    fun getSize(): Vector2
    {
        return this.size;
    }

    override fun render(batch: Batch) {
        var i = this.textures.size;

        val x = this.position.x
        val y = this.position.y

        for (texture in textures)
        {
            i --;
            batch.draw(texture, x , y - (i * this.size.y), 0f, 0f, this.size.x,this.size.y, 1f, 1f, 0f);
        }
    }

    override fun update(delta: Float, camera: Camera) {

    }
}