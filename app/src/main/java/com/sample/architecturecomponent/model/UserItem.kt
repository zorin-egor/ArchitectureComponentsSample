package com.sample.architecturecomponent.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class UserItem : Parcelable {

    @Expose
    var id: String? = null

    @Expose
    var nodeId: String? = null

    @Expose
    var login: String? = null

    @Expose
    var url: String? = null

    @Expose
    @SerializedName("avatar_url")
    var avatarUrl: String? = null

    override fun equals(other: Any?): Boolean {
        return other is UserItem &&
            id == other.id &&
            nodeId == other.nodeId &&
            login == other.login &&
            url == other.url &&
            avatarUrl == other.avatarUrl
    }
}