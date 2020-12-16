package com.levess007.f1telemetry.data.elements

data class ButtonStatus(
    val isCrossAPressed: Boolean,
    val isTriangleYPressed: Boolean,
    val isCircleBPressed: Boolean,
    val isSquareXPressed: Boolean,
    val isDpadLeftPressed: Boolean,
    val isDpadRightPressed: Boolean,
    val isDpadUpPressed: Boolean,
    val isDpadDownPressed: Boolean,
    val isOptionsMenuPressed: Boolean,
    val isL1LBPressed: Boolean,
    val isR1RBPressed: Boolean,
    val isL2LTPressed: Boolean,
    val isR2RTPressed: Boolean,
    val isLeftStickPressed: Boolean,
    val isRightStickPressed: Boolean
)