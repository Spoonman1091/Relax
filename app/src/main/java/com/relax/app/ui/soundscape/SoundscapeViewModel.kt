package com.relax.app.ui.soundscape

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.relax.app.data.model.Soundscape
import com.relax.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SoundscapeUiState(
    val soundscapes: List<Soundscape> = emptyList(),
    val activeSoundscapeIds: Set<String> = emptySet()
) {
    val activeSoundscapes: List<Soundscape>
        get() = soundscapes.filter { it.id in activeSoundscapeIds }
}

@HiltViewModel
class SoundscapeViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoundscapeUiState())
    val uiState: StateFlow<SoundscapeUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = SoundscapeUiState(soundscapes = repository.getSoundscapes())
    }

    fun toggleSoundscape(soundscape: Soundscape) {
        val current = _uiState.value.activeSoundscapeIds
        val updated = if (soundscape.id in current) {
            current - soundscape.id
        } else {
            current + soundscape.id
        }
        _uiState.value = _uiState.value.copy(activeSoundscapeIds = updated)

        // If there are no active soundscapes, pause the player
        if (updated.isEmpty()) {
            exoPlayer.pause()
        }
    }

    fun stopAll() {
        exoPlayer.pause()
        _uiState.value = _uiState.value.copy(activeSoundscapeIds = emptySet())
    }

    override fun onCleared() {
        exoPlayer.pause()
        super.onCleared()
    }
}
