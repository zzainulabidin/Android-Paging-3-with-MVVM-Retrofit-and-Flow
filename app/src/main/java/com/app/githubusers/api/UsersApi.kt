package com.app.githubusers.api

import com.app.githubusers.models.UserDetails
import com.app.githubusers.models.Users
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {

    @GET("search/users?sort=followers")
    suspend fun getUsers(
        @Query("q") q: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int,
    ): Users


    @GET("users/{user}")
    suspend fun getUserDetails(
        @Path("user") id: String
    ): UserDetails
}