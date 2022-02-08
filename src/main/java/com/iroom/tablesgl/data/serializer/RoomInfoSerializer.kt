package com.iroom.tablesgl.data.serializer

import com.google.gson.*
import me.ddayo.simplegameapi.data.RoomInfo
import java.lang.reflect.Type

class RoomInfoSerializer: JsonSerializer<RoomInfo>, JsonDeserializer<RoomInfo> {
    override fun serialize(src: RoomInfo, typeOfSrc: Type?, context: JsonSerializationContext?) = JsonPrimitive("${src.gid} ${src.rid}")

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): RoomInfo {
        val s = json.asString.split(' ').map{it.toInt()}
        return RoomInfo(s[0], s[1])
    }
}