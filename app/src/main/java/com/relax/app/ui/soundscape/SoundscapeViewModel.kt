package com.relax.app.ui.soundscape

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
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

        if (updated.isEmpty()) {
            exoPlayer.pause()
        } else if (soundscape.id in updated) {
            // Soundscape was just activated — play it
            if (soundscape.audioUrl.isNotEmpty()) {
                exoPlayer.setMediaItem(MediaItem.fromUri(soundscape.audioUrl))
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                exoPlayer.prepare()
                exoPlayer.play()
            }
        } else {
            // Soundscape was deactivated — switch to another active one if available
            val next = _uiState.value.soundscapes.firstOrNull { it.id in updated }
            if (next != null && next.audioUrl.isNotEmpty()) {
                exoPlayer.setMediaItem(MediaItem.fromUri(next.audioUrl))
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                exoPlayer.prepare()
                exoPlayer.play()
            } else {
                exoPlayer.pause()
            }
        }
    }

    fun stopAll() {
        exoPlayer.pause()
        _uiState.value = _uiState.value.copy(activeSoundscapeIds = emptySet())
    }

    override fun onCleared() {
        if (_uiState.value.activeSoundscapeIds.isNotEmpty()) {
            exoPlayer.pause()
        }
        super.onCleared()
    }
}
