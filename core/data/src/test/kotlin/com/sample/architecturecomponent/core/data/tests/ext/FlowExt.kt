package com.sample.architecturecomponent.core.data.tests.ext

import com.sample.architecturecomponents.core.common.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlin.test.assertEquals

internal suspend inline fun <A> Flow<Result<A>>.firstSuccess(): A = toList().let {
    assert(it.size == 2)
    assertEquals(Result.Loading, it.first())
    assertEquals(Result.Success::class.simpleName, it[1]::class.simpleName)
    (it[1] as Result.Success<A>).data
}