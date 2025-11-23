package com.andrei.githubbrowser.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val ownerName: String,
    val ownerAvatar: String,
    val stars: Int,
    val forks: Int,
    val updatedAt: String,
    val language: String?,
    val repoUrl: String
)