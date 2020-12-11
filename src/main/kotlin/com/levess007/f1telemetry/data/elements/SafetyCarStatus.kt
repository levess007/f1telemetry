package com.levess007.f1telemetry.data.elements

enum class SafetyCarStatus {
    NO_SAFETY_CAR, FULL_SAFETY_CAR, VIRTUAL_SAFETY_CAR;

    companion object {
        fun fromInt(i: Int): SafetyCarStatus? {
            return when (i) {
                0 -> NO_SAFETY_CAR
                1 -> FULL_SAFETY_CAR
                2 -> VIRTUAL_SAFETY_CAR
                else -> null
            }
        }
    }
}