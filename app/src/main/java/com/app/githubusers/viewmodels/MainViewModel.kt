package com.app.githubusers.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.githubusers.api.SearchResult
import com.app.githubusers.data.repository.UsersRepository
import com.app.githubusers.models.User
import com.app.githubusers.models.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UsersRepository
) : ViewModel() {
    private var currentResult: Flow<PagingData<User>>? = null

    @ExperimentalPagingApi
    fun fetchUsers(): Flow<PagingData<User>> {
        val newResult: Flow<PagingData<User>> =
            repository.getUsers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    suspend fun getUserDetails(login: String): SearchResult<UserDetails> {
        return repository.getUserDetails(login)
    }

}