package com.andrei.githubbrowser.domain.usecase

import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoDetailsUseCase @Inject constructor(
    private val repoRepository: RepoRepository
) {
    operator fun invoke(repoOwner: String, repoName: String): Flow<RepoBo?> {
        return repoRepository.getRepoDetails(repoOwner, repoName)
    }
}