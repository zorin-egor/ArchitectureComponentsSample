package com.sample.architecturecomponents.app.ui.repositories_details_list_2_pane

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryDetailsList2PaneArgs(
    val userOwner: String,
    val userUrl: String
) : Parcelable
