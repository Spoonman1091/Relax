package com.relax.app.data.remote.dto

data class YouTubeSearchResponse(val items: List<YouTubeSearchItem> = emptyList())
data class YouTubeSearchItem(val id: YouTubeItemId)
data class YouTubeItemId(val videoId: String = "")
