package com.sample.architecturecomponent.models
import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "Users",
    indices = [
        Index(value = ["user_id"], unique = true),
        Index(value = ["login"])
    ]
)
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "update_time")
    var updateTime: Long = 0,

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "user_id")
    var userId: Long = -1,

    @Expose
    @ColumnInfo(name = "node_id")
    var nodeId: String? = null,

    @Expose
    var login: String? = null,

    @Expose
    var url: String? = null,

    @Expose
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null

) : Parcelable {

    companion object {
        const val UPDATE_TIME = 30 * 60 * 1000
    }

    @Ignore
    @IgnoredOnParcel
    @Expose(serialize = false, deserialize = false)
    val isUpdateTime: Boolean = System.currentTimeMillis() - updateTime > UPDATE_TIME

}