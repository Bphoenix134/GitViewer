package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDto(
    val id: Long,
    val name: String,

    @SerialName("full_name")
    val fullName: String,

    val owner: UserDto? = null,
    val description: String? = null,

    @SerialName("html_url")
    val htmlUrl: String,

    @SerialName("stargazers_count")
    val stars: Int,

    @SerialName("forks_count")
    val forks: Int,

    val language: String? = null
)