import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun App(
    desktopContent: @Composable (() -> Unit)? = null
) {
    MaterialTheme {
        desktopContent?.invoke()
    }
}

expect fun getPlatformName(): String