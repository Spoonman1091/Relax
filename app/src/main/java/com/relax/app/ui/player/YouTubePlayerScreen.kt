package com.relax.app.ui.player

import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

@Composable
fun YouTubePlayerScreen(
    videoId: String,
    viewModel: PlayerViewModel
) {
    val context = LocalContext.current
    val state by viewModel.playerState.collectAsState()
    val commands = viewModel.playerCommand

    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
            }
            setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(this, true)

            webViewClient = WebViewClient()
        }
    }

    // Load HTML with video ID substituted
    LaunchedEffect(videoId) {
        val rawHtml = context.resources.openRawResource(
            context.resources.getIdentifier("youtube_player", "raw", context.packageName)
        ).bufferedReader().use { it.readText() }

        val html = rawHtml.replace("VIDEO_ID_PLACEHOLDER", videoId)

        webView.addJavascriptInterface(
            WebAppInterface(viewModel),
            "Android"
        )
        webView.loadDataWithBaseURL(
            "https://www.youtube.com",
            html,
            "text/html",
            "utf-8",
            null
        )
    }

    // Forward play/pause/seek commands to WebView
    LaunchedEffect(Unit) {
        commands.collect { command ->
            when (command) {
                is PlayerCommand.Play -> webView.evaluateJavascript("playVideo()", null)
                is PlayerCommand.Pause -> webView.evaluateJavascript("pauseVideo()", null)
                is PlayerCommand.Seek -> webView.evaluateJavascript("seekTo(${command.seconds})", null)
            }
        }
    }

    // Poll current time every second while playing
    LaunchedEffect(state.isPlaying) {
        while (state.isPlaying) {
            webView.evaluateJavascript("getCurrentTime()") { value ->
                val seconds = value?.toDoubleOrNull() ?: return@evaluateJavascript
                viewModel.onYouTubePositionUpdate(seconds)
            }
            delay(1000L)
        }
    }

    AndroidView(
        factory = { webView },
        modifier = Modifier.fillMaxSize()
    )
}

private class WebAppInterface(private val viewModel: PlayerViewModel) {

    @JavascriptInterface
    fun onPlayerReady(duration: Double) {
        viewModel.onYouTubePlayerReady(duration)
    }

    @JavascriptInterface
    fun onStateChange(state: Int) {
        viewModel.onYouTubeStateChange(state)
    }

    @JavascriptInterface
    fun onError(code: Int) {
        // Error from YouTube player — ViewModel will handle fallback via null videoId path
    }
}
