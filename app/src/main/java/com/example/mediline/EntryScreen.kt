package com.example.mediline

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mediline.R
import com.example.mediline.User.ui.theme.PrimaryGreen

val PrimaryGreenGradient = Brush.verticalGradient(
    colors = listOf(
        PrimaryGreen.copy(alpha = 0.3f),
        PrimaryGreen.copy(alpha = 1f)
    )
)

@Composable
fun EntryScreen(
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreenGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo), // replace with your logo
                contentDescription = "MediLine Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Welcome to MediLine",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )


            RoleButton(
                text = "Continue as User",
                color =Color(0xFF2E8B57)
                //  color = Color(0xFF00BCD4)
            ) {
                navController.navigate("auth")
            }

            Spacer(modifier = Modifier.height(20.dp))

            RoleButton(
                text = "Continue as Admin",

               color = Color(0xFF4DD8A1)

            ) {
                navController.navigate("admin_graph")
            }

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Your health, our priority \uD83D\uDC9A",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RoleButton(text: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(color, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}
