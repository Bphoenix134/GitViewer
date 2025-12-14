import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.appModule
import di.desktopModule
import di.initKoin
import presentation.viewmodel.RepoViewModel
import org.koin.java.KoinJavaComponent.get
import screen.FileViewerScreen
import screen.RepoInfoScreen
import screen.RepoSearchScreen

enum class Screen {
    SEARCH, INFO, VIEW_FILE
}

data class FileViewData(val name: String, val url: String)

fun main() = application {
    initKoin(
        appModule,
        desktopModule
    )
    val viewModel: RepoViewModel = get(RepoViewModel::class.java)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Git Viewer",
        icon = painterResource("images/icon.png")
    ) {
        MaterialTheme {
            App(
                desktopContent = {
                    var screen by remember { mutableStateOf(Screen.SEARCH) }

                    var selectedOwner by remember { mutableStateOf("") }
                    var selectedRepo by remember { mutableStateOf("") }
                    var selectedFile by remember { mutableStateOf<FileViewData?>(null) }

                    when (screen) {

                        Screen.SEARCH -> RepoSearchScreen(
                            viewModel,
                            onRepoClick = { owner, repo ->
                                selectedOwner = owner
                                selectedRepo = repo
                                screen = Screen.INFO
                            }
                        )

                        Screen.INFO -> RepoInfoScreen(
                            owner = selectedOwner,
                            repo = selectedRepo,
                            viewModel = viewModel,
                            onFileClick = { name, url ->
                                selectedFile = FileViewData(name, url)
                                screen = Screen.VIEW_FILE
                            },
                            onBack = {
                                screen = Screen.SEARCH
                            }
                        )

                        Screen.VIEW_FILE -> FileViewerScreen(
                            fileName = selectedFile!!.name,
                            url = selectedFile!!.url,
                            viewModel = viewModel,
                            onBack = {
                                screen = Screen.INFO
                            }
                        )
                    }
                }
            )
        }
    }
}