package com.andrei.githubbrowser.domain.details

sealed interface RepoDetailsAction {
    data class LoadDetails(val repoOwner: String, val repoName: String) : RepoDetailsAction
}