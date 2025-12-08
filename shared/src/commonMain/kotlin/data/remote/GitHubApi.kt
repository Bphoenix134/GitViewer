package data.remote

import data.remote.dto.RepoContentDto
import data.remote.dto.RepoDetailedDto
import data.remote.dto.RepoDto
import data.remote.dto.SearchResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json

class GitHubApi(private val client: HttpClient) {
    private val base = "https://api.github.com"

    suspend fun getListOfRepo(query: String): List<RepoDto> {
        val response: SearchResponseDto =
            client.get("$base/search/repositories") {
                parameter("q", query)
            }.body()

        return response.items
    }

    suspend fun getFullInfoAboutRepo(owner: String, repo: String): RepoDetailedDto {
        return client.get("$base/repos/$owner/$repo").body()
    }

    suspend fun getContentOfRepo(owner: String, repo: String, path: String): List<RepoContentDto> {
        return client.get("$base/repos/$owner/$repo/contents/$path").body()
    }
}

fun defaultJson() = Json { ignoreUnknownKeys = true }