package screen

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadRepository(owner, repo)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.height(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(Modifier.width(15.dp))

            Text(
                text = "Information about repository",
                style = MaterialTheme.typography.h5
            )
        }

        Spacer(Modifier.height(24.dp))
        Box(
            Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(end = 12.dp)
            ) {

                when (repoState) {

                    RepoUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is RepoUiState.Error -> {
                        Text(
                            text = (repoState as RepoUiState.Error).message,
                            color = MaterialTheme.colors.error
                        )
                    }

                    is RepoUiState.Success -> {
                        val state = repoState as RepoUiState.Success

                        val info = state.details

                        Text(info.fullName, style = MaterialTheme.typography.h6)

                        info.description?.let {
                            Text(it, Modifier.padding(top = 8.dp))
                        }

                        Spacer(Modifier.height(10.dp))

                        Text("Language: ${info.language ?: "-"}")
                        Text("⭐ Stars: ${info.stars}")
                        Text("📄 Forks: ${info.forks}")
                        Text("⭕ Issues: ${info.issues}")

                        Spacer(Modifier.height(10.dp))

                        Text("Created: ${info.createdAt}")
                        Text("Updated: ${info.updatedAt}")
                        Text("Pushed: ${info.pushedAt}")

                        Spacer(Modifier.height(24.dp))
                        Divider()
                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Structure of repository",
                            style = MaterialTheme.typography.h6
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

            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState)
            )
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