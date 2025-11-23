package com.andrei.githubbrowser.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andrei.githubbrowser.domain.list.RepoListResult
import com.andrei.githubbrowser.ui.details.RepoDetailsScreenContainer
import com.andrei.githubbrowser.ui.list.ReposListScreenContainer
import com.andrei.githubbrowser.ui.list.ReposListViewModel

@Composable
fun GitHubNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "repoList") {
        composable("repoList") {
            val viewModel: ReposListViewModel = hiltViewModel()
            val resultFlow = viewModel.result

            LaunchedEffect(Unit) {
                resultFlow.collect { result ->
                    when (result) {
                        is RepoListResult.NavigateToRepoDetail -> {
                            navController.navigate("repoDetails/${result.repo.ownerName}/${result.repo.name}")
                        }
                        is RepoListResult.ShowError -> {
                            
                        }
                    }
                }
            }

            ReposListScreenContainer()
        }
        composable(
            route = "repoDetails/{owner}/{name}",
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""

            RepoDetailsScreenContainer(repoOwner = owner, repoName = name) {
                navController.popBackStack()
            }
        }
    }
}
