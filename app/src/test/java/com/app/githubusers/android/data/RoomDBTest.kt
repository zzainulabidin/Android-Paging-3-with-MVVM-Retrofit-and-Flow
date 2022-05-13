package com.app.githubusers.android.data

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.app.githubusers.data.dao.UsersDao
import com.app.githubusers.data.db.AppDataBase
import com.app.githubusers.models.User
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class RoomDBTest: TestCase() {

    private lateinit var usersDao: UsersDao
    private lateinit var db: AppDataBase


    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDataBase::class.java
        ).build()
        usersDao = db.usersDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadSpend() = runBlocking {
        usersDao.insertUsers(getUsersDto())
        val users: PagingSource<Int, User> = usersDao.getUsers()

        Assert.assertNotNull(users)
    }

    private fun getUsersDto(): List<User> {
            return listOf(
                User(
                    userId = 1,
                    login = "microsoftopensource",
                    avatarUrl = "https://avatars.githubusercontent.com/u/22527892?v=4"
                ),
                User(
                    userId = 2,
                    login = "catalinmiron",
                    avatarUrl = "https://avatars.githubusercontent.com/u/2805320?v=4"
                )
            )
    }
}
