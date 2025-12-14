package presentation.viewmodel.state

sealed interface FileUiState {
    object Loading : FileUiState
    data class Success(val content: String) : FileUiState
    data class Error(val message: String) : FileUiState
}