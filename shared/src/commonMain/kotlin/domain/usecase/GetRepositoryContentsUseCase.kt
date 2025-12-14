package domain.usecase

import domain.model.RepoContent
import domain.model.TreeNode
import domain.repository.RepoRepository
import presentation.viewmodel.RepoViewModel

class GetRepositoryContentsUseCase(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(owner: String, repo: String, path: String): List<RepoContent> {
        return repository.getRepositoryContents(owner, repo, path)
    }
}