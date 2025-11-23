package com.andrei.githubbrowser.ui.viewmodels

import app.cash.turbine.test
import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.data.Mapper
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.domain.list.RepoListAction
import com.andrei.githubbrowser.domain.list.RepoListResult
import com.andrei.githubbrowser.domain.usecase.GetReposUseCase
import com.andrei.githubbrowser.ui.list.ReposListViewModel
import com.andrei.githubbrowser.ui.model.RepoUiModel
import com.andrei.githubbrowser.utils.StringProvider
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReposListViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var getReposUseCase: GetReposUseCase

    @RelaxedMockK
    private lateinit var mapper: Mapper<RepoBo, RepoUiModel>

    @RelaxedMockK
    private lateinit var stringProvider: StringProvider

    private lateinit var viewModel: ReposListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ReposListViewModel(getReposUseCase, mapper, stringProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search language changed success`() = runTest {
        val repoBo = mockk<RepoBo>()
        val repoUiModel = mockk<RepoUiModel>()
        coEvery { getReposUseCase(any()) } returns flowOf(listOf(repoBo))
        coEvery { mapper.map(repoBo) } returns repoUiModel

        viewModel.onAction(RepoListAction.SearchLanguageChanged("Kotlin"))
        advanceTimeBy(301)

        val state = viewModel.state.value
        assertEquals(1, state.repos.size)
        assertEquals(repoUiModel, state.repos[0])
        assertFalse(state.isEndReached)
    }

    @Test
    fun `search returns empty list`() = runTest {
        coEvery { getReposUseCase(any()) } returns flowOf(emptyList())

        viewModel.onAction(RepoListAction.SearchLanguageChanged("empty"))
        advanceTimeBy(301)

        val state = viewModel.state.value
        assertTrue(state.repos.isEmpty())
        assertTrue(state.isEndReached)
    }


    @Test
    fun `load more success`() = runTest {
        val repoBo = mockk<RepoBo>()
        val repoUiModel = mockk<RepoUiModel>()
        coEvery { getReposUseCase(match { it.page == 1 }) } returns flowOf(listOf(repoBo))
        coEvery { getReposUseCase(match { it.page == 2 }) } returns flowOf(listOf(repoBo))
        coEvery { mapper.map(repoBo) } returns repoUiModel

        viewModel.onAction(RepoListAction.SearchLanguageChanged("Kotlin"))
        advanceTimeBy(301)
        viewModel.onAction(RepoListAction.LoadMore)
        advanceTimeBy(1)

        val state = viewModel.state.value
        assertEquals(2, state.repos.size)
        assertEquals(2, state.page)
    }

    @Test
    fun `refresh success`() = runTest {
        val repoBo = mockk<RepoBo>()
        val repoUiModel = mockk<RepoUiModel>()
        coEvery { getReposUseCase(any()) } returns flowOf(listOf(repoBo))
        coEvery { mapper.map(repoBo) } returns repoUiModel

        viewModel.onAction(RepoListAction.SearchLanguageChanged("Kotlin"))
        advanceTimeBy(301)
        viewModel.onAction(RepoListAction.Refresh)
        advanceTimeBy(1)

        val state = viewModel.state.value
        assertEquals(1, state.repos.size)
        assertEquals(1, state.page)
        assertFalse(state.isEndReached)
    }

    @Test
    fun `show error on fetch`() = runTest {
        val errorMessage = "Network Error"
        coEvery { getReposUseCase(any()) } returns flow { throw Exception(errorMessage) }
        coEvery { stringProvider.getString(R.string.repo_list_unknown_error) } returns "Unknown error"

        viewModel.result.test {
            viewModel.onAction(RepoListAction.SearchLanguageChanged("Kotlin"))
            advanceTimeBy(301)

            val result = awaitItem()
            assertEquals(RepoListResult.ShowError(errorMessage), result)
        }
    }
}