package com.goldenduo.catrpc.scanner

import com.goldenduo.catrpc.endpoint.CatClient
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Proxy
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

interface ICatRemote
interface ICommonRemote {
    fun ping(): String
}

class CommonRemote : ICommonRemote {
    override fun ping(): String {
        return "pong"
    }
}

val requestIncreaseId = AtomicLong(0)
inline fun <reified T : ICatRemote> T.createStub(client: CatClient): T {
    return Proxy.newProxyInstance(
        T::class.java.classLoader, arrayOf(T::class.java)
    ) { proxy, method, args ->
        val request = CatRequest(
            requestIncreaseId.getAndIncrement(),
            T::class.java.name,
            method.name,
            method.parameterTypes,
            args,
        )
        val once = method.getAnnotation(CatOnce::class.java)
//        client.invoke(request, once != null)
        Unit
    } as T
}

val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
fun main() = runBlocking<Unit>(dispatcher) { // 启动主协程
    println("0" + Thread.currentThread())
    test()
}

suspend fun test() {
    runBlocking {
        println("1" + Thread.currentThread())
        val result = async(dispatcher) {
            println("2" + Thread.currentThread())
            // 在 runBlocking 中启动一个新的协程
            delay(1000) // 模拟一些耗时操作
            "Hello"
        }.await() // 等待协程完成并获取结果
        println("3" + Thread.currentThread())
        println(result) // 输出 "Hello"
    }
}