package domain.usecase

import domain.model.RepoDetailed
import domain.repository.RepoRepository

class GetRepositoryDetailsUseCase(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(owner: String, repo: String): RepoDetailed {
        return repository.getRepository(owner, repo)
    }
}