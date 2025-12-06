package presentation.viewmodel.state

import domain.model.RepoDetailed

sealed class DetailsState {
    data object Idle : DetailsState()
    data object Loading : DetailsState()
    data class Success(val details: RepoDetailed) : DetailsState()
    data class Error(val message: String) : DetailsState()
}