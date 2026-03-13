package com.relax.app.data.remote

import com.relax.app.BuildConfig
import com.relax.app.data.remote.dto.YouTubeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("youtube/v3/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("part") part: String = "id",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 1,
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): YouTubeSearchResponse
}
