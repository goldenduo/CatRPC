package com.goldenduo.catrpc.types

import org.reflections.Reflections


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


    fun scanAllMessage() {
        val list = mutableListOf<Class<*>>()
        val types = reflections.getTypesAnnotatedWith(CatMessage::class.java)
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