package com.andrei.githubbrowser.data.model

import com.andrei.githubbrowser.data.model.dto.RepoDto

data class RepoSearchResponse(
    val items: List<RepoDto>
)