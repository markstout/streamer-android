package com.markstouttech.streamer.util

import java.text.SimpleDateFormat
import java.util.*

object BuildUtils {
    fun getBuildTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
