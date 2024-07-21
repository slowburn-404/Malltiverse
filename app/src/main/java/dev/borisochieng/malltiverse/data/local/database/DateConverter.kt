package dev.borisochieng.malltiverse.data.local.database

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromDate(date: Date?) : Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timeStamp: Long?): Date? {
        return timeStamp?.let{Date(it)}
    }
}