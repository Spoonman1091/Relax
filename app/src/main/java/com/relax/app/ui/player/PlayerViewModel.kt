package com.relax.app.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.relax.app.data.model.ContentType
import com.relax.app.data.model.PlayerState
import com.relax.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var progressJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                Player.STATE_READY -> {
                    _playerState.value = _playerState.value.copy(
                        durationMs = exoPlayer.duration.coerceAtLeast(0L)
                    )
                    startProgressTracking()
                }
                Player.STATE_ENDED -> {
                    _playerState.value = _playerState.value.copy(isPlaying = false)
                    stopProgressTracking()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playerState.value = _playerState.value.copy(isPlaying = isPlaying)
            if (isPlaying) startProgressTracking() else stopProgressTracking()
        }
    }

    init {
        exoPlayer.addListener(playerListener)
    }

    fun loadContent(contentId: String, contentType: String) {
        val type = when (contentType) {
            "sleep" -> ContentType.SLEEP_STORY
            "soundscape" -> ContentType.SOUNDSCAPE
            else -> ContentType.MEDITATION
        }

        val (title, subtitle, audioUrl, durationMin) = when (type) {
            ContentType.MEDITATION -> {
                val meditation = repository.getMeditations().find { it.id == contentId }
                listOf(
                    meditation?.title ?: "Meditation",
                    meditation?.subtitle ?: "",
                    meditation?.audioUrl ?: "",
                    meditation?.durationMinutes ?: 10
                )
            }
            ContentType.SLEEP_STORY -> {
                val story = repository.getSleepStories().find { it.id == contentId }
                listOf(
                    story?.title ?: "Sleep Story",
                    story?.author ?: "",
                    story?.audioUrl ?: "",
                    story?.durationMinutes ?: 30
                )
            }
            ContentType.SOUNDSCAPE -> listOf("Soundscape", "", "", 0)
        }

        val durationMs = (durationMin as Int).toLong() * 60_000L

        _playerState.value = PlayerState(
            title = title as String,
            subtitle = subtitle as String,
            contentType = type,
            audioUrl = audioUrl as String,
            durationMs = durationMs
        )

        if (audioUrl.isNotEmpty()) {
            exoPlayer.setMediaItem(MediaItem.fromUri(audioUrl))
            exoPlayer.prepare()
        }
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            if (_playerState.value.audioUrl.isNotEmpty()) {
                exoPlayer.play()
            } else {
                // Demo mode: simulate playback without real audio
                _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
                if (_playerState.value.isPlaying) {
                    startProgressTracking()
                } else {
                    stopProgressTracking()
                }
            }
        }
    }

    fun seekTo(fraction: Float) {
        val position = (fraction * _playerState.value.durationMs).toLong()
        if (_playerState.value.audioUrl.isNotEmpty()) {
            exoPlayer.seekTo(position)
        }
        _playerState.value = _playerState.value.copy(currentPositionMs = position)
    }

    fun skipForward() {
        val newPosition = (_playerState.value.currentPositionMs + 30_000L)
            .coerceAtMost(_playerState.value.durationMs)
        seekTo(newPosition.toFloat() / _playerState.value.durationMs)
    }

    fun skipBackward() {
        val newPosition = (_playerState.value.currentPositionMs - 30_000L)
            .coerceAtLeast(0L)
        seekTo(newPosition.toFloat() / _playerState.value.durationMs)
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (_playerState.value.isPlaying) {
                val position = if (_playerState.value.audioUrl.isNotEmpty()) {
                    exoPlayer.currentPosition
                } else {
                    (_playerState.value.currentPositionMs + 1000L)
                        .coerceAtMost(_playerState.value.durationMs)
                }
                _playerState.value = _playerState.value.copy(currentPositionMs = position)
                delay(1000L)
            }
        }
    }

    private fun stopProgressTracking() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        exoPlayer.removeListener(playerListener)
        stopProgressTracking()
        super.onCleared()
    }
}
