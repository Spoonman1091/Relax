package com.relax.app.data.repository

import androidx.compose.ui.graphics.Color
import com.relax.app.data.model.BreathingExercise
import com.relax.app.data.model.DailyCalm
import com.relax.app.data.model.Meditation
import com.relax.app.data.model.MeditationCategory
import com.relax.app.data.model.SleepStory
import com.relax.app.data.model.Soundscape
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepository @Inject constructor() {

    fun getDailyCalm(): DailyCalm = DailyCalm(
        id = "daily_calm_today",
        title = "The Daily Calm",
        subtitle = "Find stillness in the present moment",
        theme = "Acceptance",
        durationMinutes = 10,
        gradientStart = Color(0xFF134E5E),
        gradientEnd = Color(0xFF71B280),
        audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Powerful%20mindfulness%20meditation.mp3"
    )

    fun getMeditations(): List<Meditation> = listOf(
        Meditation(
            id = "m1",
            title = "Morning Calm",
            subtitle = "Start your day with clarity",
            durationMinutes = 10,
            category = MeditationCategory.BEGINNERS,
            gradientStart = Color(0xFFFF9A9E),
            gradientEnd = Color(0xFFFECFEF),
            narrator = "Tamara Levitt",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Morning%20meditation.mp3",
            isFree = true
        ),
        Meditation(
            id = "m2",
            title = "Deep Focus",
            subtitle = "Sharpen your concentration",
            durationMinutes = 20,
            category = MeditationCategory.FOCUS,
            gradientStart = Color(0xFF4568DC),
            gradientEnd = Color(0xFFB06AB3),
            narrator = "Jeff Warren",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Mindfulness%20meditation%20guided.mp3",
            isNew = true
        ),
        Meditation(
            id = "m3",
            title = "Body Scan",
            subtitle = "Release tension head to toe",
            durationMinutes = 15,
            category = MeditationCategory.SLEEP,
            gradientStart = Color(0xFF0F3460),
            gradientEnd = Color(0xFF533483),
            narrator = "Tamara Levitt",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Deep%20Relaxation.mp3"
        ),
        Meditation(
            id = "m4",
            title = "Loving Kindness",
            subtitle = "Open your heart to compassion",
            durationMinutes = 12,
            category = MeditationCategory.ANXIETY,
            gradientStart = Color(0xFFFF8FA3),
            gradientEnd = Color(0xFFFF6B6B),
            narrator = "Jay Shetty",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Healing%20meditation.mp3"
        ),
        Meditation(
            id = "m5",
            title = "Mindful Walking",
            subtitle = "Presence in every step",
            durationMinutes = 10,
            category = MeditationCategory.NATURE,
            gradientStart = Color(0xFF6BCB77),
            gradientEnd = Color(0xFF4D96FF),
            narrator = "Jeff Warren",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Meditation%20everyone%20needs.mp3",
            isFree = true
        ),
        Meditation(
            id = "m6",
            title = "Evening Wind Down",
            subtitle = "Ease into restful sleep",
            durationMinutes = 8,
            category = MeditationCategory.SLEEP,
            gradientStart = Color(0xFF2D1B69),
            gradientEnd = Color(0xFF11998E),
            narrator = "Tamara Levitt",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Fall%20asleep.mp3"
        ),
        Meditation(
            id = "m7",
            title = "Anxiety Relief",
            subtitle = "Calm your nervous system",
            durationMinutes = 18,
            category = MeditationCategory.ANXIETY,
            gradientStart = Color(0xFF667EEA),
            gradientEnd = Color(0xFF764BA2),
            narrator = "Tamara Levitt",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Ease%20Anxiety.mp3",
            isNew = true
        ),
        Meditation(
            id = "m8",
            title = "Stress Release",
            subtitle = "Let go of daily pressures",
            durationMinutes = 14,
            category = MeditationCategory.STRESS,
            gradientStart = Color(0xFFFC5C7D),
            gradientEnd = Color(0xFF6A82FB),
            narrator = "Jeff Warren",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Ultimate%20Relaxation.mp3"
        ),
        Meditation(
            id = "m9",
            title = "Gratitude Practice",
            subtitle = "Cultivate appreciation",
            durationMinutes = 10,
            category = MeditationCategory.RELATIONSHIPS,
            gradientStart = Color(0xFFFFD166),
            gradientEnd = Color(0xFFFF8C42),
            narrator = "Jay Shetty",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/Positive%20energy.mp3"
        ),
        Meditation(
            id = "m10",
            title = "Breath Awareness",
            subtitle = "Return to the anchor of breath",
            durationMinutes = 7,
            category = MeditationCategory.BEGINNERS,
            gradientStart = Color(0xFF4ECDC4),
            gradientEnd = Color(0xFF44A08D),
            narrator = "Tamara Levitt",
            audioUrl = "https://archive.org/download/PowerfulMindfulnessMeditation/10%20minutes%20meditation.mp3",
            isFree = true
        )
    )

    fun getSleepStories(): List<SleepStory> = listOf(
        SleepStory(
            id = "ss1",
            title = "The Midnight Train",
            author = "by Sarah Johnson",
            durationMinutes = 40,
            description = "Journey through the peaceful countryside on a gentle overnight train as the world drifts by in moonlit silence.",
            gradientStart = Color(0xFF0F0C29),
            gradientEnd = Color(0xFF302B63),
            audioUrl = "https://archive.org/download/railway_children_librivox/railwaychildren_01_nesbit.mp3",
            isFree = true
        ),
        SleepStory(
            id = "ss2",
            title = "Forest Lullaby",
            author = "by Michael Chen",
            durationMinutes = 35,
            description = "Wander through an ancient forest where fireflies dance and the breeze carries the softest songs of nature.",
            gradientStart = Color(0xFF1D4350),
            gradientEnd = Color(0xFFA43931),
            audioUrl = "https://archive.org/download/wind_willows_ap_librivox/wind_willows_01_grahame_ap.mp3"
        ),
        SleepStory(
            id = "ss3",
            title = "Ocean Dreams",
            author = "by Emily Rose",
            durationMinutes = 45,
            description = "Float on warm, still waters beneath a canopy of stars as the ocean rocks you gently to sleep.",
            gradientStart = Color(0xFF005C97),
            gradientEnd = Color(0xFF363795),
            audioUrl = "https://archive.org/download/20000_leagues_under_the_seas_librivox/20000leaguesundertheseas_1-01_verne.mp3",
            isNew = true
        ),
        SleepStory(
            id = "ss4",
            title = "The Ancient Library",
            author = "by James Wilson",
            durationMinutes = 38,
            description = "Explore the endless halls of a magical library where every book holds a gateway to peaceful dreams.",
            gradientStart = Color(0xFF3A1C71),
            gradientEnd = Color(0xFFD76D77),
            audioUrl = "https://archive.org/download/arabian_nights_entertainments_02_2111_librivox/arabiannights02_01_anonymous_128kb.mp3"
        ),
        SleepStory(
            id = "ss5",
            title = "Lavender Fields",
            author = "by Sophie Martin",
            durationMinutes = 32,
            description = "Walk through endless lavender fields in Provence as the warm sun sets and the fragrant air calms your mind.",
            gradientStart = Color(0xFF654EA3),
            gradientEnd = Color(0xFFEAAFC8),
            audioUrl = "https://archive.org/download/cranford_sd_librivox/cranford_01_gaskell.mp3"
        ),
        SleepStory(
            id = "ss6",
            title = "Mountain Sanctuary",
            author = "by David Clark",
            durationMinutes = 42,
            description = "Find refuge in a cozy mountain cabin as snow falls softly outside and a warm fire crackles within.",
            gradientStart = Color(0xFF373B44),
            gradientEnd = Color(0xFF4286F4),
            audioUrl = "https://archive.org/download/heidi_solo_librivox/heidi_01_spyri.mp3",
            isNew = true
        )
    )

    fun getBreathingExercises(): List<BreathingExercise> = listOf(
        BreathingExercise(
            id = "b1",
            name = "4-7-8 Breathing",
            description = "Activate your parasympathetic nervous system for deep calm",
            inhaleSeconds = 4,
            holdSeconds = 7,
            exhaleSeconds = 8,
            cycles = 4,
            gradientStart = Color(0xFF134E5E),
            gradientEnd = Color(0xFF71B280),
            benefit = "Reduces anxiety & aids sleep"
        ),
        BreathingExercise(
            id = "b2",
            name = "Box Breathing",
            description = "Used by Navy SEALs to achieve peak calm under pressure",
            inhaleSeconds = 4,
            holdSeconds = 4,
            exhaleSeconds = 4,
            holdAfterExhaleSeconds = 4,
            cycles = 5,
            gradientStart = Color(0xFF4568DC),
            gradientEnd = Color(0xFFB06AB3),
            benefit = "Improves focus & concentration"
        ),
        BreathingExercise(
            id = "b3",
            name = "Deep Calm",
            description = "Slow, rhythmic breathing to quiet the mind",
            inhaleSeconds = 5,
            holdSeconds = 2,
            exhaleSeconds = 7,
            cycles = 6,
            gradientStart = Color(0xFF2D1B69),
            gradientEnd = Color(0xFF11998E),
            benefit = "Lowers heart rate & stress"
        ),
        BreathingExercise(
            id = "b4",
            name = "Energizing Breath",
            description = "Wake up your body and mind with invigorating breathing",
            inhaleSeconds = 2,
            holdSeconds = 0,
            exhaleSeconds = 2,
            cycles = 10,
            gradientStart = Color(0xFFFF9A9E),
            gradientEnd = Color(0xFFFFD486),
            benefit = "Boosts energy & alertness"
        )
    )

    fun getSoundscapes(): List<Soundscape> = listOf(
        Soundscape(
            id = "sc1",
            name = "Rain on Window",
            emoji = "🌧️",
            audioUrl = "https://archive.org/download/RainSound13/Gentle%20Rain%20and%20Thunder.mp3",
            gradientStart = Color(0xFF373B44),
            gradientEnd = Color(0xFF4286F4)
        ),
        Soundscape(
            id = "sc2",
            name = "Forest Morning",
            emoji = "🌲",
            audioUrl = "https://archive.org/download/NightSounds_201801/Soothing%20Night%20Time%20Forest%20Sounds.mp3",
            gradientStart = Color(0xFF134E5E),
            gradientEnd = Color(0xFF71B280)
        ),
        Soundscape(
            id = "sc3",
            name = "Ocean Waves",
            emoji = "🌊",
            audioUrl = "https://archive.org/download/OceanWaves_201602/OceanWaves.mp3",
            gradientStart = Color(0xFF005C97),
            gradientEnd = Color(0xFF363795)
        ),
        Soundscape(
            id = "sc4",
            name = "Crackling Fire",
            emoji = "🔥",
            audioUrl = "https://archive.org/download/Freesound-263994/Fireplace_3_hours-263994.mp3",
            gradientStart = Color(0xFF8B1A1A),
            gradientEnd = Color(0xFFFF8C42)
        ),
        Soundscape(
            id = "sc5",
            name = "White Noise",
            emoji = "〰️",
            audioUrl = "https://archive.org/download/whitenoise12hours/White%20Noise%2012%20Hours.mp3",
            gradientStart = Color(0xFF2C3E50),
            gradientEnd = Color(0xFF4CA1AF)
        ),
        Soundscape(
            id = "sc6",
            name = "Thunderstorm",
            emoji = "⛈️",
            audioUrl = "https://archive.org/download/1HourThunderstorm/1HrThunderstorm.mp3",
            gradientStart = Color(0xFF0F0C29),
            gradientEnd = Color(0xFF302B63)
        ),
        Soundscape(
            id = "sc7",
            name = "Mountain Stream",
            emoji = "💧",
            audioUrl = "https://archive.org/download/8-hours-of-waterfall-peaceful-sounds/8%20HOURS%20Calming%20Sounds%20of%20a%20Forest%20Stream%20and%20Bird%20Songs.mp3",
            gradientStart = Color(0xFF1D4350),
            gradientEnd = Color(0xFFA43931)
        ),
        Soundscape(
            id = "sc8",
            name = "Coffee Shop",
            emoji = "☕",
            audioUrl = "https://archive.org/download/coffee-shop-sounds-12/Coffee%20Shop%20Sounds%2011.mp3",
            gradientStart = Color(0xFF4A2C2A),
            gradientEnd = Color(0xFF8B5E52)
        ),
        Soundscape(
            id = "sc9",
            name = "Night Crickets",
            emoji = "🌙",
            audioUrl = "https://archive.org/download/relaxingcricketsoundsforsleep/Relaxing%20Cricket%20Sounds%20For%20Sleep.mp3",
            gradientStart = Color(0xFF0B1426),
            gradientEnd = Color(0xFF1A2E4A)
        )
    )
}
