package com.levess007.f1telemetry

import tornadofx.ItemViewModel

class DataModel(data: Data) :ItemViewModel<Data>(data) {
    val speed = bind(Data::speedProperty)
}