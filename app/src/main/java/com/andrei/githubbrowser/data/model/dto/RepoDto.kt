package com.andrei.githubbrowser.data.model.dto

import com.google.gson.annotations.SerializedName

data class RepoDto(
    val id: Long,
    val name: String,
    val description: String?,
    val owner: Owner,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    val language: String?,
    @SerializedName("html_url")
    val repoUrl: String
)

data class Owner(
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)