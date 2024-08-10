package com.example.workoutapp.screens.helpers

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

const val gradientAlpha = 0.2f
val GeminiGradient = Brush.horizontalGradient(
    listOf(
        Color(0xFF1F7DFE).copy(alpha = gradientAlpha), // Blue
        Color(0xFFB695F9).copy(alpha = gradientAlpha)  // Purple
    )
)
const val gradientStrongAlpha = 0.8f
val GeminiGradientStrong = Brush.horizontalGradient(
    listOf(
        Color(0xFF1F7DFE).copy(alpha = gradientStrongAlpha), // Blue
        Color(0xFFB695F9).copy(alpha = gradientStrongAlpha)  // Purple
    )
)