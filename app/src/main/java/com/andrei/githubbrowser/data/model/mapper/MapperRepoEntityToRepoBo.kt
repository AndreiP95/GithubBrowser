package com.andrei.githubbrowser.data.model.mapper

import com.andrei.githubbrowser.data.Mapper
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.model.entity.RepoEntity
import javax.inject.Inject

class MapperRepoEntityToRepoBo @Inject constructor() : Mapper<RepoEntity, RepoBo> {
    override fun map(from: RepoEntity) = RepoBo(
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