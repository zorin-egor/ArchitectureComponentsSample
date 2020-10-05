package com.sample.architecturecomponent.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.db.DetailsDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.model.Details
import com.sample.architecturecomponent.model.User


class DetailsRepository(
    private val context: Context,
    private val retrofitTool: RetrofitTool<Api>,
    private val detailsDao: DetailsDao
) {

    fun getDetails(item: User): LiveData<Details> {
        return liveData {

        }
    }

}
