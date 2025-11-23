package com.andrei.githubbrowser.domain.usecase

import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.repository.GetReposParams
import com.andrei.githubbrowser.data.repository.RepoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetReposUseCaseTest {

    private lateinit var repoRepository: RepoRepository
    private lateinit var getReposUseCase: GetReposUseCase

    @Before
    fun setUp() {
        repoRepository = mockk(relaxed = true)
        getReposUseCase = GetReposUseCase(repoRepository)
    }

    @Test
    fun `invoke should return repos from repository`() = runTest {
        val params = GetReposParams(language = "Kotlin")
        val expectedRepos = listOf(mockk<RepoBo>(), mockk<RepoBo>())
        coEvery { repoRepository.getRepos(params) } returns flowOf(expectedRepos)

        val result = getReposUseCase(params).first()

        assertEquals(expectedRepos, result)
    }
}