package dev.ronnie.allplayers.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id:Long,
    val login : String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
) : Parcelable