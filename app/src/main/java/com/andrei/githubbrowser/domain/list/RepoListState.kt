package com.andrei.githubbrowser.domain.list

import androidx.compose.runtime.Immutable
import com.andrei.githubbrowser.ui.model.RepoUiModel

@Immutable
data class RepoListState(
    val repos: List<RepoUiModel> = emptyList(),
    val searchQuery: String = "",
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val isLoadingMore: Boolean = false,
    val page: Int = 1,
    val isEndReached: Boolean = false
)