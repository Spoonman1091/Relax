package com.relax.app.ui.meditate

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.relax.app.data.model.MeditationCategory
import com.relax.app.ui.components.MeditationCard
import com.relax.app.ui.components.SectionHeader
import com.relax.app.ui.theme.CalmPurple
import com.relax.app.ui.theme.GradientEnd
import com.relax.app.ui.theme.GradientStart
import com.relax.app.ui.theme.NavyBlue
import com.relax.app.ui.theme.SurfaceCard
import com.relax.app.ui.theme.TextMuted
import com.relax.app.ui.theme.TextSecondary

@Composable
fun MeditateScreen(
    onMeditationClick: (String) -> Unit,
    viewModel: MeditateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp).padding(top = 16.dp)
        ) {
            Text(
                text = "Meditate",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Find stillness in the present moment",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        CategoryFilter(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = viewModel::selectCategory
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            SectionHeader(
                title = "${uiState.filteredMeditations.size} Sessions",
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.filteredMeditations) { meditation ->
                MeditationCard(
                    title = meditation.title,
                    subtitle = meditation.subtitle,
                    durationMinutes = meditation.durationMinutes,
                    gradientStart = meditation.gradientStart,
                    gradientEnd = meditation.gradientEnd,
                    isFree = meditation.isFree,
                    isNew = meditation.isNew,
                    onClick = { onMeditationClick(meditation.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryFilter(
    selectedCategory: MeditationCategory,
    onCategorySelected: (MeditationCategory) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(MeditationCategory.values()) { category ->
            CategoryChip(
                label = category.displayName,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) CalmPurple else SurfaceCard,
        animationSpec = tween(200),
        label = "chipBackground"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else TextSecondary,
        animationSpec = tween(200),
        label = "chipText"
    )

    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
