package com.sample.architecturecomponent.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
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
    var avatar_url: String? = null

    override fun equals(other: Any?): Boolean {
        return other is UserItem &&
            id == other.id &&
            nodeId == other.nodeId &&
            login == other.login &&
            url == other.url &&
            avatar_url == other.avatar_url
    }
}