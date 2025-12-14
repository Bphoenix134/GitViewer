package com.myapplication.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.myapplication.presentation.component.RepoList
import presentation.viewmodel.RepoViewModel
import presentation.viewmodel.state.SearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoSearchScreen(
    viewModel: RepoViewModel,
    onRepoClick: (owner: String, repo: String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val state by viewModel.searchUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Git Viewer") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search repositories") },
                leadingIcon = {
                    Icon(Icons.Default.Search, null)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (query.isNotBlank()) {
                            viewModel.searchRepositories(query)
                        }
                    }
                )
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { viewModel.searchRepositories(query) },
                enabled = query.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            Spacer(Modifier.height(16.dp))

            when (state) {

                is SearchUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SearchUiState.Success -> {
                    RepoList(
                        repos = (state as SearchUiState.Success).data,
                        onClick = {
                            onRepoClick(it.owner, it.name)
                        }
                    )
                }

                is SearchUiState.Error -> {
                    Text(
                        text = (state as SearchUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> { }
            }
        }
    }
}