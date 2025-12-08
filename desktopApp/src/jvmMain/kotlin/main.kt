import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.appModule
import di.initKoin
import presentation.viewmodel.RepoViewModel
import org.koin.java.KoinJavaComponent.get
import screen.RepoSearchScreen

fun main() = application {
    initKoin(appModule)
    val viewModel: RepoViewModel = get(RepoViewModel::class.java)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Git Viewer",
        icon = painterResource("images/icon.png")
    ) {
        MaterialTheme {
            App(desktopContent = {
                RepoSearchScreen(viewModel)
            })
        }
    }
}