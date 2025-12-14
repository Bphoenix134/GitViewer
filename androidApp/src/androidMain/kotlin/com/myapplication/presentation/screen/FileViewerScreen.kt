package com.myapplication.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import presentation.viewmodel.RepoViewModel
import presentation.viewmodel.state.FileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileViewerScreen(
    fileName: String,
    url: String,
    viewModel: RepoViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.fileUiState.collectAsState()

    LaunchedEffect(url) {
        viewModel.loadFile(url)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fileName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.downloadFile(url, fileName)
                    }) {
                        Icon(Icons.Default.KeyboardArrowDown, null)
                    }
                }
            )
        }
    ) { padding ->

        when (uiState) {

            FileUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is FileUiState.Error -> Text(
                text = (uiState as FileUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )

            is FileUiState.Success -> {
                Text(
                    text = (uiState as FileUiState.Success).content,
                    modifier = Modifier
                        .padding(padding)
                        .padding(12.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.shapes.small
                        )
                        .padding(12.dp),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
