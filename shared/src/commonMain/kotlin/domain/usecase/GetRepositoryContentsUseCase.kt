package domain.usecase

import domain.model.RepoContent
import domain.repository.RepoRepository

class GetRepositoryContentsUseCase(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(owner: String, repo: String): List<RepoContent> {
        return repository.getRepositoryContents(owner, repo)
    }
}