package com.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import com.myapplication.di.androidModule
import com.myapplication.navigation.AndroidNavApp
import com.myapplication.presentation.theme.GitViewerTheme
import di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(listOf(appModule, androidModule))
        }

        setContent {
            GitViewerTheme {
                AndroidNavApp()
            }
        }
    }
}