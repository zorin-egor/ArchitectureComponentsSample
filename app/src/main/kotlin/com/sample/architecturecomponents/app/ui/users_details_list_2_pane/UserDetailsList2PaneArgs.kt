package com.sample.architecturecomponents.app.ui.users_details_list_2_pane

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailsList2PaneArgs(
    val userId: Long,
    val userUrl: String
) : Parcelable