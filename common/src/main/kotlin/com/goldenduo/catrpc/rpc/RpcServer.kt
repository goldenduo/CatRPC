package com.goldenduo.catrpc.rpc

import com.goldenduo.catrpc.endpoint.CatServer
import com.goldenduo.catrpc.endpoint.ServerController

class RpcServer: CatServer() {
    init {
        setController {
            object:ServerController(){
                override fun handle(obj: Any, type: Class<*>): Any {
                    if (obj is RemoteRequest){

                    }
                    TODO()
                }
            }
        }
    }
}