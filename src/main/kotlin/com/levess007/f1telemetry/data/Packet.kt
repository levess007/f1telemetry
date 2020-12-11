package com.levess007.f1telemetry.data

import com.levess007.f1telemetry.data.elements.Header
import com.fasterxml.jackson.databind.ObjectMapper

abstract class Packet {
    var header: Header? = null
    fun toJSON(): String {
        val mapper = ObjectMapper()
        var json = ""
        try {
            json = mapper.writeValueAsString(this)
        } catch (e: Exception) {
            //TODO: Handle this exception
        }
        return json.replace("\\u0000", "")
    }
    override fun toString() :String{
        return header.toString()
    }
}