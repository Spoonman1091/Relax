package com.relax.app.data.repository

import com.relax.app.data.remote.YouTubeApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YouTubeContentRepository @Inject constructor(
    private val api: YouTubeApiService
) {
    private val cache = mutableMapOf<String, String>()

    suspend fun findVideoId(title: String, contentType: String): String? {
        val key = "$contentType:$title"
        cache[key]?.let { return it }
        return try {
            val query = buildQuery(title, contentType)
            val response = api.search(query = query)
            val videoId = response.items.firstOrNull()?.id?.videoId
            if (videoId != null) cache[key] = videoId
            videoId
        } catch (e: Exception) {
            null
        }
    }

    private fun buildQuery(title: String, contentType: String) = when (contentType) {
        "sleep" -> "$title sleep story audio"
        "soundscape" -> "$title ambient sounds relaxation"
        else -> "$title guided meditation"
    }
}
