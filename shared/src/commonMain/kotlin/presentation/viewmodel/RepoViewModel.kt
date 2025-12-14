package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.TreeNode
import domain.usecase.GetRepositoryContentsUseCase
import domain.usecase.GetRepositoryDetailsUseCase
import domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.viewmodel.state.ContentState
import presentation.viewmodel.state.DetailsState
import presentation.viewmodel.state.SearchState

class RepoViewModel(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase,
    private val getRepositoryContentsUseCase: GetRepositoryContentsUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState

    private val _detailsState = MutableStateFlow<DetailsState>(DetailsState.Idle)
    val detailsState: StateFlow<DetailsState> = _detailsState

    private val _contentState = MutableStateFlow<ContentState>(ContentState.Idle)
    val contentState: StateFlow<ContentState> = _contentState

    fun searchRepositories(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                val list = searchRepositoriesUseCase(query)
                _searchState.value = SearchState.Success(list)
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadRepositoryDetails(owner: String, repo: String) {
        viewModelScope.launch {
            _detailsState.value = DetailsState.Loading
            try {
                val details = getRepositoryDetailsUseCase(owner, repo)
                _detailsState.value = DetailsState.Success(details)
            } catch (e: Exception) {
                _detailsState.value = DetailsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadRepositoryContents(owner: String, repo: String, path: String) {
        viewModelScope.launch {
            _contentState.value = ContentState.Loading
            try {
                val list = getRepositoryContentsUseCase(owner, repo, path)
                _contentState.value = ContentState.Success(list)
            } catch (e: Exception) {
                _contentState.value = ContentState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun loadDirectory(
        owner: String,
        repo: String,
        path: String
    ): List<TreeNode> {
        return getRepositoryContentsUseCase(owner, repo, path).map {
            TreeNode(
                name = it.name,
                path = it.path,
                isDir = it.type == "dir",
                downloadUrl = it.downloadUrl
            )
        }
    }
}