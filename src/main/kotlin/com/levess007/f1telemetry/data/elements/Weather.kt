package com.levess007.f1telemetry.data.elements

enum class Weather {
    CLEAR, LIGHT_CLOUD, OVERCAST, LIGHT_RAIN, HEAVY_RAIN, STORM;

    companion object {
        fun fromInt(i: Int): Weather {
            return when (i) {
                0 -> CLEAR
                1 -> LIGHT_CLOUD
                2 -> OVERCAST
                3 -> LIGHT_RAIN
                4 -> HEAVY_RAIN
                else -> STORM
            }
        }
    }
}