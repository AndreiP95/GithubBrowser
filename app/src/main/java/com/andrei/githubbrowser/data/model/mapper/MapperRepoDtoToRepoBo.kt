package com.andrei.githubbrowser.data.model.mapper

import com.andrei.githubbrowser.data.Mapper
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.model.dto.RepoDto
import javax.inject.Inject

class MapperRepoDtoToRepoBo @Inject constructor() : Mapper<RepoDto, RepoBo> {

    override fun map(from: RepoDto) = RepoBo(
        id = from.id,
        name = from.name,
        description = from.description,
        ownerName = from.owner.login,
        ownerAvatar = from.owner.avatarUrl,
        stars = from.stargazersCount,
        forks = from.forksCount,
        updatedAt = from.updatedAt,
        language = from.language,
        repoUrl = from.repoUrl
    )
}