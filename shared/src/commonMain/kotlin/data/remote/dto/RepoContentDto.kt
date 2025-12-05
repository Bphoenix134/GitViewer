package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoContentDto(
    val name: String,
    val path: String,
    val sha: String,

    val type: String,

    val size: Long,

    @SerialName("download_url")
    val downloadUrl: String? = null,

    @SerialName("html_url")
    val htmlUrl: String? = null,

    @SerialName("git_url")
    val gitUrl: String? = null,

    @SerialName("url")
    val apiUrl: String
)