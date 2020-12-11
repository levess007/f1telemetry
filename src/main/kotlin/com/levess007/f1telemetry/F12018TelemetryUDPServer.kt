package com.levess007.f1telemetry

import com.levess007.f1telemetry.data.Packet
import com.levess007.f1telemetry.util.PacketDeserializer
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.DatagramChannel
import java.util.concurrent.Executors
import java.util.function.Consumer

/**
 * The base class for the F1 2018 Telemetry app. Starts up a non-blocking I/O
 * UDP server to read packets from the F1 2018 video game and then hands those
 * packets off to a parallel thread for processing based on the lambda function
 * defined. Leverages a fluent API for initialization.
 *
 *
 * Also exposes a main method for starting up a default server
 *
 * @author eh7n
 */
class F12018TelemetryUDPServer private constructor() {
    private var bindAddress: String
    private var port: Int
    private var packetConsumer: Consumer<Packet?>? = null

    /**
     * Set the bind address
     *
     * @param bindAddress
     * @return the server instance
     */
    fun bindTo(bindAddress: String): F12018TelemetryUDPServer {
        this.bindAddress = bindAddress
        return this
    }

    /**
     * Set the bind port
     *
     * @param port
     * @return the server instance
     */
    fun onPort(port: Int): F12018TelemetryUDPServer {
        this.port = port
        return this
    }

    /**
     * Set the consumer via a lambda function
     *
     * @param consumer
     * @return the server instance
     */
    fun consumeWith(consumer: Consumer<Packet?>?): F12018TelemetryUDPServer {
        packetConsumer = consumer
        return this
    }

    /**
     * Start the F1 2018 Telemetry UDP server
     *
     * @throws IOException           if the server fails to start
     * @throws IllegalStateException if you do not define how the packets should be
     * consumed
     */
    @Throws(IOException::class)
    fun start() {
        checkNotNull(packetConsumer) { "You must define how the packets will be consumed." }
        println("F1 2018 - Telemetry UDP Server")

        // Create an executor to process the Packets in a separate thread
        // To be honest, this is probably an over-optimization due to the use of NIO,
        // but it was done to provide a simple way of providing back pressure on the
        // incoming UDP packet handling to allow for long-running processing of the
        // Packet object, if required.
        val executor = Executors.newSingleThreadExecutor()
        try {
            DatagramChannel.open().use { channel ->
                channel.socket().bind(InetSocketAddress(bindAddress, port))
                println("Listening on $bindAddress:$port...")
                val buf = ByteBuffer.allocate(MAX_PACKET_SIZE)
                buf.order(ByteOrder.LITTLE_ENDIAN)
                while (true) {
                    channel.receive(buf)
                    val packet: Packet? = PacketDeserializer.Companion.read(buf.array())
                    executor.submit { packetConsumer!!.accept(packet) }
                    buf.clear()
                }
            }
        } finally {
            executor.shutdown()
        }
    }

    companion object {
        private const val DEFAULT_BIND_ADDRESS = "0.0.0.0"
        private const val DEFAULT_PORT = 20777
        private const val MAX_PACKET_SIZE = 1341

        /**
         * Create an instance of the UDP server
         *
         * @return
         */
        fun create(): F12018TelemetryUDPServer {
            return F12018TelemetryUDPServer()
        }

        /**
         * Main class in case you want to run the server independently. Uses defaults
         * for bind address and port, and just logs the incoming packets as a JSON
         * object to the location defined in the logback config
         *
         * @param args
         * @throws IOException
         */
        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            create()
                .bindTo("0.0.0.0")
                .onPort(20777)
                .consumeWith { x: Packet? -> println(x) }
                .start()
        }
    }

    init {
        bindAddress = DEFAULT_BIND_ADDRESS
        port = DEFAULT_PORT
    }
}