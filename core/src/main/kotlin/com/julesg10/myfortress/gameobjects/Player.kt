package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.julesg10.myfortress.DoubleController
import com.julesg10.myfortress.InputController
import kotlin.math.abs

class Player(position: Vector2): GameObj(position) {

    var speed: Float = 40f;
    var textures = mutableListOf<TextureRegion>();
    var direction: Direction = Direction.LEFT
    private var textureDirection: Direction = Direction.LEFT;

    private val moveClickController : InputController = InputController(100f,true);
    private val itemDoubleClickController : DoubleController = DoubleController(30f,1f, 10f);
    private var showItem = false;
    private var itemIndex = 0;

    val requestPosition = position;
    private val inventory = mutableListOf<Item>();

    private fun itemAction(delta:Float,items: MutableList<Item>)
    {
        val state = this.itemDoubleClickController.isActive(delta);
        if(state == InputController.InputStates.DOUBLE_CLICK)
        {
            if(this.showItem)
            {
                this.itemIndex++;
            }else{
                this.showItem = true;
            }

            if(this.itemIndex >= this.inventory.size)
            {
                this.showItem = !this.showItem;
                this.itemIndex = 0;
            }
        }
    }

    private fun playerCamera(delta: Float,camera: Camera, tiles: MutableList<Tile>): Boolean
    {

        val state = this.moveClickController.isActive(delta) {
            return@isActive Gdx.input.isTouched;
        }

        if(state == InputController.InputStates.CLICK)
        {
            if (this.requestPosition == this.position) //Gdx.input.isTouched
            {
                val minValue = 4;

                val inpDX = Gdx.input.deltaX
                val inpDY = Gdx.input.deltaY
                val moveX = abs(inpDX) > abs(inpDY)

                var deltaX = 0
                var deltaY = 0

                if(abs(inpDX) >= minValue)
                {
                    deltaX = if(inpDX < 0) Tile.tile_size().x.toInt() else -Tile.tile_size().x.toInt();
                }

                if(abs(inpDY) > minValue)
                {
                    deltaY = if(inpDY > 0) Tile.tile_size().y.toInt() else -Tile.tile_size().y.toInt();
                }


                if(moveX && (deltaX != 0 || deltaY != 0))
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

                for (tile in tiles)
                {
                    if(tile.position == Vector2(this.requestPosition.x,this.requestPosition.y-Tile.tile_size().y))
                    {
                        if(tile.type == Tile.TileTypes.WALL)
                        {
                            if(moveX)
                            {
                                this.requestPosition.x -= deltaX;
                                return false;
                            }else{
                                this.requestPosition.y -= deltaY;
                                return false;
                            }
                        }
                    }
                }

                if(deltaX != 0 || deltaY != 0)
                {
                    return true;
                }
            }

        }

        camera.position.set(Vector3(this.position.x, this.position.y , camera.position.z));
        camera.update()

        return false;
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

        if(this.showItem)
        {
            this.inventory[this.itemIndex].renderOnPlayer(batch,this.position,this.direction == Direction.LEFT);
        }
    }


    fun update(delta: Float, camera: Camera, tiles: MutableList<Tile>, items: MutableList<Item>) {
        if(!this.playerCamera(delta,camera,tiles))
        {
            this.itemAction(delta,items);
        }else{
            this.itemDoubleClickController.reset()
        }


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

        var i = 0;
        val tmpItems = items.toList();
        for(item in tmpItems)
        {
            if(item.position == this.requestPosition && item.type == Item.ItemTypes.COLLECT)
            {
                this.inventory.add(item);
                items.removeAt(i);
                i--;
            }
            i++;
        }
    }

    fun getInventory():List<Item>{
        return this.inventory.toList();
    }
}