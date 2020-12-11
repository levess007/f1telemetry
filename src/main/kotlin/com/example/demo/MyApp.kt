package com.example.demo

import com.example.demo.main.MainView
import javafx.application.Application
import tornadofx.App

class MyApp : App(MainView::class, Styles::class)

fun main(args: Array<String>) {
    Application.launch(MyApp::class.java, *args)
    print("megy")
}
