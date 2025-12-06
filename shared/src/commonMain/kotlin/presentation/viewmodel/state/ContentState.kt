package presentation.viewmodel.state

import domain.model.RepoContent

sealed class ContentState {
    data object Idle : ContentState()
    data object Loading : ContentState()
    data class Success(val data: List<RepoContent>) : ContentState()
    data class Error(val message: String) : ContentState()
}