package com.levess007.f1telemetry

import javafx.scene.chart.NumberAxis
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    override val root = VBox()

    private val controller: MainController by inject()

    var data1 = Data("5")

    val model = DataModel(data1)


    override fun onDock() {
        super.onDock()

        data1.speed="6"

        Thread {
            controller.server.run()
        }.start()

        currentWindow?.setOnCloseRequest {
            controller.close()
        }
    }


    init {
        with(root) {

            label(title) {
                addClass(Styles.heading)
            }
            hbox {
                text(model.speed) {
                    fill = Color.BLACK
                    font = Font(20.0)
                }

                button("Start listening") {
                    action {
                        controller.addPoint()
                    }
                }

                linechart("Unit Sales Q2 2016", NumberAxis(), NumberAxis()) {
                    series("title", controller.speedLinePoints)
                }
            }
        }
    }
}