package com.myapplication.di

import com.myapplication.utils.AndroidFileDownloader
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import utils.FileDownloader

val androidModule = module {

    single<FileDownloader> {
        AndroidFileDownloader(
            context = androidContext(),
            client = get()
        )
    }
}
