package com.sample.architecturecomponent.vo

import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
class DetailsItem : UserItem() {

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

    override fun equals(other: Any?): Boolean {
        return other is DetailsItem &&
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