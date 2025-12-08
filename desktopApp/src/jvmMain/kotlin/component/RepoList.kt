package component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.Repo

@Composable
fun RepoList(
    repos: List<Repo>,
    onClick: (Repo) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(repos) { repo ->
            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onClick(repo) }
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(repo.fullName, color = Color.Blue)
                    repo.description?.let {
                        Text(it, Modifier.padding(top = 4.dp))
                    }
                    Text(
                        "${repo.language} · ⭐${repo.stars}",
                        Modifier.padding(top = 4.dp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}