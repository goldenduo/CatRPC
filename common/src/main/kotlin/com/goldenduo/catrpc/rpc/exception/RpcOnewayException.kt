package com.goldenduo.catrpc.rpc.exception

class RpcOnewayException(msg:String="A one-way method should have a void return type"):RpcException(msg)