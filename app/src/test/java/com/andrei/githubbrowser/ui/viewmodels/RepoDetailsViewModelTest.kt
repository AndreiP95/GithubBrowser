package com.andrei.githubbrowser.ui.viewmodels

import app.cash.turbine.test
import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.domain.RepoDetailsUiState
import com.andrei.githubbrowser.domain.details.RepoDetailsAction
import com.andrei.githubbrowser.domain.details.RepoDetailsResult
import com.andrei.githubbrowser.domain.usecase.GetRepoDetailsUseCase
import com.andrei.githubbrowser.ui.base.mapper.MapperRepoBoToRepoUIModel
import com.andrei.githubbrowser.ui.details.RepoDetailsViewModel
import com.andrei.githubbrowser.ui.model.RepoUiModel
import com.andrei.githubbrowser.utils.StringProvider
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepoDetailsViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var getRepoDetailsUseCase: GetRepoDetailsUseCase

    @RelaxedMockK
    private lateinit var mapper: MapperRepoBoToRepoUIModel

    @RelaxedMockK
    private lateinit var stringProvider: StringProvider

    private lateinit var viewModel: RepoDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RepoDetailsViewModel(getRepoDetailsUseCase, mapper, stringProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDetails success`() = runTest {
        val repoBo = mockk<RepoBo>()
        val repoUiModel = mockk<RepoUiModel>()
        coEvery { getRepoDetailsUseCase.invoke(any(), any()) } returns flowOf(repoBo)
        coEvery { mapper.map(repoBo) } returns repoUiModel

        viewModel.state.test {
            assertEquals(RepoDetailsUiState(), awaitItem()) // Initial state

            viewModel.onAction(RepoDetailsAction.LoadDetails("owner", "name"))

            assertEquals(RepoDetailsUiState(isLoading = true), awaitItem()) // Loading state
            assertEquals(RepoDetailsUiState(isLoading = false, details = repoUiModel), awaitItem()) // Success state
        }

        verify { getRepoDetailsUseCase.invoke("owner", "name") }
        verify { mapper.map(repoBo) }
    }

    @Test
    fun `loadDetails error`() = runTest {
        val errorMessage = "An error occurred"
        coEvery { getRepoDetailsUseCase.invoke(any(), any()) } returns flow { throw Exception(errorMessage) }
        coEvery { stringProvider.getString(R.string.repo_details_unknown_error) } returns "Unknown error occurred."

        viewModel.results.test { 
            viewModel.onAction(RepoDetailsAction.LoadDetails("owner", "name"))
            val result = awaitItem()
            assertEquals(RepoDetailsResult.ShowError(errorMessage), result)
        }

        val state = viewModel.state.value

        assertEquals(errorMessage, state.errorMessage)
        assertEquals(false, state.isLoading)
        assertNull(state.details)
    }
}