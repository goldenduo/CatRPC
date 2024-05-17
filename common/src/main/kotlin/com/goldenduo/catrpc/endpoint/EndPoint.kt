package com.goldenduo.catrpc.endpoint

import com.goldenduo.catrpc.serialize.ISerializer
import com.goldenduo.catrpc.serialize.ProtostuffSerializer
import com.goldenduo.catrpc.types.TypeScanner

data class EndPoint(
    val serializer: ISerializer = ProtostuffSerializer(),
    val typeScanner: TypeScanner = TypeScanner(),
    val lengthFieldLength: Int = 2
)