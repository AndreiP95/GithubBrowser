package com.andrei.githubbrowser.domain.usecase

import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.repository.GetReposParams
import com.andrei.githubbrowser.data.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val repoRepository: RepoRepository
) {
    operator fun invoke(
        params: GetReposParams
    ): Flow<List<RepoBo>> {
        return repoRepository.getRepos(params)
    }
}