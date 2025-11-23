package com.andrei.githubbrowser.domain.usecase

import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.repository.RepoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRepoDetailsUseCaseTest {

    private lateinit var repoRepository: RepoRepository
    private lateinit var getRepoDetailsUseCase: GetRepoDetailsUseCase

    @Before
    fun setUp() {
        repoRepository = mockk(relaxed = true)
        getRepoDetailsUseCase = GetRepoDetailsUseCase(repoRepository)
    }

    @Test
    fun `invoke should return repo details from repository`() = runTest {
        val owner = "owner"
        val name = "name"
        val expectedRepoBo = mockk<RepoBo>()
        coEvery { repoRepository.getRepoDetails(owner, name) } returns flowOf(expectedRepoBo)

        val result = getRepoDetailsUseCase(owner, name).first()

        assertEquals(expectedRepoBo, result)
    }
}