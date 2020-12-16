package com.levess007.f1telemetry

import com.levess007.f1telemetry.data.Packet
import javafx.application.Application
import tornadofx.App


class MyApp : App(MainView::class, Styles::class)

fun main(args: Array<String>) {

    Application.launch(MyApp::class.java, *args)

}
