package com.andrei.githubbrowser.domain

import com.andrei.githubbrowser.ui.model.RepoUiModel

data class RepoDetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val details: RepoUiModel? = null
)