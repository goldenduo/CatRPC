package com.goldenduo.catrpc.types

import com.goldenduo.catrpc.utils.debugLog
import org.reflections.Reflections

private  val typeAddOns= arrayOf(
    Int::class.java,
    Long::class.java,
    String::class.java,
    Short::class.java,
    Array::class.java,
    ArrayList::class.java,
    HashMap::class.java,
    Float::class.java,
    Double::class.java
)

class TypeScanner {
    private val reflections = Reflections()
    private lateinit var availableTypes: Map<Int, Class<*>>
    private lateinit var availableTypeIndex: Map<Class<*>, Int>

    init {
        scanAllMessage()
    }
    fun findIndex(clazz:Class<*>):Int{
        return availableTypeIndex[clazz]!!
    }
    fun findType(index:Int):Class<*>{
        return availableTypes[index]!!
    }
    fun travel(handle:(Int,Class<*>)->Unit){
        availableTypes.forEach { t, u ->
            handle(t,u)
        }
    }

    fun scanAllMessage() {
        val list = mutableListOf<Class<*>>()
        val types = reflections.getSubTypesOf(CatMessage::class.java)
        list.addAll(typeAddOns)
        list.addAll(types)
        list.sortedBy {
            it.name
        }
        val availableTypes = mutableMapOf<Int, Class<*>>()
        val availableTypeIndex = mutableMapOf<Class<*>, Int>()
        list.forEachIndexed { index, clazz ->
            availableTypes[index] = clazz
            availableTypeIndex[clazz] = index
        }
        this.availableTypes=availableTypes
        this.availableTypeIndex=availableTypeIndex
    }


}