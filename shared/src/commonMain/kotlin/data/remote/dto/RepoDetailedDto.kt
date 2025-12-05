package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailedDto(
    val id: Long,
    val name: String,

    @SerialName("full_name")
    val fullName: String,

    val private: Boolean,

    val owner: UserDto,

    val description: String? = null,

    @SerialName("html_url")
    val htmlUrl: String,

    @SerialName("stargazers_count")
    val stars: Int,

    @SerialName("forks_count")
    val forks: Int,

    @SerialName("open_issues_count")
    val issues: Int,

    val language: String? = null,
    val topics: List<String> = emptyList(),

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("pushed_at")
    val pushedAt: String,

    @SerialName("default_branch")
    val defaultBranch: String
)