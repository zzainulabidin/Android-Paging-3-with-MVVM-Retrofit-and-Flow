package com.app.githubusers.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.app.githubusers.R
import com.app.githubusers.Utils
import com.app.githubusers.api.SearchResult
import com.app.githubusers.databinding.ActivityUserDetailsBinding
import com.app.githubusers.models.UserDetails
import com.app.githubusers.utils.INTENT_USER_LOGIN
import com.app.githubusers.viewmodels.MainViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserDetailsBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)

        val login = intent.getStringExtra(INTENT_USER_LOGIN)
        login?.let { populateUserDetails(it) }
        handleUI()
    }

    private fun handleUI() {
        binding.back.setOnClickListener { finish() }
    }

    private fun populateUserDetails(login: String) {
        if (Utils.isNetworkAvailable(this@UserDetailsActivity)) {
            lifecycleScope.launch {
                val searchResult = viewModel.getUserDetails(login)
                binding.progressBar.isVisible = false
                when (searchResult) {
                    is SearchResult.Success -> populateUI(searchResult.data)
                    is SearchResult.Error -> displayError(searchResult.exception)
                }

            }
            binding.progressBar.isVisible = true
        } else {
            Utils.showNetworkError(this@UserDetailsActivity)
        }
    }

    private fun populateUI(userDetails: UserDetails) {
        Glide.with(binding.profileImage).load(userDetails.avatarUrl)
            .placeholder(R.drawable.user_icon_placeholder).into(binding.profileImage)
        binding.user = userDetails
    }

    private fun displayError(exception: Throwable) {
        Toast.makeText(
            this@UserDetailsActivity,
            this@UserDetailsActivity.getString(R.string.error_fetching_user_details),
            Toast.LENGTH_LONG
        ).show()
        exception.localizedMessage?.let { Log.e(UserDetailsActivity::class.java.name, it) }
    }
}