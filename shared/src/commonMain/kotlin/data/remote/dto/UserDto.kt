package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val login: String,
    val id: Long,

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("html_url")
    val htmlUrl: String
)
