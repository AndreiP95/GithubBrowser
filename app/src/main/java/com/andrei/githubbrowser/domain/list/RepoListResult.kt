package com.andrei.githubbrowser.domain.list

import com.andrei.githubbrowser.ui.model.RepoUiModel

sealed interface RepoListResult {
    data class NavigateToRepoDetail(val repo: RepoUiModel) : RepoListResult
    data class ShowError(val message: String) : RepoListResult
}