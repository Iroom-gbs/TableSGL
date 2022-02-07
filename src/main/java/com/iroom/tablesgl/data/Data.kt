package com.iroom.tablesgl.data

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

        fun saveData(path:String)
        {
            if(!File(path).exists())
                File(path).createNewFile()
            var out = BukkitObjectOutputStream(GZIPOutputStream(FileOutputStream(path)))
            out.writeObject(GameTableList)
            out.close()
        }

        fun loadData(path:String) : Boolean
        {
            if(File(path).exists())
            {
                var ins = BukkitObjectInputStream(GZIPInputStream(FileInputStream(path)))
                GameTableList = ins.readObject() as MutableMap<String,GameTable>
                ins.close()
                return true
            }
            return true
        }
    }

}