package com.levess007.f1telemetry.util

import com.levess007.f1telemetry.data.*
import com.levess007.f1telemetry.data.elements.*
import java.util.*

class PacketDeserializer(data: ByteArray) {
    private val buffer: PacketBuffer = PacketBuffer.wrap(data)
    private fun buildPacket(): Packet {
        val header = buildHeader()
        if (header.packetId == 0)
            return buildPacketMotionData(header)
        else if (header.packetId == 1)
            return buildPacketSessionData(header)
        else if (header.packetId == 2)
            return buildPacketLapData(header)
        else if (header.packetId == 3)
            return buildPacketEventData(header)
        else if (header.packetId == 4)
            return buildPacketParticipantsData(header)
        else if (header.packetId == 5)
            return buildPacketCarSetupData(header)
        else if (header.packetId == 6)
            return buildPacketCarTelemetryData(header)
        else
            return buildPacketCarStatusData(header)
    }

    private fun buildHeader(): Header {
        return Header(
            buffer.nextUInt16AsInt, // 2
            buffer.nextUInt8AsInt, // 1
            buffer.nextUInt8AsInt, // 1
            buffer.nextUInt8AsInt, // 1
            buffer.nextUInt8AsInt, // 1
            buffer.nextUInt64AsBigInteger, // 8
            buffer.nextFloat, // 4
            buffer.nextUIntAsLong, // 4
            buffer.nextUInt8AsInt // 1
        )
    }

    private fun buildPacketLapData(header: Header): PacketLapData {
        val lapDataList: MutableList<LapData> = ArrayList()
        val playersIndex = header.playerCarIndex
        for (i in 0..TOTAL_NBR_CARS) {
            lapDataList.add(buildLapData(i, i == playersIndex))
        }
        return PacketLapData(header, lapDataList)
    }

    private fun buildLapData(carIndex: Int, playersCar: Boolean): LapData {
        return LapData(
            carIndex.toFloat(),
            ResultStatus.fromInt(buffer.nextUInt8AsInt),
            playersCar,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            PitStatus.fromInt(buffer.nextUInt8AsInt),
            buffer.nextUInt8AsInt + 1,
            buffer.nextInt8AsInt == 1,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            DriverStatus.fromInt(buffer.nextUInt8AsInt),
        )
    }

    private fun buildPacketMotionData(header: Header): PacketMotionData {
        val carMotionDataList: MutableList<CarMotionData> = ArrayList()
        var carIndex = 0
        val playersCarIndex = header.playerCarIndex
        while (carIndex < TOTAL_NBR_CARS) {
            carMotionDataList.add(buildCarMotionData(carIndex, carIndex == playersCarIndex))
            carIndex++
        }
        return PacketMotionData(
            header,
            carMotionDataList,
            WheelData(buffer.getNextFloatArray(4)),
            WheelData(buffer.getNextFloatArray(4)),
            WheelData(buffer.getNextFloatArray(4)),
            WheelData(buffer.getNextFloatArray(4)),
            WheelData(buffer.getNextFloatArray(4)),
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat
        )
    }

    private fun buildCarMotionData(carIndex: Int, playersCar: Boolean): CarMotionData {
        val denormalizer = 32767.0f
        return CarMotionData(
            carIndex,
            playersCar,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextUInt16AsInt / denormalizer,
            buffer.nextUInt16AsInt / denormalizer,
            buffer.nextUInt16AsInt / denormalizer,
            buffer.nextUInt16AsInt / denormalizer,
            buffer.nextUInt16AsInt / denormalizer,
            buffer.nextUInt16AsInt / denormalizer,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
        )
    }

    private fun buildPacketSessionData(header: Header): PacketSessionData {
        return PacketSessionData(
            header,
            Weather.fromInt(buffer.nextUInt8AsInt),
            buffer.nextInt8AsInt,
            buffer.nextInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt16AsInt,
            SessionType.fromInt(buffer.nextUInt8AsInt),
            buffer.nextInt8AsInt,
            Era.fromInt(buffer.nextInt8AsInt),
            buffer.nextUInt16AsInt,
            buffer.nextUInt16AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsBoolean,
            buffer.nextUInt8AsBoolean,
            0,
            buffer.nextUInt8AsBoolean,
            buffer.nextInt8AsInt,
            buildMarshalZones(),
            SafetyCarStatus.fromInt(buffer.nextUInt8AsInt),
            buffer.nextUInt8AsBoolean
        )
    }

    private fun buildMarshalZones(): List<MarshalZone> {
        val marshalZones: MutableList<MarshalZone> = ArrayList()
        for (k in 0 until MAX_NBR_MARSHAL_ZONES) {
            marshalZones.add(
                MarshalZone(
                    buffer.nextFloat,
                    ZoneFlag.fromInt(buffer.nextInt8AsInt)
                )
            )
        }
        return marshalZones
    }

    private fun buildPacketEventData(header: Header): PacketEventData {
        val eventData = PacketEventData(header)
        eventData.eventCode = buffer.getNextCharArrayAsString(4)
        return eventData
    }

    private fun buildPacketParticipantsData(header: Header): PacketParticipantsData {
        val numCars = buffer.nextUInt8AsInt
        val participants: MutableList<ParticipantData> = ArrayList()
        for (k in 0 until numCars) {
            participants.add(buildParticipantData())
        }
        return PacketParticipantsData(header, numCars, participants)
    }

    private fun buildParticipantData(): ParticipantData {
        return ParticipantData(
            buffer.nextUInt8AsBoolean,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.getNextCharArrayAsString(48)
        )
    }

    private fun buildPacketCarSetupData(header: Header): PacketCarSetupData {
        val carSetups: MutableList<CarSetupData> = ArrayList()
        for (k in 0 until TOTAL_NBR_CARS) {
            carSetups.add(buildCarSetupData())
        }
        return PacketCarSetupData(header, carSetups)
    }

    private fun buildCarSetupData(): CarSetupData {
        return CarSetupData(
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextUInt8AsInt,
            buffer.nextFloat,
        )
    }

    private fun buildPacketCarTelemetryData(header: Header): PacketCarTelemetryData {
        val carsTelemetry: MutableList<CarTelemetryData> = ArrayList()
        for (k in 0 until TOTAL_NBR_CARS) {
            carsTelemetry.add(buildCarTelemetryData())
        }
        return PacketCarTelemetryData(header, carsTelemetry, buildButtonStatus())
    }

    private fun buildCarTelemetryData(): CarTelemetryData {
        return CarTelemetryData(
            buffer.nextUInt16AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextInt8AsInt,
            buffer.nextUInt16AsInt,
            buffer.nextUInt8AsBoolean,
            buffer.nextUInt8AsInt,
            WheelData(buffer.getNextUInt16ArrayAsIntegerArray(4)),
            WheelData(buffer.getNextUInt16ArrayAsIntegerArray(4)),
            WheelData(buffer.getNextUInt16ArrayAsIntegerArray(4)),
            buffer.nextUInt16AsInt,
            WheelData(buffer.getNextFloatArray(4))
        )
    }

    private fun buildButtonStatus(): ButtonStatus {
        val flags = buffer.nextUIntAsLong
        return ButtonStatus(
            flags and 0x0001 == 1L,
            flags and 0x0002 == 1L,
            flags and 0x0004 == 1L,
            flags and 0x0008 == 1L,
            flags and 0x0010 == 1L,
            flags and 0x0020 == 1L,
            flags and 0x0040 == 1L,
            flags and 0x0080 == 1L,
            flags and 0x0100 == 1L,
            flags and 0x0200 == 1L,
            flags and 0x0400 == 1L,
            flags and 0x0800 == 1L,
            flags and 0x1000 == 1L,
            flags and 0x2000 == 1L,
            flags and 0x4000 == 1L
        )
    }

    private fun buildPacketCarStatusData(header: Header): PacketCarStatusData {
        val carStatuses: MutableList<CarStatusData> = ArrayList()
        for (k in 0 until TOTAL_NBR_CARS) {
            carStatuses.add(buildCarStatusData())
        }
        return PacketCarStatusData(header, carStatuses)
    }

    private fun buildCarStatusData(): CarStatusData {
        return CarStatusData(
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsBoolean,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsBoolean,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextUInt16AsInt,
            buffer.nextUInt16AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            WheelData(buffer.getNextUInt8ArrayAsIntegerArray(4)),
            buffer.nextUInt8AsInt,
            WheelData(buffer.getNextUInt8ArrayAsIntegerArray(4)),
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextUInt8AsInt,
            buffer.nextInt8AsInt,
            buffer.nextFloat,
            buffer.nextUInt8AsInt,
            buffer.nextFloat,
            buffer.nextFloat,
            buffer.nextFloat
        )
    }

    companion object {
        const val TOTAL_NBR_CARS = 20
        const val MAX_NBR_MARSHAL_ZONES = 21

        fun read(data: ByteArray): Packet {
            return PacketDeserializer(data).buildPacket()
        }
    }

}