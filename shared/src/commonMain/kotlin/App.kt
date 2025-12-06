import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import di.appModule
import di.initKoin

@Composable
fun App() {
    remember {
        initKoin(appModule)
    }

    MaterialTheme {
    }
}

expect fun getPlatformName(): String