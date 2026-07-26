package com.aiher.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    onPrimary = Color.White,
    primaryContainer = Purple200,
    secondary = Teal500,
    onSecondary = Color.White,
    secondaryContainer = Teal500.copy(alpha = 0.2f),
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF0F0F5),
    error = ErrorRed,
    onError = Color.White,
    outline = Divider
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple200,
    onPrimary = Color.Black,
    primaryContainer = Purple700,
    secondary = Teal500,
    onSecondary = Color.Black,
    secondaryContainer = Teal700,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2D2D44),
    error = ErrorRed,
    onError = Color.White,
    outline = Color(0xFF3D3D5C)
)

@Composable
fun AIHerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}