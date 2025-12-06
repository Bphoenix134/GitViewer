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

val appModule = module {

    single {
        HttpClient(CIO) {
            expectSuccess = true
        }
    }

    single { GitHubApi(get()) }

    single<RepoRepository> { RepoRepositoryImpl(get()) }

    single { SearchRepositoriesUseCase(get()) }
    single { GetRepositoryDetailsUseCase(get()) }
    single { GetRepositoryContentsUseCase(get()) }
}