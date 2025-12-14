package com.myapplication.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import presentation.viewmodel.state.RepoUiState.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoInfoScreen(
    owner: String,
    repo: String,
    viewModel: RepoViewModel,
    onFileClick: (name: String, url: String) -> Unit,
    onBack: () -> Unit
) {
    val repoState by viewModel.repoState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRepository(owner, repo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(repo) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null
                        )
                    }
                }
            )
        }
    ) { padding ->

        when (repoState) {

            Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is Error -> Text(
                text = (repoState as Error).message,
                color = MaterialTheme.colorScheme.error
            )

            is Success -> {
                val state = repoState as Success
                val scroll = rememberScrollState()

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(scroll)
                ) {

                    Text(
                        state.details.fullName,
                        style = MaterialTheme.typography.titleLarge
                    )

                    state.details.description?.let {
                        Text(it, Modifier.padding(top = 8.dp))
                    }

                    Spacer(Modifier.height(12.dp))

                    Text("⭐ Stars: ${state.details.stars}")
                    Text("🍴 Forks: ${state.details.forks}")
                    Text("⭕ Issues: ${state.details.issues}")

                    Spacer(Modifier.height(16.dp))
                    Divider()
                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Repository structure",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    TreeView(
                        nodes = state.tree,
                        onFileClick = onFileClick,
                        onOpenDir = { node ->
                            viewModel.loadDirectory(owner, repo, node.path)
                        }
                    )
                }
            }

            else -> Unit
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
        nodes.forEach {
            TreeNodeView(it, onFileClick, onOpenDir)
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

    Column(Modifier.padding(start = 12.dp)) {

        Text(
            text = when {
                node.isDir && node.isExpanded -> "📂 ${node.name}"
                node.isDir -> "📁 ${node.name}"
                else -> "📄 ${node.name}"
            },
            modifier = Modifier
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
        )

        if (node.isExpanded) {
            node.children?.forEach {
                TreeNodeView(it, onFileClick, onOpenDir)
            }
        }
    }
}