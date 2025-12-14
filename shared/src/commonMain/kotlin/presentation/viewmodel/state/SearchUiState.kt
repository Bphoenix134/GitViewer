package presentation.viewmodel.state

import domain.model.Repo

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val data: List<Repo>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
