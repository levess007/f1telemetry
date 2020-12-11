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
        val header = Header()
        header.packetFormat = buffer.nextUInt16AsInt // 2
        header.packetMajVersion = buffer.nextUInt8AsInt // 1
        header.packetMinVersion = buffer.nextUInt8AsInt // 1
        header.packetId = buffer.nextUInt8AsInt // 1
        header.sessionUID = buffer.nextUInt64AsBigInteger // 8
        header.sessionTime = buffer.nextFloat // 4
        header.frameIdentifier = buffer.nextUIntAsLong // 4
        header.playerCarIndex = buffer.nextUInt8AsInt // 1
        return header
    }

    /**
     * LAP DATA PACKET
     *
     * The lap data packet gives details of all the cars in the session.
     *
     * Frequency: Rate as specified in menus
     *
     * Size: 841 bytes
     *
     * <pre>
     * `struct PacketLapData
     * {
     * PacketHeader    m_header;              // Header
     * LapData         m_lapData[20];         // Lap data for all cars on track
     * };
    ` *
    </pre> *
     *
     * @return the PacketLapData pojo
     */
    private fun buildPacketLapData(header: Header): PacketLapData {
        val packetData = PacketLapData()
        val lapDataList: MutableList<LapData> = ArrayList()
        var i = 0
        val playersIndex = header.playerCarIndex
        while (i < TOTAL_NBR_CARS) {
            lapDataList.add(buildLapData(i, i == playersIndex))
            i++
        }
        packetData.header = header
        packetData.lapDataList = lapDataList
        return packetData
    }

    /**
     * LAP DATA
     *
     * <pre>
     * `struct LapData
     * {
     * float       m_lastLapTime;           // Last lap time in seconds
     * float       m_currentLapTime;        // Current time around the lap in seconds
     * float       m_bestLapTime;           // Best lap time of the session in seconds
     * float       m_sector1Time;           // Sector 1 time in seconds
     * float       m_sector2Time;           // Sector 2 time in seconds
     * float       m_lapDistance;           // Distance vehicle is around current lap in metres – could
     * // be negative if line hasn’t been crossed yet
     * float       m_totalDistance;         // Total distance travelled in session in metres – could
     * // be negative if line hasn’t been crossed yet
     * float       m_safetyCarDelta;        // Delta in seconds for safety car
     * uint8       m_carPosition;           // Car race position
     * uint8       m_currentLapNum;         // Current lap number
     * uint8       m_pitStatus;             // 0 = none, 1 = pitting, 2 = in pit area
     * uint8       m_sector;                // 0 = sector1, 1 = sector2, 2 = sector3
     * uint8       m_currentLapInvalid;     // Current lap invalid - 0 = valid, 1 = invalid
     * uint8       m_penalties;             // Accumulated time penalties in seconds to be added
     * uint8       m_gridPosition;          // Grid position the vehicle started the race in
     * uint8       m_driverStatus;          // Status of driver - 0 = in garage, 1 = flying lap
     * // 2 = in lap, 3 = out lap, 4 = on track
     * uint8       m_resultStatus;          // Result status - 0 = invalid, 1 = inactive, 2 = active
     * // 3 = finished, 4 = disqualified, 5 = not classified
     * // 6 = retired
     * };
    ` *
    </pre> *
     */
    private fun buildLapData(carIndex: Int, playersCar: Boolean): LapData {
        val lapData = LapData()
        lapData.carIndex = carIndex.toFloat()
        lapData.isPlayersCar = playersCar
        lapData.lastLapTime = buffer.nextFloat
        lapData.currentLapTime = buffer.nextFloat
        lapData.bestLaptTime = buffer.nextFloat
        lapData.sector1Time = buffer.nextFloat
        lapData.sector2Time = buffer.nextFloat
        lapData.lapDistance = buffer.nextFloat
        lapData.totalDistance = buffer.nextFloat
        lapData.safetyCarDelta = buffer.nextFloat
        lapData.carPosition = buffer.nextUInt8AsInt
        lapData.currentLapNum = buffer.nextUInt8AsInt
        lapData.pitStatus = PitStatus.Companion.fromInt(buffer.nextUInt8AsInt)
        lapData.sector = buffer.nextUInt8AsInt + 1
        lapData.isCurrentLapInvalid = buffer.nextUInt8AsInt == 1
        lapData.penalties = buffer.nextUInt8AsInt
        lapData.gridPosition = buffer.nextUInt8AsInt
        lapData.driverStatus = DriverStatus.Companion.fromInt(buffer.nextUInt8AsInt)
        lapData.resultStatus = ResultStatus.Companion.fromInt(buffer.nextUInt8AsInt)
        return lapData
    }

    /**
     * MOTION PACKET
     *
     * The motion packet gives physics data for all the cars being driven. There is
     * additional data for the car being driven with the goal of being able to drive
     * a motion platform setup.
     *
     * Frequency: Rate as specified in menus
     *
     * Size: 1343 bytes
     *
     * <pre>
     * `struct PacketMotionData
     * {
     * PacketHeader    m_header;               	// Header
     *
     * CarMotionData   m_carMotionData[20];		// Data for all cars on track
     *
     * // Extra player car ONLY data
     * float         m_suspensionPosition[4];       // Note: All wheel arrays have the following order:
     * float         m_suspensionVelocity[4];       // RL, RR, FL, FR
     * float         m_suspensionAcceleration[4];   // RL, RR, FL, FR
     * float         m_wheelSpeed[4];               // Speed of each wheel
     * float         m_wheelSlip[4];                // Slip ratio for each wheel
     * float         m_localVelocityX;              // Velocity in local space
     * float         m_localVelocityY;              // Velocity in local space
     * float         m_localVelocityZ;              // Velocity in local space
     * float         m_angularVelocityX;            // Angular velocity x-component
     * float         m_angularVelocityY;            // Angular velocity y-component
     * float         m_angularVelocityZ;            // Angular velocity z-component
     * float         m_angularAccelerationX;        // Angular velocity x-component
     * float         m_angularAccelerationY;        // Angular velocity y-component
     * float         m_angularAccelerationZ;        // Angular velocity z-component
     * float         m_frontWheelsAngle;            // Current front wheels angle in radians
     * };
    ` *
    </pre> *
     *
     * @return the PacketMotionData pojo
     */
    private fun buildPacketMotionData(header: Header): PacketMotionData {
        val packetMotionData = PacketMotionData()
        packetMotionData.header = header
        val carMotionDataList: MutableList<CarMotionData> = ArrayList()
        var carIndex = 0
        val playersCarIndex = packetMotionData.header!!.playerCarIndex
        while (carIndex < TOTAL_NBR_CARS) {
            carMotionDataList.add(buildCarMotionData(carIndex, carIndex == playersCarIndex))
            carIndex++
        }
        packetMotionData.carMotionDataList = carMotionDataList
        packetMotionData.suspensionPosition = WheelData(buffer.getNextFloatArray(4))
        packetMotionData.suspensionVelocity = WheelData(buffer.getNextFloatArray(4))
        packetMotionData.suspensionAcceleration = WheelData(buffer.getNextFloatArray(4))
        packetMotionData.wheelSpeed = WheelData(buffer.getNextFloatArray(4))
        packetMotionData.wheelSlip = WheelData(buffer.getNextFloatArray(4))
        packetMotionData.localVelocityX = buffer.nextFloat
        packetMotionData.localVelocityY = buffer.nextFloat
        packetMotionData.localVelocityZ = buffer.nextFloat
        packetMotionData.angularVelocityX = buffer.nextFloat
        packetMotionData.angularVelocityY = buffer.nextFloat
        packetMotionData.angularVelocityZ = buffer.nextFloat
        packetMotionData.angularAccelerationX = buffer.nextFloat
        packetMotionData.angularAccelerationY = buffer.nextFloat
        packetMotionData.angularAccelerationZ = buffer.nextFloat
        packetMotionData.frontWheelsAngle = buffer.nextFloat
        return packetMotionData
    }

    /**
     * CAR MOTION DATA
     *
     * N.B. For the normalised vectors below, to convert to float values divide by
     * 32767.0f. 16-bit signed values are used to pack the data and on the
     * assumption that direction values are always between -1.0f and 1.0f.
     *
     * <pre>
     * `struct CarMotionData
     * {
     * float         m_worldPositionX;           // World space X position
     * float         m_worldPositionY;           // World space Y position
     * float         m_worldPositionZ;           // World space Z position
     * float         m_worldVelocityX;           // Velocity in world space X
     * float         m_worldVelocityY;           // Velocity in world space Y
     * float         m_worldVelocityZ;           // Velocity in world space Z
     * int16         m_worldForwardDirX;         // World space forward X direction (normalised)
     * int16         m_worldForwardDirY;         // World space forward Y direction (normalised)
     * int16         m_worldForwardDirZ;         // World space forward Z direction (normalised)
     * int16         m_worldRightDirX;           // World space right X direction (normalised)
     * int16         m_worldRightDirY;           // World space right Y direction (normalised)
     * int16         m_worldRightDirZ;           // World space right Z direction (normalised)
     * float         m_gForceLateral;            // Lateral G-Force component
     * float         m_gForceLongitudinal;       // Longitudinal G-Force component
     * float         m_gForceVertical;           // Vertical G-Force component
     * float         m_yaw;                      // Yaw angle in radians
     * float         m_pitch;                    // Pitch angle in radians
     * float         m_roll;                     // Roll angle in radians
     * };
    ` *
    </pre> *
     */
    private fun buildCarMotionData(carIndex: Int, playersCar: Boolean): CarMotionData {
        val denormalizer = 32767.0f
        val carMotionData = CarMotionData()
        carMotionData.carIndex = carIndex
        carMotionData.isPlayersCar = playersCar
        carMotionData.worldPositionX = buffer.nextFloat
        carMotionData.worldPositionY = buffer.nextFloat
        carMotionData.worldPositionZ = buffer.nextFloat
        carMotionData.worldVelocityX = buffer.nextFloat
        carMotionData.worldVelocityY = buffer.nextFloat
        carMotionData.worldVelocityZ = buffer.nextFloat
        carMotionData.worldForwardDirX = buffer.nextUInt16AsInt / denormalizer
        carMotionData.worldForwardDirY = buffer.nextUInt16AsInt / denormalizer
        carMotionData.worldForwardDirZ = buffer.nextUInt16AsInt / denormalizer
        carMotionData.worldRightDirX = buffer.nextUInt16AsInt / denormalizer
        carMotionData.worldRightDirY = buffer.nextUInt16AsInt / denormalizer
        carMotionData.worldRightDirZ = buffer.nextUInt16AsInt / denormalizer
        carMotionData.setgForceLateral(buffer.nextFloat)
        carMotionData.setgForceLongitudinal(buffer.nextFloat)
        carMotionData.setgForceVertical(buffer.nextFloat)
        carMotionData.yaw = buffer.nextFloat
        carMotionData.pitch = buffer.nextFloat
        carMotionData.roll = buffer.nextFloat
        return carMotionData
    }

    /**
     * SESSION PACKET
     *
     * The session packet includes details about the current session in progress.
     *
     * Frequency: 2 per second
     *
     * Size: 149 bytes
     *
     * <pre>
     * `struct PacketSessionData
     * {
     * PacketHeader    m_header;               // Header
     *
     * uint8           m_weather;              // Weather - 0 = clear, 1 = light cloud, 2 = overcast
     * // 3 = light rain, 4 = heavy rain, 5 = storm
     * int8	    	m_trackTemperature;    	// Track temp. in degrees celsius
     * int8	    	m_airTemperature;      	// Air temp. in degrees celsius
     * uint8           m_totalLaps;           	// Total number of laps in this race
     * uint16          m_trackLength;          // Track length in metres
     * uint8           m_sessionType;         	// 0 = unknown, 1 = P1, 2 = P2, 3 = P3, 4 = Short P
     * // 5 = Q1, 6 = Q2, 7 = Q3, 8 = Short Q, 9 = OSQ
     * // 10 = R, 11 = R2, 12 = Time Trial
     * int8            m_trackId;         		// -1 for unknown, 0-21 for tracks, see appendix
     * uint8           m_era;                  // Era, 0 = modern, 1 = classic
     * uint16          m_sessionTimeLeft;    	// Time left in session in seconds
     * uint16          m_sessionDuration;     	// Session duration in seconds
     * uint8           m_pitSpeedLimit;      	// Pit speed limit in kilometres per hour
     * uint8           m_gamePaused;           // Whether the game is paused
     * uint8           m_isSpectating;        	// Whether the player is spectating
     * uint8           m_spectatorCarIndex;  	// Index of the car being spectated
     * uint8           m_sliProNativeSupport;	// SLI Pro support, 0 = inactive, 1 = active
     * uint8           m_numMarshalZones;      // Number of marshal zones to follow
     * MarshalZone     m_marshalZones[21];     // List of marshal zones – max 21
     * uint8           m_safetyCarStatus;      // 0 = no safety car, 1 = full safety car
     * // 2 = virtual safety car
     * uint8          m_networkGame;           // 0 = offline, 1 = online
     * };
    ` *
    </pre> *
     */
    private fun buildPacketSessionData(header: Header): PacketSessionData {
        val sessionData = PacketSessionData()
        sessionData.header = header
        sessionData.weather = Weather.Companion.fromInt(buffer.nextUInt8AsInt)
        sessionData.trackTemperature = buffer.nextInt8AsInt
        sessionData.airTemperature = buffer.nextInt8AsInt
        sessionData.totalLaps = buffer.nextUInt8AsInt
        sessionData.trackLength = buffer.nextUInt16AsInt
        sessionData.sessionType = SessionType.Companion.fromInt(buffer.nextUInt8AsInt)
        sessionData.trackId = buffer.nextInt8AsInt
        sessionData.era = Era.Companion.fromInt(buffer.nextInt8AsInt)
        sessionData.sessionTimeLeft = buffer.nextUInt16AsInt
        sessionData.sessionDuration = buffer.nextUInt16AsInt
        sessionData.pitSpeedLimit = buffer.nextUInt8AsInt
        sessionData.isGamePaused = buffer.nextUInt8AsBoolean
        sessionData.isSpectating = buffer.nextUInt8AsBoolean
        sessionData.isSliProNativeSupport = buffer.nextUInt8AsBoolean
        sessionData.numMarshalZones = buffer.nextInt8AsInt
        sessionData.marshalZones = buildMarshalZones()
        sessionData.safetyCarStatus = SafetyCarStatus.Companion.fromInt(buffer.nextUInt8AsInt)
        sessionData.isNetworkGame = buffer.nextUInt8AsBoolean
        return sessionData
    }

    /**
     * MARSHAL ZONE
     *
     * <pre>
     * `struct MarshalZone
     * {
     * float  m_zoneStart;   // Fraction (0..1) of way through the lap the marshal zone starts
     * int8   m_zoneFlag;    // -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red
     * };
    ` *
    </pre> *
     */
    private fun buildMarshalZones(): List<MarshalZone> {
        val marshalZones: MutableList<MarshalZone> = ArrayList()
        for (k in 0 until MAX_NBR_MARSHAL_ZONES) {
            val marshalZone = MarshalZone()
            marshalZone.zoneStart = buffer.nextFloat
            marshalZone.zoneFlag = ZoneFlag.Companion.fromInt(buffer.nextInt8AsInt)
            marshalZones.add(marshalZone)
        }
        return marshalZones
    }

    /**
     * EVENT PACKET
     *
     * This packet gives details of events that happen during the course of the
     * race.
     *
     * Frequency: When the event occurs
     *
     * Size: 32 bytes
     *
     * <pre>
     * `struct PacketEventData
     * {
     * PacketHeader    m_header;               // Header
     *
     * uint8           m_eventStringCode[4];   // Event string code, see above
     * };
    ` *
    </pre> *
     *
     * @param header
     * @return the EventData packet
     */
    private fun buildPacketEventData(header: Header): PacketEventData {
        val eventData = PacketEventData()
        eventData.header = header
        eventData.eventCode = buffer.getNextCharArrayAsString(4)
        return eventData
    }

    /**
     * PARTICIPANTS PACKET
     *
     * This is a list of participants in the race. If the vehicle is controlled by
     * AI, then the name will be the driver name. If this is a multiplayer game, the
     * names will be the Steam Id on PC, or the LAN name if appropriate. On Xbox
     * One, the names will always be the driver name, on PS4 the name will be the
     * LAN name if playing a LAN game, otherwise it will be the driver name.
     *
     * Frequency: Every 5 seconds
     *
     * Size: 1082 bytes
     *
     * <pre>
     * `struct PacketParticipantsData
     * {
     * PacketHeader    m_header;            // Header
     *
     * uint8           m_numCars;           // Number of cars in the data
     * ParticipantData m_participants[20];
     * };
    ` *
    </pre> *
     *
     * @param header
     * @return a PacketParticipantsData pojo
     */
    private fun buildPacketParticipantsData(header: Header): PacketParticipantsData {
        val participantsData = PacketParticipantsData()
        participantsData.header = header
        participantsData.numCars = buffer.nextUInt8AsInt
        val participants: MutableList<ParticipantData> = ArrayList()
        for (k in 0 until participantsData.numCars) {
            participants.add(buildParticipantData())
        }
        participantsData.participants = participants
        // Ignore the rest of the data in the buffer
        return participantsData
    }

    /**
     * PARTICIPANT DATA
     *
     * <pre>
     * `struct ParticipantData
     * {
     * uint8      m_aiControlled;           // Whether the vehicle is AI (1) or Human (0) controlled
     * uint8      m_driverId;               // Driver id - see appendix
     * uint8      m_teamId;                 // Team id - see appendix
     * uint8      m_raceNumber;             // Race number of the car
     * uint8      m_nationality;            // Nationality of the driver
     * char       m_name[48];               // Name of participant in UTF-8 format – null terminated
     * // Will be truncated with … (U+2026) if too long
     * }; `
     *
     * @return a ParticipantData pojo
    </pre> */
    private fun buildParticipantData(): ParticipantData {
        val participant = ParticipantData()
        participant.isAiControlled = buffer.nextUInt8AsBoolean
        participant.driverId = buffer.nextUInt8AsInt
        participant.teamId = buffer.nextUInt8AsInt
        participant.raceNumber = buffer.nextUInt8AsInt
        participant.nationality = buffer.nextUInt8AsInt
        participant.name = buffer.getNextCharArrayAsString(48)
        return participant
    }

    /**
     * CAR SETUPS PACKET
     *
     * This packet details the car setups for each vehicle in the session. Note that
     * in multiplayer games, other player cars will appear as blank, you will only
     * be able to see your car setup and AI cars.
     *
     * Frequency: Every 5 seconds
     *
     * Size: 841 bytes
     *
     * <pre>
     * `struct PacketCarSetupData
     * {
     * PacketHeader    m_header;            // Header
     *
     * CarSetupData    m_carSetups[20];
     * };
    ` *
    </pre> *
     *
     * @param header
     * @return
     */
    private fun buildPacketCarSetupData(header: Header): PacketCarSetupData {
        val carSetupData = PacketCarSetupData()
        carSetupData.header = header
        val carSetups: MutableList<CarSetupData> = ArrayList()
        for (k in 0 until TOTAL_NBR_CARS) {
            carSetups.add(buildCarSetupData())
        }
        carSetupData.carSetups = carSetups
        return carSetupData
    }

    /**
     * CAR SETUP DATA
     *
     * <pre>
     * `struct CarSetupData
     * {
     * uint8     m_frontWing;                // Front wing aero
     * uint8     m_rearWing;                 // Rear wing aero
     * uint8     m_onThrottle;               // Differential adjustment on throttle (percentage)
     * uint8     m_offThrottle;              // Differential adjustment off throttle (percentage)
     * float     m_frontCamber;              // Front camber angle (suspension geometry)
     * float     m_rearCamber;               // Rear camber angle (suspension geometry)
     * float     m_frontToe;                 // Front toe angle (suspension geometry)
     * float     m_rearToe;                  // Rear toe angle (suspension geometry)
     * uint8     m_frontSuspension;          // Front suspension
     * uint8     m_rearSuspension;           // Rear suspension
     * uint8     m_frontAntiRollBar;         // Front anti-roll bar
     * uint8     m_rearAntiRollBar;          // Front anti-roll bar
     * uint8     m_frontSuspensionHeight;    // Front ride height
     * uint8     m_rearSuspensionHeight;     // Rear ride height
     * uint8     m_brakePressure;            // Brake pressure (percentage)
     * uint8     m_brakeBias;                // Brake bias (percentage)
     * float     m_frontTyrePressure;        // Front tyre pressure (PSI)
     * float     m_rearTyrePressure;         // Rear tyre pressure (PSI)
     * uint8     m_ballast;                  // Ballast
     * float     m_fuelLoad;                 // Fuel load
     * };
    ` *
    </pre> *
     *
     * @return a CarSetupData pojo
     */
    private fun buildCarSetupData(): CarSetupData {
        val setupData = CarSetupData()
        setupData.frontWing = buffer.nextUInt8AsInt // 1*
        setupData.rearWing = buffer.nextUInt8AsInt // 2*
        setupData.onThrottle = buffer.nextUInt8AsInt // 3*
        setupData.offThrottle = buffer.nextUInt8AsInt // 4*
        setupData.frontCamber = buffer.nextFloat // 8 *
        setupData.rearCamber = buffer.nextFloat // 16*
        setupData.frontToe = buffer.nextFloat // 24*
        setupData.rearToe = buffer.nextFloat // 32*
        setupData.frontSuspension = buffer.nextUInt8AsInt // 33*
        setupData.rearSuspension = buffer.nextUInt8AsInt // 34*
        setupData.frontAntiRollBar = buffer.nextUInt8AsInt // 35*
        setupData.rearAntiRollBar = buffer.nextUInt8AsInt // 36*
        setupData.frontSuspensionHeight = buffer.nextUInt8AsInt // 37*
        setupData.rearSuspensionHeight = buffer.nextUInt8AsInt // 38*
        setupData.brakePressure = buffer.nextUInt8AsInt
        setupData.brakeBias = buffer.nextUInt8AsInt // 39
        setupData.frontTirePressure = buffer.nextFloat // 47
        setupData.rearTirePressure = buffer.nextFloat // 55
        setupData.ballast = buffer.nextUInt8AsInt // 56
        setupData.fuelLoad = buffer.nextFloat // 40
        return setupData
    }

    /**
     * CAR TELEMETRY PACKET
     *
     * This packet details telemetry for all the cars in the race. It details
     * various values that would be recorded on the car such as speed, throttle
     * application, DRS etc.
     *
     * Frequency: Rate as specified in menus
     *
     * Size: 1085 bytes
     *
     * <pre>
     * `struct PacketCarTelemetryData
     * {
     * PacketHeader        m_header;                // Header
     *
     * CarTelemetryData    m_carTelemetryData[20];
     *
     * uint32              m_buttonStatus;         // Bit flags specifying which buttons are being
     * // pressed currently - see appendices
     * };
    ` *
    </pre> *
     *
     * @param header
     * @return a PacketCarTelemetryData pojo
     */
    private fun buildPacketCarTelemetryData(header: Header): PacketCarTelemetryData {
        val packetCarTelemetry = PacketCarTelemetryData()
        packetCarTelemetry.header = header
        val carsTelemetry: MutableList<CarTelemetryData> = ArrayList()
        for (k in 0 until TOTAL_NBR_CARS) {
            carsTelemetry.add(buildCarTelemetryData())
        }
        packetCarTelemetry.buttonStatus = buildButtonStatus()
        packetCarTelemetry.carTelemetryData = carsTelemetry
        return packetCarTelemetry
    }

    /**
     * CAR TELEMETRY DATA
     *
     * <pre>
     * `struct CarTelemetryData
     * {
     * uint16    m_speed;                      // Speed of car in kilometres per hour
     * uint8     m_throttle;                   // Amount of throttle applied (0 to 100)
     * int8      m_steer;                      // Steering (-100 (full lock left) to 100 (full lock right))
     * uint8     m_brake;                      // Amount of brake applied (0 to 100)
     * uint8     m_clutch;                     // Amount of clutch applied (0 to 100)
     * int8      m_gear;                       // Gear selected (1-8, N=0, R=-1)
     * uint16    m_engineRPM;                  // Engine RPM
     * uint8     m_drs;                        // 0 = off, 1 = on
     * uint8     m_revLightsPercent;           // Rev lights indicator (percentage)
     * uint16    m_brakesTemperature[4];       // Brakes temperature (celsius)
     * uint16    m_tyresSurfaceTemperature[4]; // Tyres surface temperature (celsius)
     * uint16    m_tyresInnerTemperature[4];   // Tyres inner temperature (celsius)
     * uint16    m_engineTemperature;          // Engine temperature (celsius)
     * float     m_tyresPressure[4];           // Tyres pressure (PSI)
     * };
    ` *
    </pre> *
     *
     * @return a CarTelemetryData pojo
     */
    private fun buildCarTelemetryData(): CarTelemetryData {
        val carTelemetry = CarTelemetryData()
        carTelemetry.speed = buffer.nextUInt16AsInt
        carTelemetry.throttle = buffer.nextUInt8AsInt
        carTelemetry.steer = buffer.nextInt8AsInt
        carTelemetry.brake = buffer.nextUInt8AsInt
        carTelemetry.clutch = buffer.nextUInt8AsInt
        carTelemetry.gear = buffer.nextInt8AsInt
        carTelemetry.engineRpm = buffer.nextUInt16AsInt
        carTelemetry.isDrs = buffer.nextUInt8AsBoolean
        carTelemetry.revLightsPercent = buffer.nextUInt8AsInt
        carTelemetry.brakeTemperature = WheelData(buffer.getNextUInt16ArrayAsIntegerArray(4))
        carTelemetry.tireSurfaceTemperature = WheelData(buffer.getNextUInt16ArrayAsIntegerArray(4))
        carTelemetry.tireInnerTemperature = WheelData(buffer.getNextUInt16ArrayAsIntegerArray(4))
        carTelemetry.engineTemperature = buffer.nextUInt16AsInt
        carTelemetry.tirePressure = WheelData(buffer.getNextFloatArray(4))
        return carTelemetry
    }

    /**
     * BUTTON FLAGS
     *
     * These flags are used in the telemetry packet to determine if any buttons are
     * being held on the controlling device. If the value below logical ANDed with
     * the button status is set then the corresponding button is being held
     *
     * @return the ButtonStatus pojo
     */
    private fun buildButtonStatus(): ButtonStatus {
        val flags = buffer.nextUIntAsLong
        val controller = ButtonStatus()
        controller.isCrossAPressed = flags and 0x0001 == 1L
        controller.isTriangleYPressed = flags and 0x0002 == 1L
        controller.isCircleBPressed = flags and 0x0004 == 1L
        controller.isSquareXPressed = flags and 0x0008 == 1L
        controller.isDpadLeftPressed = flags and 0x0010 == 1L
        controller.isDpadRightPressed = flags and 0x0020 == 1L
        controller.isDpadUpPressed = flags and 0x0040 == 1L
        controller.isDpadDownPressed = flags and 0x0080 == 1L
        controller.isOptionsMenuPressed = flags and 0x0100 == 1L
        controller.isL1LBPressed = flags and 0x0200 == 1L
        controller.isR1RBPressed = flags and 0x0400 == 1L
        controller.isL2LTPressed = flags and 0x0800 == 1L
        controller.isR2RTPressed = flags and 0x1000 == 1L
        controller.isLeftStickPressed = flags and 0x2000 == 1L
        controller.isRightStickPressed = flags and 0x4000 == 1L
        return controller
    }

    /**
     * CAR STATUS PACKET
     *
     * This packet details car statuses for all the cars in the race. It includes
     * values such as the damage readings on the car.
     *
     * Frequency: 2 per second
     *
     * Size: 1061 bytes
     *
     * <pre>
     * `struct PacketCarStatusData
     * {
     * PacketHeader        m_header;            // Header
     *
     * CarStatusData       m_carStatusData[20];
     * };
    ` *
    </pre> *
     *
     * @param header
     * @return a PacketCarStatusData packet
     */
    private fun buildPacketCarStatusData(header: Header): PacketCarStatusData {
        val packetCarStatus = PacketCarStatusData()
        packetCarStatus.header = header
        val carStatuses: MutableList<CarStatusData> = ArrayList()
        for (k in 0 until TOTAL_NBR_CARS) {
            carStatuses.add(buildCarStatusData())
        }
        packetCarStatus.carStatuses = carStatuses
        return packetCarStatus
    }

    /**
     * CAR STATUS DATA
     *
     * <pre>
     * `struct CarStatusData
     * {
     * uint8       m_tractionControl;          // 0 (off) - 2 (high)
     * uint8       m_antiLockBrakes;           // 0 (off) - 1 (on)
     * uint8       m_fuelMix;                  // Fuel mix - 0 = lean, 1 = standard, 2 = rich, 3 = max
     * uint8       m_frontBrakeBias;           // Front brake bias (percentage)
     * uint8       m_pitLimiterStatus;         // Pit limiter status - 0 = off, 1 = on
     * float       m_fuelInTank;               // Current fuel mass
     * float       m_fuelCapacity;             // Fuel capacity
     * uint16      m_maxRPM;                   // Cars max RPM, point of rev limiter
     * uint16      m_idleRPM;                  // Cars idle RPM
     * uint8       m_maxGears;                 // Maximum number of gears
     * uint8       m_drsAllowed;               // 0 = not allowed, 1 = allowed, -1 = unknown
     * uint8       m_tyresWear[4];             // Tyre wear percentage
     * uint8       m_tyreCompound;             // Modern - 0 = hyper soft, 1 = ultra soft
     * // 2 = super soft, 3 = soft, 4 = medium, 5 = hard
     * // 6 = super hard, 7 = inter, 8 = wet
     * // Classic - 0-6 = dry, 7-8 = wet
     * uint8       m_tyresDamage[4];           // Tyre damage (percentage)
     * uint8       m_frontLeftWingDamage;      // Front left wing damage (percentage)
     * uint8       m_frontRightWingDamage;     // Front right wing damage (percentage)
     * uint8       m_rearWingDamage;           // Rear wing damage (percentage)
     * uint8       m_engineDamage;             // Engine damage (percentage)
     * uint8       m_gearBoxDamage;            // Gear box damage (percentage)
     * uint8       m_exhaustDamage;            // Exhaust damage (percentage)
     * int8        m_vehicleFiaFlags;          // -1 = invalid/unknown, 0 = none, 1 = green
     * // 2 = blue, 3 = yellow, 4 = red
     * float       m_ersStoreEnergy;           // ERS energy store in Joules
     * uint8       m_ersDeployMode;            // ERS deployment mode, 0 = none, 1 = low, 2 = medium
     * // 3 = high, 4 = overtake, 5 = hotlap
     * float       m_ersHarvestedThisLapMGUK;  // ERS energy harvested this lap by MGU-K
     * float       m_ersHarvestedThisLapMGUH;  // ERS energy harvested this lap by MGU-H
     * float       m_ersDeployedThisLap;       // ERS energy deployed this lap
     * };
    ` *
    </pre> *
     *
     * @return a CarStatusData pojo
     */
    private fun buildCarStatusData(): CarStatusData {
        val carStatus = CarStatusData()
        carStatus.tractionControl = buffer.nextUInt8AsInt
        carStatus.isAntiLockBrakes = buffer.nextUInt8AsBoolean
        carStatus.fuelMix = buffer.nextUInt8AsInt
        carStatus.frontBrakeBias = buffer.nextUInt8AsInt
        carStatus.isPitLimiterOn = buffer.nextUInt8AsBoolean
        carStatus.fuelInTank = buffer.nextFloat
        carStatus.fuelCapacity = buffer.nextFloat
        carStatus.maxRpm = buffer.nextUInt16AsInt
        carStatus.idleRpm = buffer.nextUInt16AsInt
        carStatus.maxGears = buffer.nextUInt8AsInt
        carStatus.drsAllowed = buffer.nextUInt8AsInt
        carStatus.tiresWear = WheelData(buffer.getNextUInt8ArrayAsIntegerArray(4))
        carStatus.tireCompound = buffer.nextUInt8AsInt
        carStatus.tiresDamage = WheelData(buffer.getNextUInt8ArrayAsIntegerArray(4))
        carStatus.frontLeftWheelDamage = buffer.nextUInt8AsInt
        carStatus.frontRightWingDamage = buffer.nextUInt8AsInt
        carStatus.rearWingDamage = buffer.nextUInt8AsInt
        carStatus.engineDamage = buffer.nextUInt8AsInt
        carStatus.gearBoxDamage = buffer.nextUInt8AsInt
        carStatus.exhaustDamage = buffer.nextUInt8AsInt
        carStatus.vehicleFiaFlags = buffer.nextInt8AsInt
        carStatus.ersStoreEngery = buffer.nextFloat
        carStatus.ersDeployMode = buffer.nextUInt8AsInt
        carStatus.ersHarvestedThisLapMGUK = buffer.nextFloat
        carStatus.ersHarvestedThisLapMGUH = buffer.nextFloat
        carStatus.ersDeployedThisLap = buffer.nextFloat
        return carStatus
    }

    companion object {
        const val TOTAL_NBR_CARS = 20
        const val MAX_NBR_MARSHAL_ZONES = 21

        /**
         * Read the packet data from a byte array
         *
         * @param data : a F1 2018 UDP packet
         * @return a Packet POJO
         */
        fun read(data: ByteArray): Packet? {
            return PacketDeserializer(data).buildPacket()
        }
    }

}