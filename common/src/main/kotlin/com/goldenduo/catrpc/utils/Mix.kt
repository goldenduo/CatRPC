package com.goldenduo.catrpc.utils

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