package com.relax.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RelaxColorScheme = darkColorScheme(
    primary = CalmPurple,
    onPrimary = TextPrimary,
    primaryContainer = MidnightBlue,
    onPrimaryContainer = LavenderMist,
    secondary = CalmTeal,
    onSecondary = DeepNavy,
    secondaryContainer = SteelBlue,
    onSecondaryContainer = TextPrimary,
    tertiary = SoftGold,
    onTertiary = DeepNavy,
    background = DeepNavy,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
    outlineVariant = SurfaceCard,
    error = RoseBlush,
    onError = DeepNavy,
    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun RelaxTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepNavy.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = RelaxColorScheme,
        typography = RelaxTypography,
        content = content
    )
}
