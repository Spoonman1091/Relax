package com.relax.app.ui.sleep

import androidx.lifecycle.ViewModel
import com.relax.app.data.model.SleepStory
import com.relax.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SleepUiState(
    val stories: List<SleepStory> = emptyList()
)

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = SleepUiState(stories = repository.getSleepStories())
    }
}
