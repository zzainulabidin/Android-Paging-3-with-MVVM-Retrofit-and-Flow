package dev.ronnie.allplayers.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Users(
    @SerializedName("items")
    val playersList: List<User>,
) : Parcelable