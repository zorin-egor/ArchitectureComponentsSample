package com.sample.architecturecomponent.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(
    tableName = "Details"
//    foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = ["id"],
//            childColumns = ["id"]
//        )
//    ]
)
class Details : User() {

    @Expose
    var name: String = ""

    @Expose
    var company: String = ""

    @Expose
    var blog: String = ""

    @Expose
    var location: String = ""

    @Expose
    var email: String = ""

    @Expose
    var bio: String = ""

    @Expose
    @SerializedName("public_repos")
    @ColumnInfo(name = "public_repos")
    var publicRepos: String = ""

    @Expose
    @SerializedName("public_gists")
    @ColumnInfo(name = "public_gists")
    var publicGists: String = ""

    @Expose
    var followers: String = ""

    @Expose
    var following: String = ""

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String = ""

    override fun equals(other: Any?): Boolean {
        return other is Details &&
            super.equals(other) &&
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

}