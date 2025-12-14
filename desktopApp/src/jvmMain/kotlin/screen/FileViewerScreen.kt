package screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*

@Composable
fun FileViewerScreen(
    fileName: String,
    url: String,
    httpClient: HttpClient,
    onBack: () -> Unit
) {
    var text by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = httpClient.get(url)
            text = response.bodyAsText()
        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.height(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(Modifier.size(15.dp))

            Text(fileName, style = MaterialTheme.typography.h5)
        }

        Spacer(Modifier.height(10.dp))

        when {
            text == null && error == null -> CircularProgressIndicator()
            error != null -> Text("Error: $error", color = MaterialTheme.colors.error)
            else -> Text(
                text!!,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}