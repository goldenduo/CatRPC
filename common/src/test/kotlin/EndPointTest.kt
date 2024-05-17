import com.goldenduo.catrpc.endpoint.CatClient
import com.goldenduo.catrpc.endpoint.CatServer
import com.goldenduo.catrpc.endpoint.EndPoint
import com.goldenduo.catrpc.endpoint.ServerController
import com.goldenduo.catrpc.types.Ping
import com.goldenduo.catrpc.types.Pong
import com.goldenduo.catrpc.utils.debug

fun main(){
    val endPoint=EndPoint()
    val catServer=CatServer(endPoint)
    val clientServer= CatClient(endPoint)
    catServer.setController {
        object:ServerController(){
            override fun handle(obj: Any, type: Class<*>): Any {
                debug(obj)
                if (obj is Ping){
                    return Pong()
                }else{
                    throw RuntimeException()
                }
            }

        }
    }
    catServer.start(10901)
    clientServer.connect("127.0.0.1",10901)
    clientServer.send(Ping())
}