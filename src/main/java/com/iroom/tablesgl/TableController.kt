package com.iroom.tablesgl

import com.dayo.simplegameapi.data.GameManager.Companion.getGameById
import com.iroom.tablesgl.data.GameTable
import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.DecentHolograms
import eu.decentsoftware.holograms.api.DecentHologramsAPI
import eu.decentsoftware.holograms.plugin.DecentHologramsPlugin
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
            val game = getGameById(t.gameID)
            val loc = t.location
            var tableHologram = DHAPI.createHologram((game.name+t.roomID.toString()),Location(loc.world,loc.x + 0.5,loc.y + 1.7,loc.z+0.5))
            DHAPI.setHologramLines(tableHologram, Arrays.asList(game.name, game.playerCount.toString()+"명 ~ "+ game.maxPlayerCount.toString()+"명"))
        }
    }
}