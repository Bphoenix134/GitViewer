package di

import DesktopFileDownloader
import org.koin.dsl.module
import utils.FileDownloader

val desktopModule = module {

    single<FileDownloader> {
        DesktopFileDownloader(
            client = get()
        )
    }
}