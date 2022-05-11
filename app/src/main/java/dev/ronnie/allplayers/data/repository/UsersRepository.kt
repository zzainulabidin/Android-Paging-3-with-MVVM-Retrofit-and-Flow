package dev.ronnie.allplayers.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.ronnie.allplayers.api.UsersApi
import dev.ronnie.allplayers.data.db.AppDataBase
import dev.ronnie.allplayers.data.remotediator.UsersRemoteMediator
import dev.ronnie.allplayers.models.User
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
    fun getPlayers(): Flow<PagingData<User>> {
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