package com.andrei.githubbrowser.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.utils.StringProvider
import com.andrei.githubbrowser.data.Mapper
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.repository.GetReposParams
import com.andrei.githubbrowser.domain.list.RepoListAction
import com.andrei.githubbrowser.domain.list.RepoListResult
import com.andrei.githubbrowser.domain.list.RepoListState
import com.andrei.githubbrowser.domain.usecase.GetReposUseCase
import com.andrei.githubbrowser.ui.model.RepoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReposListViewModel @Inject constructor(
    private val getReposUseCase: GetReposUseCase,
    private val mapper: Mapper<RepoBo, RepoUiModel>,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableStateFlow(RepoListState())
    val state: StateFlow<RepoListState> = _state.asStateFlow()

    private val _result = MutableSharedFlow<RepoListResult>()
    val result = _result.asSharedFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        fetchRepos()
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    _state.update {
                        it.copy(
                            searchQuery = query,
                            page = 1,
                            isEndReached = false,
                            repos = emptyList()
                        )
                    }
                    fetchRepos(query, 1, isRefresh = false)
                }
        }
    }

    private fun fetchRepos(
        query: String = "",
        page: Int = 1,
        isRefresh: Boolean = false,
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(isRefreshing = isRefresh, errorMessage = null)
            }

            getReposUseCase(GetReposParams(language = query, page = page))
                .catch { e ->
                    _state.update { it.copy(isRefreshing = false, isLoadingMore = false) }
                    _result.emit(
                        RepoListResult.ShowError(
                            e.localizedMessage ?: stringProvider.getString(
                                R.string.repo_list_unknown_error
                            )
                        )
                    )
                }
                .collect { repoBos ->
                    val newRepos = repoBos.map { mapper.map(it) }

                    _state.update { currentState ->
                        val updatedList = if (page == 1) newRepos else currentState.repos + newRepos

                        currentState.copy(
                            repos = updatedList,
                            isRefreshing = false,
                            isLoadingMore = false,
                            isEndReached = newRepos.isEmpty()
                        )
                    }
                }
        }
    }

    fun onAction(action: RepoListAction) {
        when (action) {
            is RepoListAction.SearchLanguageChanged -> {
                queryFlow.value = action.language
            }

            is RepoListAction.Refresh -> {
                _state.update { it.copy(page = 1, isEndReached = false) }
                fetchRepos(_state.value.searchQuery, 1, isRefresh = true)
            }

            is RepoListAction.RepoSelected -> {
                viewModelScope.launch {
                    _result.emit(RepoListResult.NavigateToRepoDetail(action.repo))
                }
            }

            is RepoListAction.LoadMore -> {
                val state = _state.value
                if (state.isLoadingMore || state.isRefreshing || state.isEndReached) return

                val nextPage = state.page + 1
                _state.update { it.copy(page = nextPage, isLoadingMore = true) }
                fetchRepos(state.searchQuery, nextPage, isRefresh = false)
            }
        }
    }
}