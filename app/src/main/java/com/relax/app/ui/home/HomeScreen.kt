package com.relax.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.relax.app.ui.components.FeaturedCard
import com.relax.app.ui.components.MeditationCard
import com.relax.app.ui.components.SectionHeader
import com.relax.app.ui.components.SleepStoryCard
import com.relax.app.ui.theme.DeepNavy
import com.relax.app.ui.theme.GradientEnd
import com.relax.app.ui.theme.GradientStart
import com.relax.app.ui.theme.TextMuted
import com.relax.app.ui.theme.TextSecondary
import java.time.LocalTime

@Composable
fun HomeScreen(
    onContentClick: (contentId: String, contentType: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                HomeHeader()
            }

            uiState.dailyCalm?.let { dailyCalm ->
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionLabel(text = "TODAY'S CALM")
                        Spacer(modifier = Modifier.height(12.dp))
                        FeaturedCard(
                            title = dailyCalm.title,
                            subtitle = dailyCalm.subtitle,
                            label = dailyCalm.theme,
                            durationMinutes = dailyCalm.durationMinutes,
                            gradientStart = dailyCalm.gradientStart,
                            gradientEnd = dailyCalm.gradientEnd,
                            onClick = { onContentClick(dailyCalm.id, "meditation") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }

            if (uiState.featuredMeditations.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Featured Meditations",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.featuredMeditations) { meditation ->
                            MeditationCard(
                                title = meditation.title,
                                subtitle = meditation.subtitle,
                                durationMinutes = meditation.durationMinutes,
                                gradientStart = meditation.gradientStart,
                                gradientEnd = meditation.gradientEnd,
                                isFree = meditation.isFree,
                                isNew = meditation.isNew,
                                onClick = { onContentClick(meditation.id, "meditation") },
                                modifier = Modifier.size(width = 160.dp, height = 200.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }

            if (uiState.recentSleepStories.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Sleep Stories",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.recentSleepStories) { story ->
                            SleepStoryCard(
                                title = story.title,
                                author = story.author,
                                durationMinutes = story.durationMinutes,
                                gradientStart = story.gradientStart,
                                gradientEnd = story.gradientEnd,
                                isFree = story.isFree,
                                isNew = story.isNew,
                                onClick = { onContentClick(story.id, "sleep") },
                                modifier = Modifier.size(width = 180.dp, height = 220.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }

            item {
                QuickActionsSection(onContentClick = onContentClick)
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = getGreeting(),
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = androidx.compose.ui.unit.TextUnit(1f, androidx.compose.ui.unit.TextUnitType.Sp)
    )
}

@Composable
private fun QuickActionsSection(
    onContentClick: (String, String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        SectionHeader(title = "Quick Start")
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeaturedCard(
                title = "Breathe",
                subtitle = "Box breathing to find calm",
                label = "2 min",
                durationMinutes = 2,
                gradientStart = Color(0xFF134E5E),
                gradientEnd = Color(0xFF71B280),
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
            FeaturedCard(
                title = "Sleep Aid",
                subtitle = "Wind down your mind",
                label = "10 min",
                durationMinutes = 10,
                gradientStart = Color(0xFF0F0C29),
                gradientEnd = Color(0xFF302B63),
                onClick = { onContentClick("m6", "meditation") },
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
        }
    }
}

private fun getGreeting(): String {
    return when (LocalTime.now().hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..20 -> "Good evening"
        else -> "Good night"
    }
}
