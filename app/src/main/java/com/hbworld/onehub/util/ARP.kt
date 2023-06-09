package com.hbworld.onehub.util

import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJobInfo
import java.net.NetworkInterface

interface ARP {

    fun readARPCache()


    class Impl() : ARP , PrintDocumentAdapter {

        override fun onLayout(
            oldAttributes: PrintAttributes?,
            newAttributes: PrintAttributes?,
            cancellationSignal: CancellationSignal?,
            callback: LayoutResultCallback?,
            extras: Bundle?
        ) {
            TODO("Not yet implemented")
        }

        override fun onWrite(
            pages: Array<out PageRange>?,
            destination: ParcelFileDescriptor?,
            cancellationSignal: CancellationSignal?,
            callback: WriteResultCallback?
        ) {
            TODO("Not yet implemented")
        }

        override fun readARPCache() {

            // Create print job info builder
            val printJobInfoBuilder = PrintJobInfo.Builder("printJobName")

            // Set the printer's network address and port
            printJobInfoBuilder.setPrinterId("$printerIpAddress:$printerPort")
            try {
                val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                while (networkInterfaces.hasMoreElements()) {
                    val networkInterface = networkInterfaces.nextElement()
                    val hardwareAddress = networkInterface.hardwareAddress
                    if (hardwareAddress != null) {
                        val inetAddresses = networkInterface.inetAddresses
                        while (inetAddresses.hasMoreElements()) {
                            val inetAddress = inetAddresses.nextElement()
                            if (!inetAddress.isLoopbackAddress) {
//                                val ipAddress = inetAddress.address
//                                if (ipAddress.size == 4) {
                                    val ipAddressString = inetAddress.hostAddress
                                    val macAddressString = convertBytesToMACAddress(hardwareAddress)
                                    println("IP Address: $ipAddressString")
                                    println("MAC Address: $macAddressString")
//                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun convertBytesToMACAddress(macAddressBytes: ByteArray): String {
            val macAddressBuilder = StringBuilder()
            for (b in macAddressBytes) {
                macAddressBuilder.append(String.format("%02X:", b))
            }
            if (macAddressBuilder.isNotEmpty()) {
                macAddressBuilder.deleteCharAt(macAddressBuilder.length - 1)
            }
            return macAddressBuilder.toString()
        }

    }
}


