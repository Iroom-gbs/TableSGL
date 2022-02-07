package com.iroom.tablesgl.event

import com.iroom.tablesgl.TableController.Companion.forceLeftPlayer
import com.iroom.tablesgl.data.Data.Companion.PlayerSittingList
import com.iroom.tablesgl.data.Data.Companion.TableIDList
import dev.geco.gsit.api.event.PlayerGetUpSitEvent
import dev.geco.gsit.api.event.PrePlayerSitEvent
import dev.geco.gsit.objects.GetUpReason
import me.ddayo.simplegameapi.data.GameManager.Companion.getPlaying
import me.ddayo.simplegameapi.data.GameManager.Companion.getRoomStatus
import me.ddayo.simplegameapi.data.GameManager.Companion.joinPlayer
import me.ddayo.simplegameapi.data.GameManager.Companion.leftPlayer
import me.ddayo.simplegameapi.data.GameManager.Companion.makePlayerFailed
import me.ddayo.simplegameapi.data.RoomInfo
import me.ddayo.simplegameapi.data.Status
import me.ddayo.simplegameapi.event.GameFinishEvent
import me.ddayo.simplegameapi.event.GameStartEvent
import me.ddayo.simplegameapi.event.PlayerFailEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*


class TableListener : Listener{
    @EventHandler
    fun OnSit(event: PrePlayerSitEvent) {
        if(PlayerSittingList.containsKey(event.player))
        {
            if(event.block.location!=PlayerSittingList.get(event.player))
                event.isCancelled=true
        }
        else
        {
            if(event.block.hasMetadata("GameID"))
            {
                Bukkit.getConsoleSender().sendMessage(event.block.getMetadata("GameID")[0].value().toString())
                val gi = event.block.getMetadata("GameID")[0].value().toString().toInt()
                val ri = event.block.getMetadata("RoomID")[0].value().toString().toInt()

                if(joinPlayer(event.player.uniqueId, RoomInfo(gi,ri)))
                {
                    PlayerSittingList.put(event.player,event.block.location)
                }

                else event.player.sendMessage("게임에 참여할 수 없습니다.")

            }

        }
    }

    @EventHandler
    fun OnExit(event: PlayerGetUpSitEvent)
    {
        if(getPlaying(event.player.uniqueId)?.let { getRoomStatus(it) }== Status.Playing)
        {
            event.player.sendMessage(getPlaying(event.player.uniqueId)!!.toString())
            //게임플레이 중에 Shift로 일어날 때
            if(event.reason==GetUpReason.GET_UP)
            {
                //TODO: GSIt 버그 고쳐지면 게임 플레이 중에 못일어나게 하기 (Pre)
                event.player.sendMessage("자리에 다시 앉아주세요!")
            }
            //다른 이유로 나가질 떄
            else if(event.reason!=GetUpReason.QUIT&&event.reason!=GetUpReason.KICKED)
            {
                makePlayerFailed(event.player.uniqueId)
            }
        }
        else
        {
            PlayerSittingList.remove(event.player)
            leftPlayer(event.player.uniqueId)
        }
    }

    @EventHandler
    fun OnQuit(event: PlayerQuitEvent)
    {
        if(getPlaying(event.player.uniqueId)?.let { getRoomStatus(it) }==Status.Playing)
        {
                makePlayerFailed(event.player.uniqueId)
        }
    }

    @EventHandler
    fun OnKick(event: PlayerKickEvent)
    {
        if(getPlaying(event.player.uniqueId)?.let { getRoomStatus(it) }==Status.Playing)
        {
            makePlayerFailed(event.player.uniqueId)
        }
    }

    @EventHandler
    fun OnMove(event: PlayerMoveEvent)
    {
        if(getPlaying(event.player.uniqueId)?.let { getRoomStatus(it) }==Status.Playing)
            if(event.to!!.distance(event.from)>0.001)
            {
                val to: Location = event.getFrom()
                to.setPitch(event.getTo()!!.getPitch())
                to.setYaw(event.getTo()!!.getYaw())
                event.setTo(to)
            }
    }

    @EventHandler
    fun OnFail(event : PlayerFailEvent)
    {
        Bukkit.getConsoleSender().sendMessage("Failed!")
        val p = Bukkit.getOfflinePlayer(event.uid)
        if(p.isOnline)
        {
            forceLeftPlayer(p as Player)
        }
        PlayerSittingList.remove(p)
    }

    @EventHandler
    fun OnGameStart(event: GameStartEvent)
    {
        TableIDList.get(event.roomInfo)!!.onGameStart()
    }

    @EventHandler
    fun OnGameStop(event: GameFinishEvent)
    {
        TableIDList.get(event.roomInfo)!!.onGameFinish()
    }
}
