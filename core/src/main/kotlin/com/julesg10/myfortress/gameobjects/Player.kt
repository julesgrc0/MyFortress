package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.julesg10.myfortress.InputController
import kotlin.math.abs
import kotlin.math.roundToInt

class Player(position: Vector2): GameObj(position) {

    var speed: Float = 40f;
    var textures = mutableListOf<TextureRegion>();
    var direction: Direction = Direction.LEFT
    private var textureDirection: Direction = Direction.LEFT;
    private val playerController : InputController = InputController(100f,true);
    val requestPosition = position;

    private fun playerCamera(delta: Float,camera: Camera)
    {
        val state = this.playerController.isActive(delta) {
            return@isActive Gdx.input.isTouched;
        }
        if(state == InputController.InputStates.CLICK)
        {

            if (Gdx.input.isTouched) {
                val minValue = 2;

                val inpDX = Gdx.input.deltaX
                val inpDY = Gdx.input.deltaY

                var deltaX = inpDX
                var deltaY = inpDY

                if(abs(deltaX) >= minValue)
                {
                    deltaX = if(deltaX < 0) Tile.tile_size().x.toInt() else -Tile.tile_size().x.toInt();
                }

                if(abs(deltaY) > minValue)
                {
                    deltaY = if(deltaY > 0) Tile.tile_size().y.toInt() else -Tile.tile_size().y.toInt();
                }


                if(abs(inpDX) > abs(inpDY))
                {
                    if(deltaX > 0)
                    {
                        this.direction = Direction.LEFT
                    }else{
                        this.direction = Direction.RIGHT
                    }
                    this.requestPosition.x += deltaX
                }else{
                    this.requestPosition.y += deltaY
                }
            }



        }

        camera.position.set(Vector3( this.position.x,this.position.y , camera.position.z));
        camera.update()
    }

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


    fun update(delta: Float, camera: Camera, tiles: MutableList<Tile>, items: MutableList<Item>) {
        this.playerCamera(delta,camera);

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

    }
}