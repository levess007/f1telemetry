package com.levess007.f1telemetry.data.elements

data class ParticipantData(
    val isAiControlled: Boolean,
    val driverId: Int,
    val teamId: Int,
    val raceNumber: Int,
    val nationality: Int,
    val name: String?
) 