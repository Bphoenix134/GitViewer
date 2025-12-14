package screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.TreeNode
import kotlinx.coroutines.launch
import presentation.viewmodel.RepoViewModel
import presentation.viewmodel.state.RepoUiState

@Composable
fun RepoInfoScreen(
    repo: String,
    owner: String,
    viewModel: RepoViewModel,
    onFileClick: (name: String, url: String) -> Unit,
    onBack: () -> Unit
) {
    val repoState by viewModel.repoState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRepository(owner, repo)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier.height(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(Modifier.size(15.dp))

            Text(
                text = "Information about repository",
                style = MaterialTheme.typography.h5,
            )
        }

        Spacer(Modifier.size(30.dp))

                when (repoState) {

            is DetailsState.Error -> Text(
                text = (detailsState as DetailsState.Error).message,
                color = MaterialTheme.colors.error
            )

            is DetailsState.Success -> {
                val info = (detailsState as DetailsState.Success).details

                Text(info.fullName, style = MaterialTheme.typography.h6)
                if (info.description != null) {
                    Text(info.description!!, Modifier.padding(top = 8.dp))
                }

                Spacer(Modifier.height(10.dp))

                Text("Language: ${info.language ?: "-"}")
                Text("⭐ Stars: ${info.stars}")
                Text("\uD83D\uDCC4 Forks: ${info.forks}")
                Text("⭕ Issues: ${info.issues}")

                Spacer(Modifier.height(10.dp))

                Text("Created: ${info.createdAt}")
                Text("Updated: ${info.updatedAt}")
                Text("Pushed: ${info.pushedAt}")
            }

            else -> {}
        }

        Spacer(Modifier.height(25.dp))

        Divider()

        Spacer(Modifier.height(15.dp))

        Text("Structure of repository", style = MaterialTheme.typography.h6)

        when (contentState) {

            is ContentState.Error -> Text(
                text = (contentState as ContentState.Error).message,
                color = MaterialTheme.colors.error
            )

                        Spacer(Modifier.height(8.dp))

                        TreeView(
                            nodes = state.tree,
                            onFileClick = onFileClick,
                            onOpenDir = { node ->
                                viewModel.loadDirectory(
                                    owner = owner,
                                    repo = repo,
                                    path = node.path
                                )
                            }
                        )
                    }

                    RepoUiState.Idle -> Unit
                }
            }

            else -> {}
        }
    }
}


@Composable
fun TreeView(
    nodes: List<TreeNode>,
    onFileClick: (String, String) -> Unit,
    onOpenDir: suspend (TreeNode) -> List<TreeNode>
) {
    Column {
        nodes.forEach { node ->
            TreeNodeView(
                node = node,
                onFileClick = onFileClick,
                onOpenDir = onOpenDir
            )
        }
    }
}

@Composable
fun TreeNodeView(
    node: TreeNode,
    onFileClick: (String, String) -> Unit,
    onOpenDir: suspend (TreeNode) -> List<TreeNode>
) {
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(start = 16.dp)) {

        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    if (node.isDir) {
                        scope.launch {
                            if (node.children == null) {
                                node.children = onOpenDir(node)
                            }
                            node.isExpanded = !node.isExpanded
                        }
                    } else {
                        node.downloadUrl?.let {
                            onFileClick(node.name, it)
                        }
                    }
                }
                .padding(6.dp)
        ) {
            Text(
                text = when {
                    node.isDir && node.isExpanded -> "📂 ${node.name}"
                    node.isDir -> "📁 ${node.name}"
                    else -> "📄 ${node.name}"
                }
            )
        }

        if (node.isExpanded) {
            node.children?.forEach {
                TreeNodeView(
                    node = it,
                    onFileClick = onFileClick,
                    onOpenDir = onOpenDir
                )
            }
        }
    }
}