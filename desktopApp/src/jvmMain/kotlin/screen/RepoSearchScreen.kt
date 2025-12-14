package screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import component.RepoList
import presentation.viewmodel.RepoViewModel
import presentation.viewmodel.state.SearchUiState
import androidx.compose.ui.text.input.ImeAction

@Composable
fun RepoSearchScreen(
    viewModel: RepoViewModel,
    onRepoClick: (owner: String, repo: String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val state by viewModel.searchUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(60.dp)
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                placeholder = { Text("Enter your query") },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.Black
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (query.isNotBlank()) {
                            viewModel.searchRepositories(query)
                        }
                    }
                ),
            )

            Spacer(Modifier.size(15.dp))

            Button(
                onClick = {
                    viewModel.searchRepositories(query)
                },
                enabled = query.isNotBlank(),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (query.isNotBlank()) Color.Black else Color.Gray
                )
            ) {
                Text(
                    text = "Search",
                    color = Color.White
                )
            }
        }

        when (state) {
            is SearchUiState.Success -> RepoList(
                repos = (state as SearchUiState.Success).data,
                onClick = { repo ->
                    if (repo.owner.isNotBlank()) {
                        onRepoClick(repo.owner, repo.name)
                    }
                }
            )

            is SearchUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }

            is SearchUiState.Error -> Text(
                text = "(state as SearchState.Error).message",
                color = MaterialTheme.colors.error
            )

            else -> {}
        }
    }
}