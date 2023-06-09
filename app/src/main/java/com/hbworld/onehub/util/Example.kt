package com.hbworld.onehub.util

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.print.PrintJobInfo
import android.print.PageRange

class Example(private val context: Context) {

    fun sendPrintJob(printerIpAddress: String, printerPort: Int) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        // Create a print job name
        val printJobName = "My Print Job"

        // Create a print adapter
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
                // Configure the layout of the document
                // Set print attributes, such as page size, margins, etc.
                // Provide the content layout for printing
                val layoutResult = PrintDocumentAdapter.LayoutResultCallback()
                layoutResult.onLayoutFinished(null, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                // Generate the print content and write it to the destination
                // You can convert your document to PDF, image, or other print format
                // and write the data to the provided ParcelFileDescriptor
                // For simplicity, we'll write a sample text here
                val outputStream = ParcelFileDescriptor.AutoCloseOutputStream(destination)
                val printData = "Sample Print Data".toByteArray()
                outputStream.write(printData)
                outputStream.close()
                callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
        }

        // Create print job info builder
        val printJobInfoBuilder = PrintJobInfo.Builder(printJobName)

        // Set the printer's network address and port
        printJobInfoBuilder.setPrinterId("$printerIpAddress:$printerPort")

        // Create print job info
        val printJobInfo = printJobInfoBuilder.build()

        // Start the print job
        val printJob: PrintJob = printManager.print(printJobInfo, printAdapter)
    }
}
