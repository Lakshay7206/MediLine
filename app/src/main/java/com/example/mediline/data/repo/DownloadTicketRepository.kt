package com.example.mediline.data.repo


import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.mediline.data.model.Form
import java.io.File
import java.io.FileOutputStream

interface DownloadTicketRepo {
    fun generateTicketPdf(context: Context, ticket: Form): File
}

class DownloadTicketRepoImpl: DownloadTicketRepo {

    override  fun generateTicketPdf(context: Context, ticket: Form): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint()

        // Drawing ticket content
        canvas.drawText("Ticket ID: ${ticket.id}", 10f, 50f, paint)
        canvas.drawText("Title: ${ticket.ticketNumber}", 10f, 80f, paint)
        canvas.drawText("Description: ${ticket.departmentId}", 10f, 110f, paint)
        canvas.drawText("Created At: ${ticket.phone}", 10f, 140f, paint)
        canvas.drawText("User: ${ticket.name}", 10f, 170f, paint)

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "ticket_${ticket.id}.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }
}
