package dev.ronnie.allplayers.data.remotediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.ronnie.allplayers.api.UsersApi
import dev.ronnie.allplayers.data.db.AppDataBase
import dev.ronnie.allplayers.data.entity.RemoteKeys
import dev.ronnie.allplayers.models.User
import dev.ronnie.allplayers.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class UsersRemoteMediator(
    private val service: UsersApi,
    private val db: AppDataBase
) : RemoteMediator<Int, User>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (db.usersDao.count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getUsers("users", state.config.pageSize, page)

            val playersList = apiResponse.playersList

            db.withTransaction {
                val nextKey = page + 1

                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        nextKey = nextKey,
                        isEndReached = false
                    )
                )
                db.usersDao.insertUsers(playersList)
            }
            return MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }


}