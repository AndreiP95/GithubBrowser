package com.andrei.githubbrowser.data.repository

import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.model.mapper.MapperRepoBoToRepoEntity
import com.andrei.githubbrowser.data.model.mapper.MapperRepoDtoToRepoBo
import com.andrei.githubbrowser.data.model.mapper.MapperRepoEntityToRepoBo
import com.andrei.githubbrowser.data.sources.local.RepoDao
import com.andrei.githubbrowser.data.sources.remote.GithubRepoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRepository @Inject constructor(
    private val api: GithubRepoService,
    private val repoDao: RepoDao,
    private val mapperRepoDtoToRepoBo: MapperRepoDtoToRepoBo,
    private val mapperRepoEntityToRepoBo: MapperRepoEntityToRepoBo,
    private val mapperRepoBoToRepoEntity: MapperRepoBoToRepoEntity
) {

    fun getRepos(params: GetReposParams): Flow<List<RepoBo>> = flow {
        try {
            val query =
                if (params.language.isBlank()) "stars:>1000" else "language:${params.language}"
            val response = api.searchRepos(
                query = query,
                sort = params.sort,
                order = params.order,
                perPage = params.perPage,
                page = params.page
            )
            val repoBos = response.items.map { mapperRepoDtoToRepoBo.map(it) }
            if (params.page == 1) {
                repoDao.clearRepos()
                repoDao.insertRepos(repoBos.map { mapperRepoBoToRepoEntity.map(it) })
            }
            emit(repoBos)
        } catch (e: IOException) { // More specific exception
            if (params.page == 1) {
                val localRepos = repoDao.getRepos().first()
                if (localRepos.isNotEmpty()) {
                    emit(localRepos.map { mapperRepoEntityToRepoBo.map(it) })
                } else {
                    throw e
                }
            } else {
                throw e
            }
        }
    }

    fun getRepoDetails(repoOwner: String, repoName: String): Flow<RepoBo?> = flow<RepoBo?> {
        val repoDto = api.getRepoRetails(repoOwner, repoName)
        val repoBo = mapperRepoDtoToRepoBo.map(repoDto)
        emit(repoBo)
    }
}
