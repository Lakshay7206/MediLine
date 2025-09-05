package com.example.mediline.data.repo


import android.R.attr.textSize
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.mediline.data.model.Form
import java.io.File
import java.io.FileOutputStream



interface DownloadTicketRepo {
    fun generateTicketPdf(context: Context, ticket: Form, doctors: List<String>): File
}
class DownloadTicketRepoImpl : DownloadTicketRepo {

    override fun generateTicketPdf(context: Context, ticket: Form, doctors: List<String>): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(600, 900, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Paints
        val headerPaint = Paint().apply {
            textSize = 18f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }
        val normalPaint = Paint().apply {
            textSize = 12f
            textAlign = Paint.Align.LEFT
        }
        val boldPaint = Paint().apply {
            textSize = 12f
            isFakeBoldText = true
            textAlign = Paint.Align.LEFT
        }
        val linePaint = Paint().apply {
            strokeWidth = 1f
        }

        // --- HEADER ---
        canvas.drawText(
            "ABC MULTISPECIALITY HOSPITAL",
            (pageInfo.pageWidth / 2).toFloat(),
            40f,
            headerPaint
        )
        canvas.drawText(
            "G.T. Road, Murthal (Sonipat), Ph:1234-5678-0123",
            (pageInfo.pageWidth / 2).toFloat(),
            60f,
            normalPaint
        )

        var y = 90f

        // --- PATIENT INFO BOX ---
        canvas.drawLine(30f, y, 570f, y, linePaint) // top line
        y += 20f
        canvas.drawText("UHID No: ${ticket.id}", 40f, y, boldPaint)
        canvas.drawText("Registration Amount:100", 320f, y, boldPaint)

        y += 20f
        canvas.drawText("Pt. Name: ${ticket.name}", 40f, y, boldPaint)
        canvas.drawText("Mobile: ${ticket.phone}", 320f, y, boldPaint)

        y += 20f
        canvas.drawText("Sex:${ticket.sex}", 40f, y, boldPaint)
        canvas.drawText("Address: ${ticket.address}", 320f, y, boldPaint)

        y += 20f
        canvas.drawText("Age: ${ticket.age}", 40f, y, boldPaint)
        canvas.drawText("Date: ${ticket.timeStamp}", 320f, y, boldPaint)

        y += 20f
        canvas.drawText("Consultant: Dr. Manoj Rai", 40f, y, boldPaint)
        canvas.drawText("Department: Physician", 320f, y, boldPaint)

        y += 10f
        canvas.drawLine(30f, y, 570f, y, linePaint) // bottom line

        // --- FACULTY / DOCTOR LIST ---
        y += 30f
        canvas.drawText("Faculty:", 40f, y, boldPaint)
        y += 20f
        doctors.forEach {
            canvas.drawText(it, 60f, y, normalPaint)
            y += 15f
        }


        // --- SPECIAL CLINIC DAYS ---
        y += 20f
        canvas.drawText("Special Clinic Days:", 40f, y, boldPaint)
        y += 20f
        canvas.drawText("Vitiligo Clinic - Monday", 60f, y, normalPaint); y += 15f
        canvas.drawText("Autoimmune Clinic - Tuesday", 60f, y, normalPaint); y += 15f
        canvas.drawText("Psoriasis Clinic - Wednesday", 60f, y, normalPaint); y += 15f
        canvas.drawText("Leprosy Clinic - Thursday", 60f, y, normalPaint); y += 15f
        canvas.drawText("Urticaria Clinic - Friday", 60f, y, normalPaint)

        // --- FOOTER ---
        y = 850f
        canvas.drawLine(30f, y, 570f, y, linePaint)
        y += 20f
        canvas.drawText("Prepared By: ___________", 40f, y, normalPaint)
        canvas.drawText("Date Time: ___________", 320f, y, normalPaint)

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "ticket_${ticket.id}.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }
}

//    override  fun generateTicketPdf(context: Context, ticket: Form): File {
//        val pdfDocument = PdfDocument()
//        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
//        val page = pdfDocument.startPage(pageInfo)
//        val canvas = page.canvas
//        val paint = android.graphics.Paint()
//
//        // Drawing ticket content
//        canvas.drawText("Ticket ID: ${ticket.id}", 10f, 50f, paint)
//        canvas.drawText("Title: ${ticket.ticketNumber}", 10f, 80f, paint)
//        canvas.drawText("Description: ${ticket.departmentId}", 10f, 110f, paint)
//        canvas.drawText("Created At: ${ticket.phone}", 10f, 140f, paint)
//        canvas.drawText("User: ${ticket.name}", 10f, 170f, paint)
//
//        pdfDocument.finishPage(page)
//
//        val file = File(context.cacheDir, "ticket_${ticket.id}.pdf")
//        pdfDocument.writeTo(FileOutputStream(file))
//        pdfDocument.close()
//
//        return file
//    }
//}
