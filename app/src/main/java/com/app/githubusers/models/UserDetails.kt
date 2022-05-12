package com.app.githubusers.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetails(
    val name: String,
    val email: String?,
    @SerializedName("company")
    val organisation: String?,
    val bio: String?,
    val followers: String,
    val following: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    val location: String?
) : Parcelable