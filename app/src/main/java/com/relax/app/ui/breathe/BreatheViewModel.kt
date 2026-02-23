package com.relax.app.ui.breathe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relax.app.data.model.BreathingExercise
import com.relax.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class BreathPhase(val label: String) {
    IDLE("Tap to Begin"),
    INHALE("Inhale"),
    HOLD("Hold"),
    EXHALE("Exhale"),
    HOLD_AFTER("Hold"),
    COMPLETE("Complete")
}

data class BreatheUiState(
    val exercises: List<BreathingExercise> = emptyList(),
    val selectedExercise: BreathingExercise? = null,
    val currentPhase: BreathPhase = BreathPhase.IDLE,
    val secondsRemaining: Int = 0,
    val cyclesCompleted: Int = 0,
    val isRunning: Boolean = false,
    val circleScale: Float = 1f
)

@HiltViewModel
class BreatheViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BreatheUiState())
    val uiState: StateFlow<BreatheUiState> = _uiState.asStateFlow()

    private var breathingJob: Job? = null

    init {
        val exercises = repository.getBreathingExercises()
        _uiState.value = _uiState.value.copy(
            exercises = exercises,
            selectedExercise = exercises.firstOrNull()
        )
    }

    fun selectExercise(exercise: BreathingExercise) {
        stopBreathing()
        _uiState.value = _uiState.value.copy(
            selectedExercise = exercise,
            currentPhase = BreathPhase.IDLE,
            cyclesCompleted = 0,
            isRunning = false
        )
    }

    fun toggleBreathing() {
        if (_uiState.value.isRunning) {
            stopBreathing()
        } else {
            startBreathing()
        }
    }

    private fun startBreathing() {
        val exercise = _uiState.value.selectedExercise ?: return
        breathingJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRunning = true, cyclesCompleted = 0)

            repeat(exercise.cycles) { cycle ->
                if (!_uiState.value.isRunning) return@repeat

                // Inhale
                runPhase(BreathPhase.INHALE, exercise.inhaleSeconds, 1f, 1.4f)

                // Hold after inhale
                if (exercise.holdSeconds > 0 && _uiState.value.isRunning) {
                    runPhase(BreathPhase.HOLD, exercise.holdSeconds, 1.4f, 1.4f)
                }

                // Exhale
                if (_uiState.value.isRunning) {
                    runPhase(BreathPhase.EXHALE, exercise.exhaleSeconds, 1.4f, 1f)
                }

                // Hold after exhale
                if (exercise.holdAfterExhaleSeconds > 0 && _uiState.value.isRunning) {
                    runPhase(BreathPhase.HOLD_AFTER, exercise.holdAfterExhaleSeconds, 1f, 1f)
                }

                if (_uiState.value.isRunning) {
                    _uiState.value = _uiState.value.copy(
                        cyclesCompleted = cycle + 1
                    )
                }
            }

            if (_uiState.value.isRunning) {
                _uiState.value = _uiState.value.copy(
                    currentPhase = BreathPhase.COMPLETE,
                    isRunning = false
                )
                delay(3000)
                _uiState.value = _uiState.value.copy(
                    currentPhase = BreathPhase.IDLE
                )
            }
        }
    }

    private suspend fun runPhase(
        phase: BreathPhase,
        seconds: Int,
        startScale: Float,
        endScale: Float
    ) {
        val steps = seconds * 10
        repeat(steps) { step ->
            if (!_uiState.value.isRunning) return
            val progress = step.toFloat() / steps
            val scale = startScale + (endScale - startScale) * progress
            val secondsLeft = seconds - (step / 10)
            _uiState.value = _uiState.value.copy(
                currentPhase = phase,
                secondsRemaining = secondsLeft,
                circleScale = scale
            )
            delay(100L)
        }
    }

    private fun stopBreathing() {
        breathingJob?.cancel()
        breathingJob = null
        _uiState.value = _uiState.value.copy(
            isRunning = false,
            currentPhase = BreathPhase.IDLE,
            circleScale = 1f
        )
    }

    override fun onCleared() {
        stopBreathing()
        super.onCleared()
    }
}
