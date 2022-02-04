package com.iroom.tablesgl.data

import com.dayo.simplegameapi.data.GameManager.Companion.getGameById
import com.iroom.tablesgl.TableSGL
import org.bukkit.Location
import org.bukkit.metadata.FixedMetadataValue
import java.io.Serializable
import java.util.*

class GameTable(gameID: Int, roomID: Int, loc: Location):Serializable {
    val location = loc.world!!.getBlockAt(loc).location
    val gameID = gameID
    val roomID = roomID

    val seats = ArrayList<Seat>()


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