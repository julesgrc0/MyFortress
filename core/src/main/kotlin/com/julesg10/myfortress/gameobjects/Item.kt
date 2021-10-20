package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.Vector2
import com.julesg10.myfortress.GameScreen
import kotlin.math.tan

class Item(position: Vector2,type: ItemTypes,texture: TextureRegion, length: Int) : GameObj(position) {
    val type: ItemTypes = type;
    private var texture: TextureRegion = texture;
    private var textureDirection: Boolean = true;
    private val length = length;
    private var size: Vector2;


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

    private fun flip(left: Boolean)
    {
        if(this.textureDirection != left)
        {
            texture.flip(true,false);
            this.textureDirection = left;
        }
    }

    fun renderOnPlayer(batch: Batch,position: Vector2,left: Boolean)
    {
        this.flip(left);
        var angle = 45f
        var y = (position.y - this.size.y * (this.length-1)) - (this.size.y/4) - 2f
        var x = position.x - 2f

        if(left)
        {
            x = (position.x - 4f) + this.size.x;
            y =  (position.y - this.size.y * (this.length-1)) - (this.size.y/4) + this.size.y/2
            angle = -45f
        }

        batch.draw(texture, x , y, 0f, 0f, this.size.x,this.size.y * this.length, 1f, 1f, angle);

    }

    override fun render(batch: Batch) {

        val x = this.position.x
        val y = this.position.y

        batch.draw(texture, x , y , 0f, 0f, this.size.x,this.size.y*this.length, 1f, 1f, 0f);
    }

    override fun update(delta: Float, camera: Camera) {

    }
}