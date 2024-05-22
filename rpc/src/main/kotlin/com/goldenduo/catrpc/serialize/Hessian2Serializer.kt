package com.goldenduo.catrpc.serialize

import com.caucho.hessian.io.Hessian2Input
import com.caucho.hessian.io.Hessian2Output
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Serializable

class Hessian2Serializer : ISerializer {
    // 序列化一个对象
    override fun serialize(obj: Any): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            val ho = Hessian2Output(byteArrayOutputStream)
            try {
                ho.writeObject(obj)
                ho.flush()
            } finally {
                ho.close()
            }
            return byteArrayOutputStream.toByteArray()
        }
    }

    override fun <T : Any> deserialize(bytes: ByteArray,offset:Int,length:Int, clazz: Class<T>): T {
        ByteArrayInputStream(bytes,offset,length).use { byteArrayInputStream ->
            val hi = Hessian2Input(byteArrayInputStream)
            try {
                @Suppress("UNCHECKED_CAST") return hi.readObject(clazz) as T
            } finally {
                hi.close()
            }
        }
    }

}