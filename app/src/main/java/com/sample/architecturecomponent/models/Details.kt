package com.sample.architecturecomponent.models

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "Details",
    indices = [
        Index(value = ["user_id"], unique = true),
        Index(value = ["name"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
class Details(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "update_time")
    var updateTime: Long = 0,

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "user_id")
    var userId: Long = -1,

    @Expose
    var name: String? = null,

    @Expose
    var company: String? = null,

    @Expose
    var blog: String? = null,

    @Expose
    var location: String? = null,

    @Expose
    var email: String? = null,

    @Expose
    var bio: String? = null,

    @Expose
    @SerializedName("public_repos")
    @ColumnInfo(name = "public_repos")
    var publicRepos: String? = null,

    @Expose
    @SerializedName("public_gists")
    @ColumnInfo(name = "public_gists")
    var publicGists: String? = null,

    @Expose
    var followers: String? = null,

    @Expose
    var following: String? = null,

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null

) : Parcelable {

    companion object {
        const val UPDATE_TIME = 60 * 1000
    }

    @Ignore
    @IgnoredOnParcel
    @Expose(serialize = false, deserialize = false)
    val isUpdateTime: Boolean = System.currentTimeMillis() - updateTime > UPDATE_TIME

}