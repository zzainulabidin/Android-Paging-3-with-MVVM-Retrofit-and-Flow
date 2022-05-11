package dev.ronnie.allplayers.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ronnie.allplayers.data.repository.UsersRepository
import dev.ronnie.allplayers.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UsersRepository
) : ViewModel() {
    private var currentResult: Flow<PagingData<User>>? = null

    @ExperimentalPagingApi
    fun searchUsers(): Flow<PagingData<User>> {
        val newResult: Flow<PagingData<User>> =
            repository.getPlayers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}