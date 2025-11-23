package com.andrei.githubbrowser.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.domain.RepoDetailsUiState
import com.andrei.githubbrowser.domain.details.RepoDetailsAction
import com.andrei.githubbrowser.domain.details.RepoDetailsResult
import com.andrei.githubbrowser.domain.usecase.GetRepoDetailsUseCase
import com.andrei.githubbrowser.ui.base.mapper.MapperRepoBoToRepoUIModel
import com.andrei.githubbrowser.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val getRepoDetailsUseCase: GetRepoDetailsUseCase,
    private val mapper: MapperRepoBoToRepoUIModel,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableStateFlow(RepoDetailsUiState(isLoading = false))
    val state: StateFlow<RepoDetailsUiState> = _state.asStateFlow()

    private val _results = MutableSharedFlow<RepoDetailsResult>()
    val results = _results.asSharedFlow()

    fun onAction(action: RepoDetailsAction) {
        when (action) {
            is RepoDetailsAction.LoadDetails -> loadDetails(action.repoOwner, action.repoName)
        }
    }

    private fun loadDetails(owner: String, name: String) {
        viewModelScope.launch {
            if (_state.value.isLoading || _state.value.details != null) return@launch

            _state.update { it.copy(isLoading = true, errorMessage = null) }

            getRepoDetailsUseCase(owner, name)
                .catch { e ->
                    val errorMessage = e.localizedMessage
                        ?: stringProvider.getString(R.string.repo_details_unknown_error)

                    _results.emit(RepoDetailsResult.ShowError(errorMessage))

                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
                .collect { repoUiModel ->
                    repoUiModel?.let {
                        val displayModel = mapper.map(repoUiModel)

                        _state.update {
                            it.copy(
                                isLoading = false,
                                details = displayModel
                            )
                        }
                    }
                }
        }
    }
}
