package domain.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TreeNode(
    val name: String,
    val path: String,
    val isDir: Boolean,
    val downloadUrl: String?
) {
    var children by mutableStateOf<List<TreeNode>?>(null)
    var isExpanded by mutableStateOf(false)
}