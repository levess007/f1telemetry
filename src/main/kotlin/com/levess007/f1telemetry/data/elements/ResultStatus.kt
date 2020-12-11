package com.levess007.f1telemetry.data.elements

enum class ResultStatus {
    INVALID, INACTIVE, ACTIVE, FINISHED, DISQUALIFIED, NOT_CLASSIFIED, RETIRED;

    companion object {
        fun fromInt(i: Int): ResultStatus? {
            return when (i) {
                0 -> INVALID
                1 -> INACTIVE
                2 -> ACTIVE
                3 -> FINISHED
                4 -> DISQUALIFIED
                5 -> NOT_CLASSIFIED
                6 -> RETIRED
                else -> null
            }
        }
    }
}