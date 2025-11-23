package com.andrei.githubbrowser.domain.list

import com.andrei.githubbrowser.ui.model.RepoUiModel

sealed interface RepoListAction {
    data class SearchLanguageChanged(val language: String) : RepoListAction
    data class RepoSelected(val repo: RepoUiModel) : RepoListAction
    object Refresh : RepoListAction
    object LoadMore : RepoListAction

}