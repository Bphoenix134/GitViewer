package domain.repository

import domain.model.Repo
import domain.model.RepoContent
import domain.model.RepoDetailed

interface RepoRepository {
    suspend fun searchRepositories(query: String): List<Repo>
    suspend fun getRepository(owner: String, repo: String): RepoDetailed
    suspend fun getRepositoryContents(owner: String, repo: String): List<RepoContent>
}