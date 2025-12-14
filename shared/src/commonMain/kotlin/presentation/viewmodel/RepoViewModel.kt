package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecase.GetRepositoryContentsUseCase
import domain.usecase.GetRepositoryDetailsUseCase
import domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.viewmodel.state.RepoUiState
import presentation.viewmodel.state.SearchState

class RepoViewModel(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase,
    private val getRepositoryContentsUseCase: GetRepositoryContentsUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState

    fun searchRepositories(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                _searchState.value =
                    SearchState.Success(searchRepositoriesUseCase(query))
            } catch (e: Exception) {
                _searchState.value =
                    SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private val _repoState = MutableStateFlow<RepoUiState>(RepoUiState.Idle)
    val repoState: StateFlow<RepoUiState> = _repoState

    fun loadRepository(owner: String, repo: String) {
        viewModelScope.launch {
            _repoState.value = RepoUiState.Loading

            try {
                val detailsDeferred = async {
                    getRepositoryDetailsUseCase(owner, repo)
                }
                val contentsDeferred = async {
                    getRepositoryContentsUseCase(owner, repo)
                }

                _repoState.value = RepoUiState.Success(
                    details = detailsDeferred.await(),
                    contents = contentsDeferred.await()
                )

            } catch (e: Exception) {
                _repoState.value = RepoUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }
}