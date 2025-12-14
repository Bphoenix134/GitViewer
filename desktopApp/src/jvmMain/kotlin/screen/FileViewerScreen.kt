package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import presentation.viewmodel.RepoViewModel
import presentation.viewmodel.state.FileUiState

@Composable
fun FileViewerScreen(
    fileName: String,
    url: String,
    viewModel: RepoViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.fileUiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(url) {
        viewModel.loadFile(url)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.height(36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    text = fileName,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )

                IconButton(
                    onClick = {
                        val content = (uiState as? FileUiState.Success)?.content
                        content?.let {
                            clipboardManager.setText(AnnotatedString(it))
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Copied to clipboard"
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Copy"
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.downloadFile(
                            url = url,
                            fileName = fileName
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Downloading…"
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Download"
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            when (uiState) {

                FileUiState.Loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FileUiState.Error -> {
                    Text(
                        text = (uiState as FileUiState.Error).message,
                        color = MaterialTheme.colors.error
                    )
                }

                is FileUiState.Success -> {
                    CodeBlock(
                        code = (uiState as FileUiState.Success).content
                    )
                }
            }
        }
    }
}


@Composable
fun CodeBlock(code: String) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
                shape = MaterialTheme.shapes.small
            )
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.body2.copy(
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp
            )
        )
    }
}