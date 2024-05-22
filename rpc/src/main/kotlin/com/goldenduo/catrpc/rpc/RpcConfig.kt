package com.goldenduo.catrpc.rpc

annotation class RpcConfig(val isOneWay:Boolean=false,val maxTimeMillis:Long=10_000)