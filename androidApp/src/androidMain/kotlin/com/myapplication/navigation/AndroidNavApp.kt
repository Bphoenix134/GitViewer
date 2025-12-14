package com.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myapplication.presentation.screen.FileViewerScreen
import com.myapplication.presentation.screen.RepoInfoScreen
import com.myapplication.presentation.screen.RepoSearchScreen
import org.koin.androidx.compose.koinViewModel
import presentation.viewmodel.RepoViewModel
import java.net.URLDecoder

@Composable
fun AndroidNavApp(
    viewModel: RepoViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoute.Search.route
    ) {

        composable(NavRoute.Search.route) {
            RepoSearchScreen(
                viewModel = viewModel,
                onRepoClick = { owner, repo ->
                    navController.navigate(
                        NavRoute.RepoInfo.create(owner, repo)
                    )
                }
            )
        }

        composable(
            route = NavRoute.RepoInfo.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { entry ->
            RepoInfoScreen(
                owner = entry.arguments!!.getString("owner")!!,
                repo = entry.arguments!!.getString("repo")!!,
                viewModel = viewModel,
                onFileClick = { name, url ->
                    navController.navigate(
                        NavRoute.File.create(name, url)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoute.File.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType }
            )
        ) { entry ->
            FileViewerScreen(
                fileName = entry.arguments!!.getString("name")!!,
                url = URLDecoder.decode(
                    entry.arguments!!.getString("url")!!,
                    "UTF-8"
                ),
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}