package com.sample.architecturecomponent.core.common

import app.cash.turbine.test
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.common.result.asResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ResultKtTest {

    @Test
    fun resultCatchesErrors() = runTest {
        flow {
            emit(2)
            throw Exception("Test Done")
        }
        .asResult()
        .test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(1), awaitItem())

            when (val errorResult = awaitItem()) {
                Result.Loading,
                is Result.Success -> throw IllegalStateException("The flow should have emitted an Error Result",)
                is Result.Error -> assertEquals("Test Done", errorResult.exception.message,)
            }

            awaitComplete()
        }
    }
}
