package com.app.githubusers.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.githubusers.models.User

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(list: List<User>)

    @Query("SELECT * FROM users_table")
    fun getUsers(): PagingSource<Int, User>

    @Query("SELECT COUNT(userId) from users_table")
    suspend fun count(): Int

}