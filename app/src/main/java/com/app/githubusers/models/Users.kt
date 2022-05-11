package com.app.githubusers.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Users(
    @SerializedName("items")
    val usersList: List<User>,
) : Parcelable