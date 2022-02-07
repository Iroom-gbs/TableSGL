package com.iroom.tablesgl.command

import com.iroom.tablesgl.TableController.Companion.addIDList
import com.iroom.tablesgl.TableController.Companion.makeHologram
import com.iroom.tablesgl.TableController.Companion.removeIDList
import com.iroom.tablesgl.TableSGL
import com.iroom.tablesgl.data.Data.Companion.GameTableList
import com.iroom.tablesgl.data.GameTable
import me.ddayo.simplegameapi.data.GameManager.Companion.getGameId
import me.ddayo.simplegameapi.data.RoomInfo
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TableCommand(plugin: TableSGL) : CommandExecutor {
    val plugin = plugin
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when(command.name)
        {
            "gt"->
            {
                when(args[0])
                {
                    "create"->
                    {
                        val p = sender as Player
                        val g = GameTable(args[2] ,args[3].toInt(),p.location.add(0.0,-1.0,0.0))
                        if(addIDList(RoomInfo(getGameId(args[2])!!,args[3].toInt()),g))
                        {
                            makeHologram(g)
                            GameTableList[args[1]] = g
                            sender.sendMessage("게임 테이블 생성완료")
                        }
                        else sender.sendMessage("같은 방 정보의 테이블을 두개 이상 생성할 수 없습니다!")
                    }
                    "remove"->
                    {
                        val p = sender as Player
                        removeIDList(RoomInfo(getGameId(GameTableList[args[1]]!!.gamename)!!,GameTableList[args[1]]!!.roomID))
                        GameTableList[args[1]]?.removeSeat(plugin)
                        GameTableList.remove(args[1])
                        sender.sendMessage("게임 테이블 제거완료")
                    }
                    "list"->
                    {
                        val I = GameTableList.iterator()
                        sender.sendMessage("---테이블 목록---")
                        while(I.hasNext())
                        {
                            val g = I.next()
                            sender.sendMessage(g.key + ": " + g.value.gamename + ", " + g.value.roomID.toString())
                        }
                        sender.sendMessage("---------------")
                    }
                    "update"->
                    {
                        val I = GameTableList.iterator()
                        while(I.hasNext())
                        {
                            val g = I.next()
                            g.value.addSeat(plugin)
                        }
                        sender.sendMessage("테이블 갱신 완료!")
                    }
                    "seat"->
                    {
                        val p = sender as Player
                        GameTableList[args[1]]?.regSeat(p.location.add(0.0,-1.0,0.0))
                        GameTableList[args[1]]?.addSeat(plugin)
                        sender.sendMessage("좌석 등록 완료!")
                    }
                }
            }
        }
        return true
    }
}