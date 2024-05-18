package com.goldenduo.catrpc.endpoint

import com.goldenduo.catrpc.types.Ping
import com.goldenduo.catrpc.types.Pong
import com.goldenduo.catrpc.utils.findAvailablePort
import io.netty.channel.ChannelHandlerContext
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.Test

class EndPointTest {
    @Test
    fun pingAndPong() {

        val server = CatServer()
        val client = CatClient()
        val done=CountDownLatch(1)
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
                }
            }
        }
        client.connect("127.0.0.1", port)
        client.send(Ping())
        done.await()

        server.close()
        client.close()
    }
}