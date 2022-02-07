package com.iroom.tablesgl.data

import com.iroom.tablesgl.TableController.Companion.getHologram
import com.iroom.tablesgl.TableSGL
import eu.decentsoftware.holograms.api.DHAPI
import me.ddayo.simplegameapi.data.GameManager.Companion.getGame
import me.ddayo.simplegameapi.data.GameManager.Companion.getGameId
import me.ddayo.simplegameapi.data.RoomInfo
import org.bukkit.Location
import org.bukkit.metadata.FixedMetadataValue
import java.io.Serializable
import java.util.*

class GameTable(gamename: String, roomID: Int, loc: Location):Serializable {
    val location = loc.world!!.getBlockAt(loc).location
    val gamename = gamename
    val roomID = roomID

    val seats = ArrayList<Seat>()

    open fun onGameStart()
    {
        DHAPI.removeHologramLine(getHologram(RoomInfo(getGameId(gamename)!!,roomID)),0)
        DHAPI.removeHologramLine(getHologram(RoomInfo(getGameId(gamename)!!,roomID)),0)
    }

    open fun onGameFinish()
    {
        val game = getGame(RoomInfo(getGameId(gamename)!!,roomID))
        DHAPI.setHologramLines(getHologram(RoomInfo(getGameId(gamename)!!,roomID)), Arrays.asList(gamename, game!!.playerCount.toString()+"명 ~ "+ game.maxPlayerCount.toString()+"명"))
    }

    fun removeSeat(plugin:TableSGL)
    {
        val I = seats.iterator()
        while(I.hasNext())
        {
            val seat = I.next()
            val b = seat.location.world!!.getBlockAt(seat.location)
            b.removeMetadata("GameID",plugin)
            b.removeMetadata("RoomID",plugin)
        }

    }

    fun regSeat(loc: Location)
    {
        seats.add(Seat(loc))
    }

    fun addSeat(plugin: TableSGL)
    {
        val I = seats.iterator()
        while(I.hasNext())
        {
            val seat = I.next()
            val b = seat.location.world!!.getBlockAt(seat.location)
            b.setMetadata("GameID", FixedMetadataValue(plugin, getGameId(gamename).toString()))
            b.setMetadata("RoomID", FixedMetadataValue(plugin, roomID.toString()))
        }
    }
}