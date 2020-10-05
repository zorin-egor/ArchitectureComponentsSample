package com.sample.architecturecomponent.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository {

    protected var foregroundContext: CoroutineContext = Dispatchers.Main

    protected var job = SupervisorJob()

    protected val scope = CoroutineScope(job + foregroundContext)

    fun cancelAll() {
        job.cancelChildren()
    }

}
