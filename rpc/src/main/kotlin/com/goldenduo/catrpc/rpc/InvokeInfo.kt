package com.goldenduo.catrpc.rpc

import java.lang.reflect.Method

data class InvokeInfo(val obj:Any?,val method: Method)
