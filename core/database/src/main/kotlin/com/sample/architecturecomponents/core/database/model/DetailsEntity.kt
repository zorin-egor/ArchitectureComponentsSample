package com.sample.architecturecomponents.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.sample.architecturecomponents.core.model.Details

@Entity(
    tableName = "Details",
    indices = [
        Index(value = ["user_id"], unique = true),
        Index(value = ["name"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class DetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "company") val company: String?,
    @ColumnInfo(name = "blog") val blog: String?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "bio") val bio: String?,
    @ColumnInfo(name = "public_repos") val publicRepos: Long?,
    @ColumnInfo(name = "public_gists") val publicGists: Long?,
    @ColumnInfo(name = "followers") val followers: Long?,
    @ColumnInfo(name = "following") val following: Long?,
    @ColumnInfo(name = "created_at") val createdAt: String?
)

data class UserAndDetails(
    @Embedded val details: DetailsEntity? = null,
    @Relation(
        entity = UserEntity::class,
        parentColumn = "user_id",
        entityColumn = "user_id"
    ) val user: UserEntity?
)

fun DetailsEntity.asExternalModel() = Details(
    id = userId,
    avatarUrl = avatarUrl,
    name = name,
    company = company,
    blog = blog,
    location = location,
    email = email,
    bio = bio,
    publicRepos = publicRepos,
    publicGists = publicGists,
    followers = followers,
    following = following,
    createdAt = createdAt,
)

fun UserEntity.toDetailsEntity() = DetailsEntity(
    id = 0,
    userId = userId,
    avatarUrl = avatarUrl,
    name = null,
    company = null,
    blog = null,
    location = null,
    email = null,
    bio = null,
    publicRepos = null,
    publicGists = null,
    followers = null,
    following = null,
    createdAt = null,
)