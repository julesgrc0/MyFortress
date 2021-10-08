package com.julesg10.myfortress.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import java.io.File
import java.lang.StringBuilder


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
        for (line in lines) {

            if (!line.startsWith("#")) {

                if (line.startsWith("[") && line.endsWith("]\r")) {
                    line.replace("[", "")
                    line.replace("]\r", "")
                    line.toLowerCase();
                    section_name = line

                } else if (section_name != "") {
                    if (line.startsWith("->")) {
                        val llist = line.split(":")
                        if (llist.size == 2) {
                            llist[0].replace("->", "")
                            llist[1].trim()

                            val lname = llist[0]
                            val lvalue = llist[1]

                            if (section_name == "player") {
                                when (lname) {
                                    "position" -> {
                                        val pos =
                                            Vector2(lvalue.split(",")[0].toFloat(), lvalue.split(",")[1].toFloat())
                                        //this.player.move(pos,this.batch,this.camera);
                                    }
                                    "direction" -> {

                                    }
                                }
                            }
                        }
                    } else if (line.startsWith("/")) {

                        val parts = line.split(";").toMutableList();
                        if (parts.size == 4) {

                            val pos = parts[0].replace("/", "").replace("(", "").replace(")", "").split(",")
                            val x = pos[0].toInt().toFloat() * Tile.tile_size().x;
                            val y = pos[1].toInt().toFloat() * Tile.tile_size().x;


                            val type: Tile.TileTypes = Tile.TileTypes.values()[parts[1].toInt()];

                            val texturesX: Int = parts[2].toInt();
                            val texturesY: Int = parts[3].replace("\r","").toInt();

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

                                if (pair.first.contentEquals(parts[2].replace("\r",""))) {
                                    val tile = Tile(Vector2(x, y), type, pair.second);
                                    this.tiles.add(tile)

                                    break;
                                }
                            }*/
                        }
                    }
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