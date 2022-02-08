package com.iroom.tablesgl.data.serializer

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.bukkit.Location
import java.lang.reflect.Type

class LocationSerializer: JsonSerializer<Location>, JsonDeserializer<Location> {
    override fun serialize(src: Location, typeOfSrc: Type?, context: JsonSerializationContext?) = Gson().toJsonTree(src.serialize())
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?) = Location.deserialize(Gson().fromJson(json, object: TypeToken<Map<String, Any>>() {}.type))
}