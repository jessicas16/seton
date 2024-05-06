package com.example.seton.component

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TFDateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateString(format: String = "MM/dd/yyyy"): String {
        var formatter = DateTimeFormatter.ofPattern(format)
        return LocalDate.now().format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToLocalDate(millis: Long) : LocalDate {
        return LocalDate.ofEpochDay(Duration.ofMillis(millis).toDays());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToDateString(millis: Long, format: String = "MM/dd/yyyy"): String {
        var formatter = DateTimeFormatter.ofPattern(format)
        return convertMillisToLocalDate(millis).format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTimeString(format: String = "hh:mm a"): String {
        var formatter = DateTimeFormatter.ofPattern(format)
        return LocalTime.now().format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeString(hour: Int, min: Int, format: String = "hh:mm a") : String {
        var formatter = DateTimeFormatter.ofPattern(format)
        var localTime = LocalTime.of(hour, min)
        return localTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocalDataTimeFromString(dateTime: String, format: String): LocalDateTime {
        var formatter = DateTimeFormatter.ofPattern(format)
        return LocalDateTime.parse(dateTime, formatter)
    }
}