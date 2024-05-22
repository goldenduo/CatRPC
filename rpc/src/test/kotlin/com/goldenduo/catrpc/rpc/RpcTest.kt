package com.goldenduo.catrpc.rpc

import com.goldenduo.catrpc.rpc.remote.ICommonRemote
import com.goldenduo.catrpc.rpc.remote.IRemote
import com.goldenduo.catrpc.rpc.remote.createStub
import com.goldenduo.catrpc.utils.calcTimeMills
import com.goldenduo.catrpc.utils.findAvailablePort
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RpcTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun testCommonRemote() {
        val testCount = 1
        val port = findAvailablePort()
        val server = RpcServer()
        val client = RpcClient()
        server.start(port)
        client.connect("127.0.0.1", port)
        val commonRemoteStub = IRemote.createStub<ICommonRemote>(client)
        calcTimeMills("testCommonRemote") {
            (0..1_000).forEach {
                commonRemoteStub.ping()
            }
        }

    }
}