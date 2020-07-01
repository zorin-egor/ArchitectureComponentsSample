package com.sample.architecturecomponent.vo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

open class UserItem : Parcelable {

    companion object CREATOR : Parcelable.Creator<UserItem> {
        override fun createFromParcel(parcel: Parcel): UserItem {
            return UserItem(parcel)
        }

        override fun newArray(size: Int): Array<UserItem?> {
            return arrayOfNulls(size)
        }
    }

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

    constructor(parcel: Parcel) {
        id = parcel.readString()
        nodeId = parcel.readString()
        login = parcel.readString()
        url = parcel.readString()
        avatar_url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nodeId)
        parcel.writeString(login)
        parcel.writeString(url)
        parcel.writeString(avatar_url)
    }

    override fun describeContents(): Int {
        return 0
    }

}