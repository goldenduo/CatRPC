package com.goldenduo.catrpc.endpoint

import com.goldenduo.catrpc.serialize.ISerializer
import com.goldenduo.catrpc.serialize.ProtostuffSerializer
import com.goldenduo.catrpc.types.TypeScanner
import java.net.ServerSocket

data class EndPoint(
    val serializer: ISerializer = ProtostuffSerializer(),
    val typeScanner: TypeScanner = TypeScanner(),
    val lengthFieldLength: Int = 2,

){
    var type:Type=Type.Unknown
        set(value) {
            if (field==Type.Unknown){
                field=value
            }else{
                throw IllegalArgumentException("Type has already been set")
            }
        }
    enum class Type{
        Client,Server,Unknown
    }

}