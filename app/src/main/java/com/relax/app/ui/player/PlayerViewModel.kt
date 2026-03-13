package com.relax.app.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.relax.app.data.model.ContentType
import com.relax.app.data.model.PlayerState
import com.relax.app.data.repository.ContentRepository
import com.relax.app.data.repository.YouTubeContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlayerCommand {
    object Play : PlayerCommand()
    object Pause : PlayerCommand()
    data class Seek(val seconds: Float) : PlayerCommand()
}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val youTubeContentRepository: YouTubeContentRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _playerCommand = MutableSharedFlow<PlayerCommand>(extraBufferCapacity = 1)
    val playerCommand: SharedFlow<PlayerCommand> = _playerCommand.asSharedFlow()

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

        val title: String
        val subtitle: String
        val audioUrl: String
        val durationMin: Int

        when (type) {
            ContentType.MEDITATION -> {
                val meditation = repository.getMeditations().find { it.id == contentId }
                if (meditation != null) {
                    title = meditation.title
                    subtitle = meditation.subtitle
                    audioUrl = meditation.audioUrl
                    durationMin = meditation.durationMinutes
                } else {
                    val dailyCalm = repository.getDailyCalm().takeIf { it.id == contentId }
                    title = dailyCalm?.title ?: "Meditation"
                    subtitle = dailyCalm?.subtitle ?: ""
                    audioUrl = dailyCalm?.audioUrl ?: ""
                    durationMin = dailyCalm?.durationMinutes ?: 10
                }
            }
            ContentType.SLEEP_STORY -> {
                val story = repository.getSleepStories().find { it.id == contentId }
                title = story?.title ?: "Sleep Story"
                subtitle = story?.author ?: ""
                audioUrl = story?.audioUrl ?: ""
                durationMin = story?.durationMinutes ?: 30
            }
            ContentType.SOUNDSCAPE -> {
                title = "Soundscape"
                subtitle = ""
                audioUrl = ""
                durationMin = 0
            }
        }

        val durationMs = durationMin.toLong() * 60_000L

        _playerState.value = PlayerState(
            title = title,
            subtitle = subtitle,
            contentType = type,
            audioUrl = audioUrl,
            durationMs = durationMs,
            isLoadingVideo = true
        )

        viewModelScope.launch {
            val videoId = youTubeContentRepository.findVideoId(title, contentType)
            _playerState.value = _playerState.value.copy(
                videoId = videoId,
                isLoadingVideo = false
            )
            if (videoId == null && audioUrl.isNotEmpty()) {
                exoPlayer.setMediaItem(MediaItem.fromUri(audioUrl))
                exoPlayer.prepare()
            }
        }
    }

    fun togglePlayPause() {
        val state = _playerState.value
        if (state.videoId != null) {
            if (state.isPlaying) {
                _playerCommand.tryEmit(PlayerCommand.Pause)
                _playerState.value = state.copy(isPlaying = false)
                stopProgressTracking()
            } else {
                _playerCommand.tryEmit(PlayerCommand.Play)
                _playerState.value = state.copy(isPlaying = true)
                startProgressTracking()
            }
        } else if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            if (state.audioUrl.isNotEmpty()) {
                exoPlayer.play()
            } else {
                // Demo mode: simulate playback without real audio
                _playerState.value = state.copy(isPlaying = !state.isPlaying)
                if (_playerState.value.isPlaying) {
                    startProgressTracking()
                } else {
                    stopProgressTracking()
                }
            }
        }
    }

    fun seekTo(fraction: Float) {
        val state = _playerState.value
        val position = (fraction * state.durationMs).toLong()
        if (state.videoId != null) {
            val seconds = position / 1000f
            _playerCommand.tryEmit(PlayerCommand.Seek(seconds))
        } else if (state.audioUrl.isNotEmpty()) {
            exoPlayer.seekTo(position)
        }
        _playerState.value = state.copy(currentPositionMs = position)
    }

    fun skipForward() {
        val duration = _playerState.value.durationMs
        if (duration == 0L) return
        val newPosition = (_playerState.value.currentPositionMs + 30_000L).coerceAtMost(duration)
        seekTo(newPosition.toFloat() / duration)
    }

    fun skipBackward() {
        val duration = _playerState.value.durationMs
        if (duration == 0L) return
        val newPosition = (_playerState.value.currentPositionMs - 30_000L).coerceAtLeast(0L)
        seekTo(newPosition.toFloat() / duration)
    }

    fun onYouTubePlayerReady(durationSeconds: Double) {
        _playerState.value = _playerState.value.copy(
            durationMs = (durationSeconds * 1000).toLong()
        )
    }

    fun onYouTubePositionUpdate(currentSeconds: Double) {
        _playerState.value = _playerState.value.copy(
            currentPositionMs = (currentSeconds * 1000).toLong()
        )
    }

    fun onYouTubeStateChange(ytState: Int) {
        // YT player states: -1=unstarted, 0=ended, 1=playing, 2=paused, 3=buffering, 5=cued
        when (ytState) {
            1 -> {
                _playerState.value = _playerState.value.copy(isPlaying = true)
                startProgressTracking()
            }
            0, 2 -> {
                _playerState.value = _playerState.value.copy(isPlaying = false)
                stopProgressTracking()
            }
        }
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (_playerState.value.isPlaying) {
                val state = _playerState.value
                if (state.videoId == null) {
                    val position = if (state.audioUrl.isNotEmpty()) {
                        exoPlayer.currentPosition
                    } else {
                        (state.currentPositionMs + 1000L).coerceAtMost(state.durationMs)
                    }
                    _playerState.value = state.copy(currentPositionMs = position)
                }
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
