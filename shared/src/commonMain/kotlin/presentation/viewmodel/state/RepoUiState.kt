package presentation.viewmodel.state

import domain.model.RepoContent
import domain.model.RepoDetailed
import domain.model.TreeNode

sealed interface RepoUiState {
    object Idle : RepoUiState
    object Loading : RepoUiState
    data class Success(
        val details: RepoDetailed,
        val tree: List<TreeNode>
    ) : RepoUiState
    data class Error(val message: String) : RepoUiState
}
