package com.relax.app.ui.meditate

import androidx.lifecycle.ViewModel
import com.relax.app.data.model.Meditation
import com.relax.app.data.model.MeditationCategory
import com.relax.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MeditateUiState(
    val meditations: List<Meditation> = emptyList(),
    val selectedCategory: MeditationCategory = MeditationCategory.ALL
) {
    val filteredMeditations: List<Meditation>
        get() = if (selectedCategory == MeditationCategory.ALL) meditations
        else meditations.filter { it.category == selectedCategory }
}

@HiltViewModel
class MeditateViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeditateUiState())
    val uiState: StateFlow<MeditateUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(meditations = repository.getMeditations())
    }

    fun selectCategory(category: MeditationCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }
}
