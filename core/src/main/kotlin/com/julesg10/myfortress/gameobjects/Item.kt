package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.Vector2
import com.julesg10.myfortress.GameScreen
import kotlin.math.tan

class Item(position: Vector2,type: ItemTypes,textures: List<TextureRegion>) : GameObj(position) {
    val type: ItemTypes = type;
    private var textures: List<TextureRegion> = textures;
    private var textureDirection: Boolean = true;
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

    private fun flip(left: Boolean)
    {
        if(this.textureDirection != left)
        {
            for(texture in textures)
            {
                texture.flip(true,false);
            }
            this.textureDirection = left;
        }
    }

    fun renderOnPlayer(batch: Batch,position: Vector2,left: Boolean)
    {
        this.flip(left);
        val angle = if(left) 315f else 45f
        var i = this.textures.size;
        val y = position.y
        val x = position.x //+ (if(left) 11.5f else -8f)

        for (texture in textures)
        {
            i --;
            //batch.draw(texture, x + (this.size.x/sin(angle))*i, y+ (this.size.y/sin(angle))*i, 0f, 0f, this.size.x,this.size.y, 1f, 1f, angle);
        }

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