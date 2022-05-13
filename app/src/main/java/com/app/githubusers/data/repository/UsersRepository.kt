package com.app.githubusers.data.repository

import androidx.paging.*
import com.app.githubusers.api.SearchResult
import com.app.githubusers.api.UsersApi
import com.app.githubusers.data.db.AppDataBase
import com.app.githubusers.data.remotediator.UsersRemoteMediator
import com.app.githubusers.models.User
import com.app.githubusers.models.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersApi: UsersApi,
    private val db: AppDataBase
) {

    @ExperimentalPagingApi
    fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = UsersRemoteMediator(
                usersApi,
                db
            ),
            pagingSourceFactory = { pagingSourceFactory() }
        ).flow
    }

    private fun pagingSourceFactory(): PagingSource<Int, User> {
        return db.usersDao.getUsers()
    }

    suspend fun getUserDetails(login: String): SearchResult<UserDetails> =
        withContext(Dispatchers.IO) {
            try {
                val data = usersApi.getUserDetails(login)
                SearchResult.Success(data)
            } catch (ex: Exception) {
                SearchResult.Error(ex)
            }
        }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

}