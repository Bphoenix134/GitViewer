package com.myapplication.navigation

import java.net.URLEncoder

sealed class NavRoute(val route: String) {

    data object Search : NavRoute("search")

    data object RepoInfo : NavRoute("repo/{owner}/{repo}") {
        fun create(owner: String, repo: String) =
            "repo/$owner/$repo"
    }

    data object File : NavRoute("file/{name}/{url}") {
        fun create(name: String, url: String) =
            "file/$name/${URLEncoder.encode(url, "UTF-8")}"
    }
}
