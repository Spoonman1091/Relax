package com.relax.app.ui.breathe

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.relax.app.data.model.BreathingExercise
import com.relax.app.ui.theme.BreatheGradientEnd
import com.relax.app.ui.theme.BreatheGradientStart
import com.relax.app.ui.theme.CalmPurple
import com.relax.app.ui.theme.SurfaceCard
import com.relax.app.ui.theme.TextMuted
import com.relax.app.ui.theme.TextSecondary

@Composable
fun BreatheScreen(
    viewModel: BreatheViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val exercise = uiState.selectedExercise

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        exercise?.gradientStart ?: BreatheGradientStart,
                        exercise?.gradientEnd ?: BreatheGradientEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp).padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Breathe",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = exercise?.name ?: "Select an exercise",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            ExerciseSelector(
                exercises = uiState.exercises,
                selectedExercise = exercise,
                onSelect = viewModel::selectExercise
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Breathing circle
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BreathingCircle(
                    phase = uiState.currentPhase,
                    scale = uiState.circleScale,
                    secondsRemaining = uiState.secondsRemaining,
                    cyclesCompleted = uiState.cyclesCompleted,
                    totalCycles = exercise?.cycles ?: 0,
                    isRunning = uiState.isRunning,
                    onTap = viewModel::toggleBreathing
                )
            }

            exercise?.let {
                ExerciseInfo(exercise = it, cyclesCompleted = uiState.cyclesCompleted)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ExerciseSelector(
    exercises: List<BreathingExercise>,
    selectedExercise: BreathingExercise?,
    onSelect: (BreathingExercise) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(exercises) { exercise ->
            val isSelected = exercise.id == selectedExercise?.id
            Surface(
                shape = RoundedCornerShape(50),
                color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                modifier = Modifier.clickable { onSelect(exercise) }
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun BreathingCircle(
    phase: BreathPhase,
    scale: Float,
    secondsRemaining: Int,
    cyclesCompleted: Int,
    totalCycles: Int,
    isRunning: Boolean,
    onTap: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 100),
        label = "breatheScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.clickable(onClick = onTap)
    ) {
        // Outer glow ring
        Box(
            modifier = Modifier
                .size(260.dp)
                .scale(animatedScale * 1.15f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Middle ring
        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(animatedScale * 1.05f)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
        )

        // Main circle
        Box(
            modifier = Modifier
                .size(180.dp)
                .scale(animatedScale)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.15f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedContent(
                    targetState = phase.label,
                    transitionSpec = {
                        fadeIn(tween(400)) togetherWith fadeOut(tween(400))
                    },
                    label = "phaseLabel"
                ) { label ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }

                if (isRunning && secondsRemaining > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = secondsRemaining.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (!isRunning && phase == BreathPhase.IDLE) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "tap",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseInfo(
    exercise: BreathingExercise,
    cyclesCompleted: Int
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Black.copy(alpha = 0.2f),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PhaseInfo(label = "Inhale", seconds = exercise.inhaleSeconds)
                if (exercise.holdSeconds > 0) {
                    PhaseInfo(label = "Hold", seconds = exercise.holdSeconds)
                }
                PhaseInfo(label = "Exhale", seconds = exercise.exhaleSeconds)
                if (exercise.holdAfterExhaleSeconds > 0) {
                    PhaseInfo(label = "Hold", seconds = exercise.holdAfterExhaleSeconds)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = exercise.benefit,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$cyclesCompleted / ${exercise.cycles} cycles",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
private fun PhaseInfo(label: String, seconds: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${seconds}s",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.65f)
        )
    }
}
