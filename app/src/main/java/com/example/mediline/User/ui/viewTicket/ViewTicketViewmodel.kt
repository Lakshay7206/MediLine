package com.example.mediline.User.ui.viewTicket

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.dl.GetDepartmentsUseCase
import com.example.mediline.dl.GetTicketsUseCase
import com.example.mediline.dl.generatePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ViewTicketViewModel @Inject constructor(
    private val getTicketsUseCase: GetTicketsUseCase,
    private val generatePdfUseCase: generatePdfUseCase,
    private val getDepartmentsUseCase: GetDepartmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TicketUiState>(TicketUiState.Loading)
    val uiState: StateFlow<TicketUiState> = _uiState.asStateFlow()

    fun loadTickets() {
        viewModelScope.launch {
            getTicketsUseCase().fold(
                onSuccess = { tickets ->
                    // Change .find to .filter to get a List<Form>
                    val activeTickets = tickets.filter { it.ticketStatus == TicketStatus.ACTIVE ||it.ticketStatus== TicketStatus.SERVING }.sortedByDescending { it.timeStamp }
                    val history = tickets.filter {
                        it.ticketStatus != TicketStatus.ACTIVE && it.ticketStatus != TicketStatus.NULL
                    }.sortedByDescending { it.timeStamp }


//                    val history = tickets.filter { it.ticketStatus != TicketStatus.ACTIVE && it.ticketStatus!= TicketStatus.NULL }
//                        .sortedByDescending { it.timeStamp }

                    // Pass the list to the UI state
                    _uiState.value = TicketUiState.Success(activeTickets, history)
                },
                onFailure = { e ->
                    Log.e("ViewTicketViewModel", "Error loading tickets", e)
                    _uiState.value = TicketUiState.Error(e.message ?: "Failed to load tickets")
                }

            )
        }
    }

    private val _doctors = MutableStateFlow<List<String>>(emptyList())
    val doctors: StateFlow<List<String>> = _doctors

    fun loadDoctors() {
        viewModelScope.launch {
            getDepartmentsUseCase().collect { departments ->
                val doctorList = departments.map { "${it.doctor} (${it.name})" }
                _doctors.value = doctorList
            }
        }
    }

    fun createAndDownloadTicketFile(context: Context, ticket: Form): File? {
        loadDoctors()

        val pdfFile = generatePdfUseCase(context, ticket, doctors.value)


        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val destFile = File(downloadsDir, pdfFile.name)
        try {
            pdfFile.copyTo(destFile, overwrite = true)
        } catch (e: Exception) {
            e.printStackTrace()
            return null // return null if failed
        }

        // 3. Open with external PDF viewer if available
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider", // must be configured in Manifest + provider_paths.xml
            destFile
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val packageManager = context.packageManager
        if (intent.resolveActivity(packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
        }

        return destFile
    }

}
