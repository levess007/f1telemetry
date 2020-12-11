package com.levess007.f1telemetry.data.elements

class WheelData<T> {
    var rearLeft: T? = null
        private set
    var rearRight: T? = null
        private set
    var frontLeft: T? = null
        private set
    var frontRight: T? = null
        private set

    constructor() {}
    constructor(datapoints: Array<T>?) {
        setRearLeft(datapoints!![0])
        setRearRight(datapoints[1])
        setFrontLeft(datapoints[2])
        setFrontRight(datapoints[3])
    }

    constructor(rl: T, rr: T, fl: T, fr: T) {
        setRearLeft(rl)
        setRearRight(rr)
        setFrontLeft(fl)
        setFrontRight(fr)
    }

    fun setRearLeft(rearLeft: T) {
        this.rearLeft = rearLeft
    }

    fun setRearRight(rearRight: T) {
        this.rearRight = rearRight
    }

    fun setFrontLeft(frontLeft: T) {
        this.frontLeft = frontLeft
    }

    fun setFrontRight(frontRight: T) {
        this.frontRight = frontRight
    }
}