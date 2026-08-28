package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = IslamicTealLight,
    onPrimary = DarkNavy,
    primaryContainer = IslamicTealDark,
    onPrimaryContainer = GoldenStarLight,
    secondary = GoldenStar,
    onSecondary = DarkNavy,
    secondaryContainer = GoldenAmberDark,
    onSecondaryContainer = GoldenStarLight,
    tertiary = SkyBlue,
    background = DarkNavy,
    surface = DarkNavySurface,
    onBackground = Color(0xFFECEFF1),
    onSurface = Color(0xFFECEFF1),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = IslamicTeal,
    onPrimary = Color.White,
    primaryContainer = IslamicTealContainer,
    onPrimaryContainer = IslamicTealDark,
    secondary = GoldenStar,
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = GoldenStarLight,
    onSecondaryContainer = GoldenAmberDark,
    tertiary = SkyBlue,
    background = SoftBgLight,
    surface = SurfaceCardLight,
    surfaceVariant = Color(0xFFEDF6F2),
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
  )

@Composable
fun JagoSholatTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color to maintain consistent vibrant theme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  JagoSholatTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}

