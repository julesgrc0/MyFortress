package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import java.io.File
import java.lang.StringBuilder
import java.util.*


class Level() {

    val player: Player = Player(Vector2(0f, 0f));
    private var tiles: MutableList<Tile> = mutableListOf();
    //private var textures: MutableList<Pair<String, TextureRegion>> = mutableListOf();
    private var textures:  Array<Array<TextureRegion>>? = null;
    /*
    fun loadTextures(textureAtlas: TextureAtlas) {
        val textureNames: List<String> = listOf(


            // floor
            "FillDotLeft",
            "FillDotRight",
            "FloorBottomHoleWater",
            "FloorRight",
            "FloorLeft",
            "FloorFill",

            // brick
            "BrickWallStart",
            "BrickWallFill",
            "BrickWallEnd",

            // brick with item
            "BottomTubeWallFace",
            "TopTubeWallFace",
            "WallBreakHoleWater",
            "SewerGrate"
        );

        for (name in textureNames) {
            val pair = Pair(name, textureAtlas.findRegion(name))
            textures.add(pair);
        }
    }
    */

    fun loadTextures(textures: Array<Array<TextureRegion>>?)
    {
        this.textures = textures;

        // default
        this.playerTexture(arrayListOf(Pair(14,15), Pair(15,15)))
    }

    private fun playerTexture(textures_pairs : List<Pair<Int,Int>>)
    {
        this.player.textures.clear()
        for(pair in textures_pairs)
        {
            val t = this.textures?.get(pair.first)?.get(pair.second)
            if(t != null)
            {
                this.player.textures.add(t)
            }
        }
    }

    fun loadLevel(levelIndex: Int): Boolean {
        this.tiles.clear()
        val handle = Gdx.files.internal("$levelIndex.level")
        if (!handle.exists() && handle.isDirectory) {
            return false;
        }

        val text = handle.readString();
        val lines = text.split("\n");
        var section_name: String = "";
        val variables = mutableListOf<Pair<String,String>>();

        for (_line in lines) {
            var line = _line.replace("\r","");

            if (!line.startsWith("#")) {

                if (line.startsWith("[") && line.endsWith("]")) {
                    line=line.replace("[", "").replace("]", "").toLowerCase(Locale.getDefault());
                    section_name = line
                } else if (section_name != "") {
                    if (line.startsWith("->")) {
                        val llist = line.split(":")
                        if (llist.size == 2) {
                            val lname = llist[0].trim().replace("->","")
                            var lvalue = llist[1].trim()

                            if(lvalue.contains("$"))
                            {
                                for (v in variables)
                                {
                                    if(v.first == lvalue.replace("$",""))
                                    {
                                        lvalue = v.second;
                                        break
                                    }
                                }
                            }

                            if (section_name == "player") {
                                when (lname) {
                                    "position" -> {
                                        val pos =
                                            Vector2(
                                                lvalue.split(",")[0].toFloat() * Tile.tile_size().x,
                                                lvalue.split(",")[1].toFloat()* Tile.tile_size().y)

                                        this.player.position = pos;
                                    }
                                    "speed" ->{
                                        this.player.speed = lvalue.toFloat();
                                    }
                                    "skin"->{
                                        val tex = lvalue.split(",")
                                        if(tex.size%2 == 0)
                                        {
                                            val listIndex = mutableListOf<Pair<Int,Int>>()
                                            for(i in 0 until tex.size step 2)
                                            {
                                                val pIndex = Pair<Int,Int>(tex[i].toInt(),tex[i+1].toInt())
                                                listIndex.add(pIndex)
                                            }
                                            this.playerTexture(listIndex);
                                        }
                                    }
                                }
                            }
                        }


                    }
                    else if (line.startsWith("/")) {

                        val parts = line.split(";").toMutableList();
                        if (parts.size == 3) {

                            if(line.contains("$"))
                            {
                                var i =0;
                                for(p in parts)
                                {
                                    if(p.startsWith("$"))
                                    {
                                        varloop@ for (v in variables)
                                        {
                                            if(v.first == p.replace("$",""))
                                            {
                                                parts[i] = v.second;
                                                break@varloop
                                            }
                                        }
                                    }
                                    i++;
                                }
                            }


                            val pos = parts[0].replace("/", "").replace("(", "").replace(")", "").split(",")
                            val x = pos[0].toInt().toFloat() * Tile.tile_size().x;
                            val y = pos[1].toInt().toFloat() * Tile.tile_size().y;


                            val type: Tile.TileTypes = Tile.TileTypes.values()[parts[1].toInt()];

                            val textCoord = parts[2].trim().split(",")

                            val texturesX: Int = textCoord[0].toInt();
                            val texturesY: Int = textCoord[1].toInt();

                            val textureSize = (this.textures?.size ?: 16);

                            if((texturesX in 0..textureSize) && (texturesY in 0..textureSize))
                            {
                                val texture: TextureRegion? = this.textures?.get(texturesY)?.get(texturesX);
                                if(texture != null)
                                {
                                    val tile = Tile(Vector2(x, y), type,  texture);
                                    this.tiles.add(tile)
                                }
                            }
                            /*for (pair in this.textures) {

                                if (pair.first.contentEquals(parts[2])) {
                                    val tile = Tile(Vector2(x, y), type, pair.second);
                                    this.tiles.add(tile)

                                    break;
                                }
                            }*/
                        }
                    }
                }else if(line.startsWith("$"))
                {
                    val parts = line.replace("$","").split(":");
                    variables.add(Pair( parts[0].trim(),parts[1].trim()))
                }
            }
        }


        return true;
    }

    fun update(delta: Float, camera: Camera) {
        this.player.update(delta, camera, this.tiles)
    }

    fun render(batch: Batch, pause: Boolean = false) {
        for (tile: Tile in this.tiles) {
            tile.render(batch);
        }

        this.player.render(batch);
    }
}