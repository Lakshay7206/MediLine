package com.example.mediline.User.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import com.example.mediline.R


val PTSansFamily = FontFamily(
    Font(R.font.pt_sans_regular, FontWeight.Normal),
    Font(R.font.pt_sans_bold, FontWeight.Bold)
)

// Define the typography for the app
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PTSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
        color = White
    ),
    headlineLarge = TextStyle(
        fontFamily = PTSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        color = White
    ),
    titleLarge = TextStyle(
        fontFamily = PTSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = White
    ),
    bodyLarge = TextStyle(
        fontFamily = PTSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
      //  lineHeight = 24.sp,
        letterSpacing = 0.5.sp,

    ),
    labelSmall = TextStyle(
        fontFamily = PTSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
       // lineHeight = 16.sp,
        letterSpacing = 0.5.sp,

    )
)
// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
//)