package com.goldenduo.catrpc.rpc

import com.goldenduo.catrpc.utils.debug
import com.goldenduo.catrpc.utils.debugLog
import org.reflections.Reflections

class RemoteScanner {
    private val reflections=Reflections()
    private val remoteInterfaces=reflections.getSubTypesOf(IRemote::class.java)


}

fun main(){
    Reflections().getSubTypesOf(IRemote::class.java).forEach {
        it.isInterface
    }
}