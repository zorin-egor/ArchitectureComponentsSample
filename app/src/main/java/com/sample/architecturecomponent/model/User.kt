package com.sample.architecturecomponent.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "Users",
    indices = [
        Index(value = ["user_id"], unique = true),
        Index(value = ["login"])
    ]
)
open class User(

    @PrimaryKey(autoGenerate = true)
    val id: Long

) : Parcelable, Comparable<User> {

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "user_id")
    var userId: String? = null

    @Expose
    @ColumnInfo(name = "node_id")
    var nodeId: String? = null

    @Expose
    var login: String? = null

    @Expose
    var url: String? = null

    @Expose
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null

    override fun equals(other: Any?): Boolean {
        return other is User &&
            userId == other.userId &&
            nodeId == other.nodeId &&
            login == other.login &&
            url == other.url &&
            avatarUrl == other.avatarUrl
    }

    override fun compareTo(other: User): Int {
        return compareValues(id, other.id)
    }
}