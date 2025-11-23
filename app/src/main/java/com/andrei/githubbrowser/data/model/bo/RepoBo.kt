package com.andrei.githubbrowser.data.model.bo

data class RepoBo(
    val id: Long,
    val name: String,
    val description: String?,
    val ownerName: String = "",
    val ownerAvatar: String = "",
    val stars: Int,
    val forks: Int,
    val updatedAt: String,
    val language: String?,
    val repoUrl: String
)