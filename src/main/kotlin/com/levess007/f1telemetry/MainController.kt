package com.levess007.f1telemetry

import com.levess007.f1telemetry.data.Packet
import javafx.scene.chart.XYChart
import tornadofx.*

class MainController : Controller() {

    val loginScreen: MainView by inject()

    var packetnumber = 0

    val server: F12019TelemetryUDPServer =
        F12019TelemetryUDPServer("0.0.0.0", 20777) { x: Packet -> handlePacket(x) }

    var speedLinePoints = listOf(
        XYChart.Data(0 as Number, 10 as Number),
        XYChart.Data(1 as Number, 15 as Number),
    ).asObservable()


    fun handlePacket(p: Packet) {
        if (p.getSpeed() != -1) {
            println(p.getSpeed())
            loginScreen.data1.speed = p.getSpeed().toString()
        }
    }

    fun addPoint() {
        speedLinePoints[0] = XYChart.Data(2 as Number, 13 as Number)
    }

    fun close() {
        server.close()
    }
}
