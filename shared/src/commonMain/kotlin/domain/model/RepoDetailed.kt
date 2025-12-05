package domain.model

data class RepoDetailed(
    val id: Long,
    val name: String,
    val fullName: String,
    val owner: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val issues: Int,
    val language: String?,
    val createdAt: String,
    val updatedAt: String,
    val pushedAt: String
)
