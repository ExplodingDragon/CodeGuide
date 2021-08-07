package guide.netty

import java.text.SimpleDateFormat

data class UNIXTime(var date: Long = System.currentTimeMillis() / 1000L + 2208988800L) {
    override fun toString(): String {
        val time = (date - 2208988800L) * 1000L
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(time)
    }
}
