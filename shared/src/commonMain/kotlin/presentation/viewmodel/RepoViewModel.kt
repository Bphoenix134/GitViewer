package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.TreeNode
import domain.usecase.GetRepositoryContentsUseCase
import domain.usecase.GetRepositoryDetailsUseCase
import domain.usecase.SearchRepositoriesUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.viewmodel.state.FileUiState
import presentation.viewmodel.state.RepoUiState
import presentation.viewmodel.state.SearchUiState
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import utils.FileDownloader

class RepoViewModel(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase,
    private val getRepositoryContentsUseCase: GetRepositoryContentsUseCase,
    private val httpClient: HttpClient,
    private val downloader: FileDownloader
) : ViewModel() {

    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    private val _fileUiState = MutableStateFlow<FileUiState>(FileUiState.Loading)
    val fileUiState: StateFlow<FileUiState> = _fileUiState

    fun searchRepositories(query: String) {
        viewModelScope.launch {
            _searchUiState.value = SearchUiState.Loading
            try {
                _searchUiState.value =
                    SearchUiState.Success(searchRepositoriesUseCase(query))
            } catch (e: Exception) {
                _searchUiState.value =
                    SearchUiState.Error(e.message ?: "Unknown error")
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

                val treeDeferred = async {
                    loadDirectory(owner, repo, "")
                }

                _repoState.value = RepoUiState.Success(
                    details = detailsDeferred.await(),
                    tree = treeDeferred.await()
                )

            } catch (e: Exception) {
                _repoState.value = RepoUiState.Error(
                    e.message ?: "Unknown error"
                )
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

    fun loadFile(url: String) {
        viewModelScope.launch {
            _fileUiState.value = FileUiState.Loading
            try {
                val response = httpClient.get(url)
                _fileUiState.value = FileUiState.Success(
                    response.bodyAsText()
                )
            } catch (e: Exception) {
                _fileUiState.value = FileUiState.Error(
                    e.message ?: "Failed to load file"
                )
            }
        }
    }

    fun downloadFile(url: String, fileName: String) {
        viewModelScope.launch {
            try {
                downloader.download(url, fileName)
            } catch (e: Exception) {
            }
        }
    }
}