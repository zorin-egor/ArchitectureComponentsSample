package com.sample.architecturecomponent.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Users")
open class User : Parcelable, Comparable<User> {

    @Expose
    @PrimaryKey
    var id: Long = 0

    @Expose
    @ColumnInfo(name = "node_id")
    var nodeId: String = ""

    @Expose
    var login: String = ""

    @Expose
    var url: String = ""

    @Expose
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String = ""

    override fun equals(other: Any?): Boolean {
        return other is User &&
            id == other.id &&
            nodeId == other.nodeId &&
            login == other.login &&
            url == other.url &&
            avatarUrl == other.avatarUrl
    }

    override fun compareTo(other: User): Int {
        return compareValues(id, other.id)
    }
}