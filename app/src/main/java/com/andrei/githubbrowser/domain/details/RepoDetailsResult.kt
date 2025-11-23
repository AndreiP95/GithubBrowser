package com.andrei.githubbrowser.domain.details

sealed interface RepoDetailsResult {
    data class ShowError(val message: String) : RepoDetailsResult
}