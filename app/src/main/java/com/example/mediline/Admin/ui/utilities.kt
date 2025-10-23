package com.example.mediline.Admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BeautifulLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x88000000)) // semi-transparent dark overlay
            .clickable(enabled = false) {}, // block touches
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp) // bigger size
                    .padding(8.dp),
                strokeWidth = 6.dp, // thicker stroke
                color = MaterialTheme.colorScheme.primary // theme color
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}
