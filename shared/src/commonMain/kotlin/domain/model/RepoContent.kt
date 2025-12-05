package domain.model

data class RepoContent(
    val name: String,
    val path: String,
    val type: String,
    val downloadUrl: String?
)
