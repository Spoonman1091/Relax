package com.relax.app.ui.home

import androidx.lifecycle.ViewModel
import com.relax.app.data.model.DailyCalm
import com.relax.app.data.model.Meditation
import com.relax.app.data.model.SleepStory
import com.relax.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HomeUiState(
    val dailyCalm: DailyCalm? = null,
    val featuredMeditations: List<Meditation> = emptyList(),
    val recentSleepStories: List<SleepStory> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        _uiState.value = HomeUiState(
            dailyCalm = repository.getDailyCalm(),
            featuredMeditations = repository.getMeditations().take(4),
            recentSleepStories = repository.getSleepStories().take(3)
        )
    }
}
