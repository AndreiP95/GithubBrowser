package com.andrei.githubbrowser.data.model.mapper

import com.andrei.githubbrowser.data.Mapper
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.model.entity.RepoEntity
import javax.inject.Inject

class MapperRepoBoToRepoEntity @Inject constructor() : Mapper<RepoBo, RepoEntity> {
    override fun map(from: RepoBo) = RepoEntity(
        id = from.id,
        name = from.name,
        description = from.description,
        ownerName = from.ownerName,
        ownerAvatar = from.ownerAvatar,
        stars = from.stars,
        forks = from.forks,
        updatedAt = from.updatedAt,
        language = from.language,
        repoUrl = from.repoUrl
    )
}