package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.julesg10.myfortress.GameScreen

class Item(position: Vector2,type: ItemTypes,texture: TextureRegion) : GameObj(position) {
    private var type: ItemTypes = ItemTypes.FIX;
    private var texture: TextureRegion;
    private var size: Vector2;
    var rotation: Float = 0f;


    init {
        this.type = type
        this.texture = texture;
        this.size = Vector2(GameScreen.width_pixel(texture.regionWidth),GameScreen.height_pixel(texture.regionHeight))
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
        batch.draw(this.texture, this.position.x, this.position.y, 0f, 0f, this.size.x,this.size.y, 1f, 1f, this.rotation);
    }

    override fun update(delta: Float, camera: Camera) {

    }
}