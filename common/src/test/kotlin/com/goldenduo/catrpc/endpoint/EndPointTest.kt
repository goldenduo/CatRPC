package com.goldenduo.catrpc.endpoint

import com.goldenduo.catrpc.types.Ping
import com.goldenduo.catrpc.types.Pong
import com.goldenduo.catrpc.utils.debug
import com.goldenduo.catrpc.utils.findAvailablePort
import com.goldenduo.catrpc.utils.info
import com.goldenduo.catrpc.utils.toMB
import io.netty.channel.ChannelHandlerContext
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.Test

class EndPointTest {
    @Test
    fun pingAndPong() {
        debug("max memory:${Runtime.getRuntime().maxMemory().toMB()}M")

        val testCount = 1_000_000  //100m
        val server = CatServer()
        val client = CatClient()
        val done = CountDownLatch(testCount)
        val port = findAvailablePort()
        server.setController {
            object : ServerController() {
                override fun handle(obj: Any, type: Class<*>): Any {
                    if (obj is Ping) {
                        return Pong()
                    } else {
                        throw RuntimeException()
                    }
                }

            }
        }
        server.start(port)
        client.setController {
            object : ClientController() {
                override fun handle(ctx: ChannelHandlerContext, obj: Any, type: Class<*>) {
                    assertTrue(obj is Pong)
                    obj as Pong
                    assertTrue(obj.msg == "pong")
                    done.countDown()
                    if (done.count % (testCount / 100) == 0L) {
                        debug("receive pong:${done.count}")
                    }
                }
            }
        }
        client.connect("127.0.0.1", port)
        System.gc()
        val lastFreeMemory = Runtime.getRuntime().freeMemory().toMB()
        val lastTime=System.currentTimeMillis()
        val threadCount = 10
        (1..threadCount).forEach {
            Thread {
                (1..testCount/threadCount).forEach {
                    client.send(Ping())
                }
            }.start()
        }
        done.await()
        System.gc()
        val nowFreeMemory = Runtime.getRuntime().freeMemory().toMB()
        val nowTime=System.currentTimeMillis()
        debug("free memory before sending:${lastFreeMemory}M")
        debug("free memory after sending:${nowFreeMemory}M")
        debug("free memory gap:${nowFreeMemory - lastFreeMemory}M")
        debug("cost time:${nowTime - lastTime}ms")
        server.close()
        client.close()
    }
}