package com.example.mediline.User.ui.Queue

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QueueScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Current Number
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Now Serving", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Text("#23", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Text("Estimated wait: ~15 min", fontSize = 14.sp, color = Color.Gray)
                LinearProgressIndicator(progress = 0.6f, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total patients", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Text("50", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
//                Text("Estimated wait: ~20 min", fontSize = 14.sp, color = Color.Gray)
//                LinearProgressIndicator(progress = 0.6f, modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp))
            }
        }




        Spacer(Modifier.height(24.dp))

        // Quick Actions
        Button(
            onClick = { /* Navigate to booking */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Book Appointment")
        }

        OutlinedButton(
            onClick = { /* View Tickets */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("View My Tickets")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun QueueScreenPreview() {
    QueueScreen()
}