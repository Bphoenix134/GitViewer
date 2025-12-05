package data.repository

import data.remote.GitHubApi
import data.remote.mapper.toDomain
import domain.model.Repo
import domain.model.RepoContent
import domain.model.RepoDetailed
import domain.repository.RepoRepository

class RepoRepositoryImpl(
    private val api: GitHubApi
): RepoRepository {
    override suspend fun searchRepositories(query: String): List<Repo> {
        val items = api.getListOfRepo(query)
        return items.map { it.toDomain() }
    }

    override suspend fun getRepository(owner: String, repo: String): RepoDetailed {
        val dto = api.getFullInfoAboutRepo(owner, repo)
        return dto.toDomain()
    }

    override suspend fun getRepositoryContents(owner: String, repo: String): List<RepoContent> {
        val list = api.getContentOfRepo(owner, repo)
        return list.map { it.toDomain() }
    }
}