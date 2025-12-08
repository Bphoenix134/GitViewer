package domain.model

data class TreeNode(
    val name: String,
    val path: String,
    val isDir: Boolean,
    val downloadUrl: String?,
    var children: List<TreeNode>? = null,
    var isExpanded: Boolean = false
)
