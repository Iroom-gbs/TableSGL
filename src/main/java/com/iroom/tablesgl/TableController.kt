package com.iroom.tablesgl

import com.iroom.tablesgl.data.Data.Companion.TableIDList
import com.iroom.tablesgl.data.GameTable
import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import me.ddayo.simplegameapi.data.GameManager.Companion.getGame
import me.ddayo.simplegameapi.data.GameManager.Companion.getGameId
import me.ddayo.simplegameapi.data.RoomInfo
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class TableController {
    companion object
    {
        fun forceLeftPlayer(player: Player)
        {
            player.teleport(player.location.add(0.0,1.0,0.0))
        }

        fun makeHologram(t: GameTable)
        {
            val game = getGame(RoomInfo(getGameId(t.gamename)!!,t.roomID))
            val loc = t.location
            var tableHologram = DHAPI.createHologram((game!!.name+t.roomID.toString()),Location(loc.world,loc.x + 0.5,loc.y + 1.7,loc.z+0.5))
            DHAPI.setHologramLines(tableHologram, Arrays.asList(game.name, game.playerCount.toString()+"명 ~ "+ game.maxPlayerCount.toString()+"명"))
        }

        fun getHologram(r:RoomInfo): Hologram?
        {
            return DHAPI.getHologram(getGame(r)!!.name+r.rid.toString())
        }

        fun addIDList(r: RoomInfo, t:GameTable):Boolean
        {
            if(TableIDList.containsKey(r))
                return false
            else
            {
                TableIDList[r] = t
                return true
            }

        }

        fun removeIDList(r: RoomInfo):Boolean
        {
            if(TableIDList.containsKey(r))
            {
                TableIDList.remove(r)
                return true
            }
            else return false
        }
    }
}