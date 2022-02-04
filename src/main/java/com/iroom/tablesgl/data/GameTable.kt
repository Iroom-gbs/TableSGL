package com.iroom.tablesgl.data

import com.dayo.simplegameapi.data.GameManager.Companion.getGameById
import com.dayo.simplegameapi.data.RoomInfo
import com.iroom.tablesgl.TableController.Companion.getHologram
import com.iroom.tablesgl.TableSGL
import eu.decentsoftware.holograms.api.DHAPI
import org.bukkit.Location
import org.bukkit.metadata.FixedMetadataValue
import java.io.Serializable
import java.util.*

class GameTable(gameID: Int, roomID: Int, loc: Location):Serializable {
    val location = loc.world!!.getBlockAt(loc).location
    val gameID = gameID
    val roomID = roomID

    val seats = ArrayList<Seat>()

    open fun onGameStart()
    {
        DHAPI.removeHologramLine(getHologram(RoomInfo(gameID,roomID)),0)
        DHAPI.removeHologramLine(getHologram(RoomInfo(gameID,roomID)),0)
    }

    open fun onGameFinish()
    {
        val game = getGameById(gameID)
        DHAPI.setHologramLines(getHologram(RoomInfo(gameID,roomID)), Arrays.asList(game.name, game.playerCount.toString()+"명 ~ "+ game.maxPlayerCount.toString()+"명"))
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
            b.setMetadata("GameID", FixedMetadataValue(plugin, gameID.toString()))
            b.setMetadata("RoomID", FixedMetadataValue(plugin, roomID.toString()))
        }
    }
}