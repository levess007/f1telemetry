package com.levess007.f1telemetry.data.elements

enum class SessionType {
    UNKNOWN, P1, P2, P3, SHORT_P, Q1, Q2, Q3, SHORT_Q, OSQ, R, R2, TIME_TRIAL;

    companion object {
        fun fromInt(i: Int): SessionType? {
            return when (i) {
                0 -> UNKNOWN
                1 -> P1
                2 -> P2
                3 -> P3
                4 -> SHORT_P
                5 -> Q1
                6 -> Q2
                7 -> Q3
                8 -> SHORT_Q
                9 -> OSQ
                10 -> R
                11 -> R2
                12 -> TIME_TRIAL
                else -> null
            }
        }
    }
}