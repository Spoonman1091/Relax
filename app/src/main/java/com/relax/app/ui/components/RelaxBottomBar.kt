package com.relax.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.relax.app.ui.navigation.Screen
import com.relax.app.ui.theme.CalmPurple
import com.relax.app.ui.theme.DeepNavy
import com.relax.app.ui.theme.DividerColor
import com.relax.app.ui.theme.TextMuted
import com.relax.app.ui.theme.TextPrimary

private data class NavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val navItems = listOf(
    NavItem(Screen.Home, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem(Screen.Meditate, "Meditate", Icons.Filled.SelfImprovement, Icons.Outlined.SelfImprovement),
    NavItem(Screen.Sleep, "Sleep", Icons.Filled.NightsStay, Icons.Outlined.NightsStay),
    NavItem(Screen.Breathe, "Breathe", Icons.Filled.Air, Icons.Outlined.Air),
    NavItem(Screen.Soundscape, "Sounds", Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
)

@Composable
fun RelaxBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = DeepNavy,
        tonalElevation = 0.dp
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            val iconColor by animateColorAsState(
                targetValue = if (isSelected) CalmPurple else TextMuted,
                animationSpec = tween(200),
                label = "iconColor"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) CalmPurple else TextMuted
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CalmPurple,
                    selectedTextColor = CalmPurple,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = DividerColor
                )
            )
        }
    }
}
