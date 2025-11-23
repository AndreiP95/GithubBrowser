package com.andrei.githubbrowser.data.sources.remote

import com.andrei.githubbrowser.data.model.RepoSearchResponse
import com.andrei.githubbrowser.data.model.dto.RepoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubRepoService {

@GET("search/repositories")
suspend fun searchRepos(
    @Query("q") query: String = "stars:>1000",
    @Query("sort") sort: String = "stars",
    @Query("order") order: String = "desc",
    @Query("per_page") perPage: Int = 50,
    @Query("page") page: Int = 1
): RepoSearchResponse

    @GET("repos/{owner}/{repo}")
    suspend fun getRepoRetails(
        @Path("owner") owner: String,
        @Path("repo") name: String
    ): RepoDto

    
}