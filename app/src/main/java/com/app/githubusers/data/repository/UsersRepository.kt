package com.app.githubusers.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.app.githubusers.api.UsersApi
import com.app.githubusers.data.db.AppDataBase
import com.app.githubusers.data.remotediator.UsersRemoteMediator
import com.app.githubusers.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersApi: UsersApi,
    private val db: AppDataBase
) {

    private val pagingSourceFactory = { db.usersDao.getUsers() }

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
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

}