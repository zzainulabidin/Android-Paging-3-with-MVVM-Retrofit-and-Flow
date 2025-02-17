package com.app.githubusers.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.githubusers.R
import com.app.githubusers.adapters.UsersAdapter
import com.app.githubusers.adapters.UsersLoadingStateAdapter
import com.app.githubusers.databinding.ActivityUsersListBinding
import com.app.githubusers.utils.INTENT_USER_LOGIN
import com.app.githubusers.utils.RecyclerViewItemDecoration
import com.app.githubusers.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UsersListActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityUsersListBinding
    private val adapter =
        UsersAdapter { login: String -> handleItemClick(login) }

    private var searchJob: Job? = null

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_users_list)

        setUpAdapter()
        startSearchJob()
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

    }

    @ExperimentalPagingApi
    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.fetchUsers()
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun handleItemClick(login: String) {
        val myIntent = Intent(this@UsersListActivity, UserDetailsActivity::class.java)
        myIntent.putExtra(INTENT_USER_LOGIN, login)
        this@UsersListActivity.startActivity(myIntent)
    }

    private fun setUpAdapter() {

        binding.allProductRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@UsersListActivity)
            setHasFixedSize(true)
            addItemDecoration(RecyclerViewItemDecoration())
        }
        binding.allProductRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = UsersLoadingStateAdapter { retry() }
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {

                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = true
                }
                binding.errorTxt.isVisible = false

            } else {
                binding.progress.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        binding.errorTxt.isVisible = true
                        binding.errorTxt.text = it.error.localizedMessage
                    }

                }

            }
        }

    }

    private fun retry() {
        adapter.retry()
    }

}
