package com.goldenduo.catrpc.utils

import org.apache.commons.codec.digest.DigestUtils
import java.net.ServerSocket

fun findAvailablePort(): Int {
    for (port in 1024..65535) {
        try {
            ServerSocket(port).use { return port }
        } catch (e: Exception) {
            // 端口被占用，尝试下一个
        }
    }
    throw IllegalArgumentException("No available port found")
}
fun Long.toMB():Long{
    return this/1024/1024
}

fun String.toMD5(): String = DigestUtils.md5Hex(this)
fun String.toSHA256(): String = DigestUtils.sha256Hex(this)