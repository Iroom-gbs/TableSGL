package com.iroom.tablesgl.data

import com.dayo.simplegameapi.data.RoomInfo
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
        var GameTableList = emptyMap<String,GameTable>().toMutableMap()
        var TableIDList = emptyMap<RoomInfo, GameTable>().toMutableMap()

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