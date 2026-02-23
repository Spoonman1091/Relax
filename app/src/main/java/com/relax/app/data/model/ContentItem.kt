package com.relax.app.data.model

import androidx.compose.ui.graphics.Color

enum class ContentType { MEDITATION, SLEEP_STORY, SOUNDSCAPE }

enum class MeditationCategory(val displayName: String) {
    ALL("All"),
    STRESS("Stress"),
    ANXIETY("Anxiety"),
    SLEEP("Sleep"),
    FOCUS("Focus"),
    BEGINNERS("Beginners"),
    NATURE("Nature"),
    RELATIONSHIPS("Relationships")
}

data class Meditation(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val category: MeditationCategory,
    val gradientStart: Color,
    val gradientEnd: Color,
    val narrator: String,
    val audioUrl: String = "",
    val isFree: Boolean = false,
    val isNew: Boolean = false
)

data class SleepStory(
    val id: String,
    val title: String,
    val author: String,
    val durationMinutes: Int,
    val description: String,
    val gradientStart: Color,
    val gradientEnd: Color,
    val audioUrl: String = "",
    val isFree: Boolean = false,
    val isNew: Boolean = false
)

data class BreathingExercise(
    val id: String,
    val name: String,
    val description: String,
    val inhaleSeconds: Int,
    val holdSeconds: Int,
    val exhaleSeconds: Int,
    val holdAfterExhaleSeconds: Int = 0,
    val cycles: Int,
    val gradientStart: Color,
    val gradientEnd: Color,
    val benefit: String
)

data class Soundscape(
    val id: String,
    val name: String,
    val emoji: String,
    val audioUrl: String = "",
    val gradientStart: Color,
    val gradientEnd: Color,
    val isPlaying: Boolean = false,
    val volume: Float = 0.8f
)

data class DailyCalm(
    val id: String,
    val title: String,
    val subtitle: String,
    val theme: String,
    val durationMinutes: Int,
    val gradientStart: Color,
    val gradientEnd: Color,
    val audioUrl: String = ""
)
