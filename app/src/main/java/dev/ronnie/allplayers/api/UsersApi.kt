package dev.ronnie.allplayers.api

import dev.ronnie.allplayers.models.Users
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {

    @GET("search/users?sort=followers")
    suspend fun getUsers(
        @Query("q") q: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int,
    ): Users
}