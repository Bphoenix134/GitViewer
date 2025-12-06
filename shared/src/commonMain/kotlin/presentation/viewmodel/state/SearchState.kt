package presentation.viewmodel.state

import domain.model.Repo

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data class Success(val data: List<Repo>) : SearchState()
    data class Error(val message: String) : SearchState()
}
