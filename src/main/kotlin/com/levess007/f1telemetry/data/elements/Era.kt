package com.levess007.f1telemetry.data.elements

enum class Era {
    MODERN, CLASSIC;

    companion object {
        fun fromInt(i: Int): Era? {
            return when (i) {
                0 -> MODERN
                1 -> CLASSIC
                else -> null
            }
        }
    }
}