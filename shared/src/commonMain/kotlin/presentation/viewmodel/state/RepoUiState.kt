package presentation.viewmodel.state

import domain.model.RepoContent
import domain.model.RepoDetailed

sealed interface RepoUiState {
    object Idle : RepoUiState
    object Loading : RepoUiState
    data class Success(
        val details: RepoDetailed,
        val contents: List<RepoContent>
    ) : RepoUiState
    data class Error(val message: String) : RepoUiState
}

