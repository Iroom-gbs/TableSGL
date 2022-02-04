package com.iroom.tablesgl.event

import com.dayo.simplegameapi.data.GameManager.Companion.getPlaying
import com.dayo.simplegameapi.data.GameManager.Companion.getRoomStatus
import com.dayo.simplegameapi.data.GameManager.Companion.joinPlayer
import com.dayo.simplegameapi.data.GameManager.Companion.leftPlayer
import com.dayo.simplegameapi.data.RoomInfo
import com.dayo.simplegameapi.data.Status
import com.dayo.simplegameapi.event.GameFinishEvent
import com.dayo.simplegameapi.event.PlayerFailEvent
import com.iroom.tablesgl.TableController.Companion.forceLeftPlayer
import dev.geco.gsit.api.event.PlayerGetUpSitEvent
import dev.geco.gsit.api.event.PlayerSitEvent
import dev.geco.gsit.objects.GetUpReason
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class TableListener : Listener{
    @EventHandler
    fun OnSit(event: PlayerSitEvent) {
        if(event.seat.block.hasMetadata("GameID"))
        {
            Bukkit.getConsoleSender().sendMessage(event.seat.block.getMetadata("GameID")[0].value().toString())
            val gi = event.seat.block.getMetadata("GameID")[0].value().toString().toInt()
            val ri = event.seat.block.getMetadata("RoomID")[0].value().toString().toInt()

            joinPlayer(event.player.uniqueId, RoomInfo(gi,ri))
        }
    }

    @EventHandler
    fun OnExit(event: PlayerGetUpSitEvent)
    {
        //게임플레이 중에 Shift로 일어날 때
        if(getPlaying(event.player.uniqueId)?.let { getRoomStatus(it) }==Status.Playing && event.reason==GetUpReason.GET_UP)
        {
            //TODO: GSIt 버그 고쳐지면 게임 플레이 중에 못일어나게 하기 (Pre)
            event.player.sendMessage("자리에 다시 앉아주세요!")
        }
        else leftPlayer(event.player.uniqueId)
    }

    @EventHandler
    fun OnMove(event: PlayerMoveEvent)
    {
        if(getPlaying(event.player.uniqueId)?.let { getRoomStatus(it) }==Status.Playing)
            if(event.to!!.distance(event.from)>0.01)
            event.isCancelled=true
    }

    @EventHandler
    fun OnFail(event : PlayerFailEvent)
    {
        forceLeftPlayer(event.player)
    }

    @EventHandler
    fun OnGameStop(event: GameFinishEvent)
    {

    }
}
