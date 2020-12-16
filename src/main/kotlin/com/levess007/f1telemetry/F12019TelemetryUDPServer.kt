package com.levess007.f1telemetry

import com.levess007.f1telemetry.data.Packet
import com.levess007.f1telemetry.util.PacketDeserializer
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.function.Consumer


class F12019TelemetryUDPServer(
    val bindAddress: String,
    val port: Int,
    val packetConsumer: Consumer<Packet>
) : Runnable {
    private var running = true
    private val MAX_PACKET_SIZE = 1347

    fun close() {
        running = false
    }

    fun receiveUDP() {
        var buffer = ByteArray(MAX_PACKET_SIZE)
        var socket: DatagramSocket? = null
        //Keep a socket open to listen to all the UDP traffic that is destined for this port
        socket = DatagramSocket(port, InetAddress.getByName(bindAddress))
        while (running) {
            try {
                val packet = DatagramPacket(buffer, buffer.size)
                socket.soTimeout = 1000
                socket.receive(packet)
                buffer = ByteArray(MAX_PACKET_SIZE)
                val nicepacket: Packet = PacketDeserializer.read(packet.data)
                packetConsumer.accept(nicepacket)
            } catch (e: Exception) {
                println("No packet")
            }
        }
        socket.close()
    }

    override fun run() {
        checkNotNull(packetConsumer) { "You must define how the packets will be consumed." }
        receiveUDP()
    }
}