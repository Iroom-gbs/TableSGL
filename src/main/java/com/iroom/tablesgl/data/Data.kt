package com.iroom.tablesgl.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.iroom.tablesgl.data.serializer.LocationSerializer
import com.iroom.tablesgl.data.serializer.RoomInfoSerializer
import me.ddayo.simplegameapi.data.RoomInfo
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class Data : Serializable {
    companion object
    {
        var GameTableList = emptyMap<String,GameTable>().toMutableMap() //기본적인 GameTableMap, Serializable
        var TableIDList = emptyMap<RoomInfo, GameTable>().toMutableMap()  //RoomInfo로 GameTable 얻기, 겹침 방지용
        var PlayerSittingList = emptyMap<Player, Location>().toMutableMap() //플레이어 게임플레이중 다른 자리 앉기 방지용
        val gson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeHierarchyAdapter(Location::class.java, LocationSerializer())
                .registerTypeHierarchyAdapter(RoomInfo::class.java, RoomInfoSerializer())
                .create()
        fun saveData(path:String)
        {
            if(!File(path).exists())
                File(path).mkdir()
            File(path, "TableData.json").writeText(gson.toJson(GameTableList))
            File(path, "TableID.json").writeText(gson.toJson(TableIDList))
        }

        fun loadData(path:String) : Boolean
        {
            val Gpath = File(path + File.separator+"TableData.json")
            val Ipath = File(path + File.separator+"TableID.json")
            if(Gpath.exists())
                GameTableList = gson.fromJson<Map<String, GameTable>>(Gpath.reader(), object: TypeToken<Map<String, GameTable>>(){}.type).toMutableMap()
            if(Ipath.exists())
                TableIDList = gson.fromJson<Map<RoomInfo, GameTable>>(Ipath.reader(), object: TypeToken<Map<RoomInfo, GameTable>>(){}.type).toMutableMap()
            return true
        }
    }

}