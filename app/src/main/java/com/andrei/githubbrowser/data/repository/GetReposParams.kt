package com.andrei.githubbrowser.data.repository
data class GetReposParams(
    val language: String,
    val sort: String = "stars",
    val order: String = "desc",
    val perPage: Int = 50,
    val page: Int = 1
)