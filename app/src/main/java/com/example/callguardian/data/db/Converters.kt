package com.example.callguardian.data.db

import androidx.room.TypeConverter
import com.example.callguardian.model.BlockMode
import java.time.Instant

class Converters {

    @TypeConverter
    fun fromEpoch(epoch: Long?): Instant? = epoch?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun toEpoch(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun fromBlockMode(value: String?): BlockMode =
        value?.let { BlockMode.valueOf(it) } ?: BlockMode.NONE

    @TypeConverter
    fun toBlockMode(mode: BlockMode?): String = mode?.name ?: BlockMode.NONE.name

    @TypeConverter
    fun fromTags(value: String?): List<String> =
        value?.split("|")?.filter { it.isNotBlank() } ?: emptyList()

    @TypeConverter
    fun toTags(tags: List<String>?): String =
        tags?.joinToString(separator = "|") ?: ""
}
