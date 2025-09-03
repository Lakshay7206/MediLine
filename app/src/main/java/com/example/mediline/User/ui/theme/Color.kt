package com.example.mediline.User.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White


val BackgroundDark = Color(0xFF010F1C)
val PrimaryGreen = Color(0xFF3BB77E)
val TextGrayLight = Color(0xFF939393)
val TextGray = Color(0xFF646464)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
// val LightColors = lightColorScheme(
//    primary = Color(0xFF3BB77E),        // Main brand green
//    onPrimary = White,            // Text/icons on primary
//
//    secondary = Color(0xFF010F1C),      // Deep navy for accents
//    onSecondary = Color.White,          // Text/icons on secondary
//
//    background = Color(0xFFFDFDFD),     // App background (almost white)
//    onBackground = Color(0xFF010F1C),   // Text/icons on background
//
//    surface = Color.White,              // Cards, sheets, input fields
//    onSurface = Color(0xFF010F1C),      // Text/icons on cards
//
//    outline = Color(0xFF939393),        // Borders, strokes, hint text
//)
//
// val DarkColors = darkColorScheme(
//    primary = Color(0xFF3BB77E),
//    onPrimary = Color.Black,
//
//    secondary = Color(0xFF646464),
//    onSecondary = Color.White,
//
//    background = Color(0xFF010F1C),
//    onBackground = Color(0xFFFDFDFD),
//
//    surface = Color(0xFF1A1A1A),
//    onSurface = Color(0xFFFDFDFD),
//
//    outline = Color(0xFF646464),
//)


 //Define the app's light color scheme
val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = White,
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = White,
    secondary = PrimaryGreen,
    onSecondary = White,
    background =White,
    onBackground = Black,
    surface = White,
    onSurface = Black
)

// Define the app's dark color scheme (if you plan to support a dark theme)
private val DarkColors = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = White,
    secondary = PrimaryGreen,
    onSecondary = White,
    background = BackgroundDark,
    onBackground = White,
    surface = BackgroundDark,
    onSurface = White
)


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)