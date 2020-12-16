package com.levess007.f1telemetry

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Data(speed: String? = null) {
    val speedProperty = SimpleStringProperty(this, "speed", speed)
    var speed by speedProperty

}