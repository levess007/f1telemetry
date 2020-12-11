package com.levess007.f1telemetry.data.elements

enum class DriverStatus {
    IN_GARAGE, FLYING_LAP, IN_LAP, OUT_LAP, ON_TRACK;

    companion object {
        fun fromInt(i: Int): DriverStatus {
            return when (i) {
                0 -> IN_GARAGE
                1 -> FLYING_LAP
                2 -> IN_LAP
                3 -> OUT_LAP
                else -> ON_TRACK
            }
        }
    }
}