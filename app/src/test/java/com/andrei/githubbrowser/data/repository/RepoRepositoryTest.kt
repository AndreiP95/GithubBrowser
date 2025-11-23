package com.andrei.githubbrowser.data.repository

import com.andrei.githubbrowser.data.model.RepoSearchResponse
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.model.dto.RepoDto
import com.andrei.githubbrowser.data.model.mapper.MapperRepoBoToRepoEntity
import com.andrei.githubbrowser.data.model.mapper.MapperRepoDtoToRepoBo
import com.andrei.githubbrowser.data.model.mapper.MapperRepoEntityToRepoBo
import com.andrei.githubbrowser.data.sources.local.RepoDao
import com.andrei.githubbrowser.data.sources.remote.GithubRepoService
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepoRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var api: GithubRepoService

    @RelaxedMockK
    private lateinit var repoDao: RepoDao

    @RelaxedMockK
    private lateinit var mapperRepoDtoToRepoBo: MapperRepoDtoToRepoBo

    @RelaxedMockK
    private lateinit var mapperRepoEntityToRepoBo: MapperRepoEntityToRepoBo

    @RelaxedMockK
    private lateinit var mapperRepoBoToRepoEntity: MapperRepoBoToRepoEntity

    private lateinit var repoRepository: RepoRepository

    @Before
    fun setUp() {
        repoRepository = RepoRepository(
            api,
            repoDao,
            mapperRepoDtoToRepoBo,
            mapperRepoEntityToRepoBo,
            mapperRepoBoToRepoEntity
        )
    }

    @Test
    fun `getRepos should return list of RepoBo`() = runTest {
        val repoDto = mockk<RepoDto>()
        val repoSearchResponse = RepoSearchResponse(items = listOf(repoDto))
        val expectedRepoBo = mockk<RepoBo>()
        val params = GetReposParams(language = "Kotlin")
        coEvery { api.searchRepos(any(), any(), any(), any(), any()) } returns repoSearchResponse
        coEvery { mapperRepoDtoToRepoBo.map(repoDto) } returns expectedRepoBo

        val result = repoRepository.getRepos(params).first()

        assertEquals(listOf(expectedRepoBo), result)
    }

    @Test
    fun `getRepoDetails should return RepoBo`() = runTest {
        val owner = "owner"
        val name = "name"
        val repoDto = mockk<RepoDto>()
        val expectedRepoBo = mockk<RepoBo>()
        coEvery { api.getRepoRetails(owner, name) } returns repoDto
        coEvery { mapperRepoDtoToRepoBo.map(repoDto) } returns expectedRepoBo

        val result = repoRepository.getRepoDetails(owner, name).first()

        assertEquals(expectedRepoBo, result)
    }
}