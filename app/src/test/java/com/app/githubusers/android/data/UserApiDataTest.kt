package com.app.githubusers.android.data

import com.app.githubusers.api.UsersApi
import com.app.githubusers.models.User
import com.app.githubusers.models.Users
import com.app.githubusers.utils.QUERY_USER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserApiDataTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val usersApi: UsersApi = mock()
//    private val appDataBase: AppDataBase = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when remote api returns success Then repo should also return success with correct mapping`() {
        val usersDto = getUsersDto()
        runTest {
            whenever(usersApi.getUsers(QUERY_USER, 10, 1)).thenReturn(usersDto)
            val obj = usersApi.getUsers(QUERY_USER, 10, 1)
            val userList = obj.usersList

            assertEquals(getUsersDto().usersList.size, userList.size)
            assertEquals(getUsersDto().usersList[0], userList[0])
            assertEquals(getUsersDto().usersList[1], userList[1])
        }
    }

    private fun getUsersDto(): Users {
        return Users(
            usersList = listOf(
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
        )
    }
}
