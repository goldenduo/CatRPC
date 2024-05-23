# CatRPC - 一个简单的RPC库

----

[![](https://jitpack.io/v/goldenduo/catrpc.svg)](https://jitpack.io/#goldenduo/catrpc)

CatRPC是一个基于Java的简单RPC库，它帮助您在不同的Java应用程序之间进行远程方法调用。通过使用内置的KeeperServer进行服务注册与发现，CatRPC确保您的服务可以轻松地找到彼此并进行通信。
## 特性
- **内置服务注册与发现**：使用CatRPC自带的KeeperServer来管理服务的注册与发现。
- **简单易用**：CatRPC提供了简洁的API，您可以轻松地将服务注册到KeeperServer，并通过它来进行远程方法调用。
- **多服务支持**：您可以在一个KeeperServer上注册多个服务，CatRPC会自动处理服务之间的通信。
## 快速开始
### 添加依赖
首先，您需要在项目的`build.gradle`文件中添加CatRPC的依赖：
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.goldenduo:catrpc:b30a3b5122'
}
```
### 创建服务接口
创建一个Java接口，该接口需要实现`IRemote`接口，并在其中声明需要远程调用的方法。
```java
package com.goldenduo.catrpc.zookeeper;
import com.goldenduo.catrpc.rpc.remote.IRemote;
public interface ISimpleRemote extends IRemote {
    String ping();
}
```
### 创建服务实现
创建一个实现了`ISimpleRemote`接口的Java类，并在其中实现接口中声明的方法。
```java
package com.goldenduo.catrpc.zookeeper;
import com.goldenduo.catrpc.rpc.remote.IRemote;
public class SimpleRemoteImpl implements ISimpleRemote {
    @Override
    public String ping() {
        return "Pong";
    }
}
```
### 启动KeeperServer
启动CatRPC自带的KeeperServer，以便服务可以进行注册和发现。
```java
package com.goldenduo.catrpc.zookeeper;
import com.goldenduo.catrpc.keeper.KeeperServer;
public class ServerBootstrap {
    public static void main(String[] args) {
        KeeperServer keeperServer = new KeeperServer();
        keeperServer.start(9099);
    }
}
```
### 注册服务
创建一个`KeeperClient`实例，将您的服务实现注册到KeeperServer。
```java
package com.goldenduo.catrpc.zookeeper;
import com.goldenduo.catrpc.keeper.KeeperClient;
import com.goldenduo.catrpc.rpc.remote.IRemote;
import com.goldenduo.catrpc.utils.findAvailablePort;
public class Server {
    public static void main(String[] args) {
        KeeperClient clientA = new KeeperClient("clientA", findAvailablePort(), Collections.singleton(new SimpleRemoteImpl()));
        clientA.connectKeeper("127.0.0.1", 9099);
    }
}
```
### 调用服务
创建一个`KeeperClient`实例，并通过它来调用远程服务。
```java
package com.goldenduo.catrpc.zookeeper;
import com.goldenduo.catrpc.keeper.KeeperClient;
import com.goldenduo.catrpc.rpc.remote.IRemote;
import com.goldenduo.catrpc.utils.findAvailablePort;
public class Client {
    public static void main(String[] args) {
        KeeperClient clientB = new KeeperClient("clientB", findAvailablePort(), Collections.emptySet());
        clientB.connectKeeper("127.0.0.1", 9099);
        clientB.connect("clientA");
        ISimpleRemote remote = IRemote.createStub(ISimpleRemote.class, clientB);
        System.out.println(remote.ping());
    }
}
```
## 许可证
CatRPC使用MIT许可证进行许可，您可以在MIT许可证的条款下自由使用、修改和分发CatRPC。
## 联系方式
如果您有任何问题或建议，请随时通过[电子邮件](mailto:goldenduo@qq.com)联系我们。
