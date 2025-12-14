package di

import data.remote.GitHubApi
import data.repository.RepoRepositoryImpl
import domain.repository.RepoRepository
import domain.usecase.GetRepositoryContentsUseCase
import domain.usecase.GetRepositoryDetailsUseCase
import domain.usecase.SearchRepositoriesUseCase
import io.ktor.client.HttpClient
import org.koin.dsl.module
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import presentation.viewmodel.RepoViewModel
import utils.FileDownloader

val appModule = module {

    single {
        HttpClient(CIO) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = false
                        isLenient = true
                    }
                )
            }
        }
    }

    single { GitHubApi(get()) }

    single<RepoRepository> { RepoRepositoryImpl(get()) }

    single { SearchRepositoriesUseCase(get()) }
    single { GetRepositoryDetailsUseCase(get()) }
    single { GetRepositoryContentsUseCase(get()) }

    single { RepoViewModel(get(), get(), get(), get(), get()) }

}