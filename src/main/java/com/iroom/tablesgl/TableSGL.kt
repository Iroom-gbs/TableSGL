package com.iroom.tablesgl

import com.iroom.tablesgl.TableController.Companion.addIDList
import com.iroom.tablesgl.TableController.Companion.makeHologram
import com.iroom.tablesgl.command.TableCommand
import com.iroom.tablesgl.data.Data.Companion.GameTableList
import com.iroom.tablesgl.data.Data.Companion.loadData
import com.iroom.tablesgl.data.Data.Companion.saveData
import com.iroom.tablesgl.event.TableListener
import me.ddayo.simplegameapi.data.GameManager.Companion.getGameId
import me.ddayo.simplegameapi.data.GameManager.Companion.getRegisteredGameNameList
import me.ddayo.simplegameapi.data.RoomInfo
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class TableSGL : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(TableListener(),this)
        val Ig = getRegisteredGameNameList().iterator()
        while(Ig.hasNext())
        {
            Bukkit.getConsoleSender().sendMessage(Ig.next())
        }
        this.getCommand("gt")?.setExecutor(TableCommand(this))
        loadData(dataFolder.toString() + File.separator+"TableData.dat")
        val I = GameTableList.iterator()
        while(I.hasNext())
        {
            val g = I.next()
            addIDList(RoomInfo(getGameId(g.value.gamename)!!,g.value.roomID),g.value)
            g.value.addSeat(this)
            makeHologram(g.value)
        }
        Bukkit.getConsoleSender().sendMessage("TableSGL Enabled!")
    }

    override fun onDisable() {
        saveData(dataFolder.toString() + File.separator+ "TableData.dat")
    }

}