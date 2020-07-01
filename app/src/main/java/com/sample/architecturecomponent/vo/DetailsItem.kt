package com.sample.architecturecomponent.vo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

class DetailsItem : UserItem {

    companion object CREATOR : Parcelable.Creator<DetailsItem> {
        override fun createFromParcel(parcel: Parcel): DetailsItem {
            return DetailsItem(parcel)
        }

        override fun newArray(size: Int): Array<DetailsItem?> {
            return arrayOfNulls(size)
        }
    }

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
    var publicRepos: String? = null

    @Expose
    var publicGists: String? = null

    @Expose
    var followers: String? = null

    @Expose
    var following: String? = null

    @Expose
    var createdAt: String? = null

    constructor(parcel: Parcel) : super(parcel) {
        name = parcel.readString()
        company = parcel.readString()
        blog = parcel.readString()
        location = parcel.readString()
        email = parcel.readString()
        bio = parcel.readString()
        publicRepos = parcel.readString()
        publicGists = parcel.readString()
        followers = parcel.readString()
        following = parcel.readString()
        createdAt = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(name)
        parcel.writeString(company)
        parcel.writeString(blog)
        parcel.writeString(location)
        parcel.writeString(email)
        parcel.writeString(bio)
        parcel.writeString(publicRepos)
        parcel.writeString(publicGists)
        parcel.writeString(followers)
        parcel.writeString(following)
        parcel.writeString(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

}