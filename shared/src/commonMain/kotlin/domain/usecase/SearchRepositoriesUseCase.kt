package domain.usecase

import domain.model.Repo
import domain.repository.RepoRepository

class SearchRepositoriesUseCase(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(query: String): List<Repo> {
        return repository.searchRepositories(query)
    }
}