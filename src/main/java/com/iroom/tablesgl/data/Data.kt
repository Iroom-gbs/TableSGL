package com.iroom.tablesgl.data

import com.google.gson.Gson
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
            val Gpath = path + File.separator+"TableData.dat"
            val Ipath = path + File.separator+"TableID.dat"
            if(!File(Gpath).exists())
                File(Gpath).createNewFile()
            if(!File(Ipath).exists())
                File(Ipath).createNewFile()
            File(Gpath).writeText(Gson().toJson(GameTableList))
            var Gout = BukkitObjectOutputStream(GZIPOutputStream(FileOutputStream(Gpath)))
            var Iout = BukkitObjectOutputStream(GZIPOutputStream(FileOutputStream(Ipath)))
            Gout.writeObject(GameTableList)
            Iout.writeObject(TableIDList)
            Gout.close()
            Iout.close()
        }

        fun loadData(path:String) : Boolean
        {
            val Gpath = path + File.separator+"TableData.dat"
            val Ipath = path + File.separator+"TableID.dat"
            if(File(Gpath).exists())
            {
                var ins = BukkitObjectInputStream(GZIPInputStream(FileInputStream(Gpath)))
                GameTableList = ins.readObject() as MutableMap<String,GameTable>
                ins.close()
            }
            if(File(Ipath).exists())
            {
                var ins = BukkitObjectInputStream(GZIPInputStream(FileInputStream(Ipath)))
                TableIDList = ins.readObject() as MutableMap<RoomInfo,GameTable>
                ins.close()
            }
            return true
        }
    }

}