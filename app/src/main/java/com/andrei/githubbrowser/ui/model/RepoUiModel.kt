package com.andrei.githubbrowser.ui.model

data class RepoUiModel(
    val name: String,
    val ownerName: String,
    val ownerAvatarUrl: String,
    val repoUrl: String,
    val forks: String,
    val stars: String,
    val description: String,
    val language: String,
    val updatedAt: String
)