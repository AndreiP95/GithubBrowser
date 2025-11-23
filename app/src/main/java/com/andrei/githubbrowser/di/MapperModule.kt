package com.andrei.githubbrowser.di

import com.andrei.githubbrowser.data.Mapper
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.data.model.dto.RepoDto
import com.andrei.githubbrowser.data.model.entity.RepoEntity
import com.andrei.githubbrowser.data.model.mapper.MapperRepoBoToRepoEntity
import com.andrei.githubbrowser.data.model.mapper.MapperRepoDtoToRepoBo
import com.andrei.githubbrowser.data.model.mapper.MapperRepoEntityToRepoBo
import com.andrei.githubbrowser.ui.base.mapper.MapperRepoBoToRepoUIModel
import com.andrei.githubbrowser.ui.model.RepoUiModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {
    @Binds
    @Singleton
    abstract fun bindRepoBoToRepoUIModel(
        impl: MapperRepoBoToRepoUIModel
    ): Mapper<RepoBo, RepoUiModel>

    @Binds
    @Singleton
    abstract fun bindRepoDtoToRepoBo(
        impl: MapperRepoDtoToRepoBo
    ): Mapper<RepoDto, RepoBo>

    @Binds
    @Singleton
    abstract fun bindRepoEntityToRepoBo(
        impl: MapperRepoEntityToRepoBo
    ): Mapper<RepoEntity, RepoBo>

    @Binds
    @Singleton
    abstract fun bindRepoBoToRepoEntity(
        impl: MapperRepoBoToRepoEntity
    ): Mapper<RepoBo, RepoEntity>
}