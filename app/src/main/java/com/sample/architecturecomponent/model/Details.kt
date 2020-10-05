package com.sample.architecturecomponent.model

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


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
    val id: Long

) : Parcelable, Comparable<Details> {

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "user_id")
    var userId: String? = null

    @Expose
    var name: String? = null

    @Expose
    var company: String? = null

    @Expose
    var blog: String? = null

    @Expose
    var location: String? = null

    @Expose
    var email: String? = null

    @Expose
    var bio: String? = null

    @Expose
    @SerializedName("public_repos")
    @ColumnInfo(name = "public_repos")
    var publicRepos: String? = null

    @Expose
    @SerializedName("public_gists")
    @ColumnInfo(name = "public_gists")
    var publicGists: String? = null

    @Expose
    var followers: String? = null

    @Expose
    var following: String? = null

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null

    override fun equals(other: Any?): Boolean {
        return other is Details &&
            userId == other.userId &&
            name == other.name &&
            company == other.company &&
            blog == other.blog &&
            location == other.location &&
            email == other.email &&
            bio == other.bio &&
            publicRepos == other.publicRepos &&
            publicGists == other.publicGists &&
            followers == other.followers &&
            following == other.following &&
            createdAt == other.createdAt
    }

    override fun compareTo(other: Details): Int {
        return compareValues(id, other.id)
    }

}