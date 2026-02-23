package com.relax.app.data.model

data class PlayerState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val title: String = "",
    val subtitle: String = "",
    val contentType: ContentType = ContentType.MEDITATION,
    val audioUrl: String = ""
) {
    val progressFraction: Float
        get() = if (durationMs > 0) currentPositionMs.toFloat() / durationMs else 0f

    val currentPositionFormatted: String
        get() = formatTime(currentPositionMs)

    val durationFormatted: String
        get() = formatTime(durationMs)

    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }
}
