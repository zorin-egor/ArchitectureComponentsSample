package com.sample.architecturecomponent.core.common.tests

import com.sample.architecturecomponents.core.common.extensions.safeIndex
import com.sample.architecturecomponents.core.common.extensions.safeSubList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ListExtTest {

    @Test
    fun safeIndexTest() = runTest {
        val items = mutableListOf<Int>()
        repeat(5) { items.add(it) }

        assertEquals(items.lastIndex, items.safeIndex(10))
        assertEquals(items.lastIndex, items.safeIndex(5))
        assertEquals(4, items.safeIndex(4))
        assertEquals(0, items.safeIndex(-2))
        assertEquals(0, emptyList<Int>().safeIndex(3))
    }

    @Test
    fun safeSubListTest() = runTest {
        val items = mutableListOf<Int>()
        repeat(5) { items.add(it) }
        assertEquals(items.subList(0, 2), items.safeSubList(0, 2))
        assertEquals(items.subList(0, 5), items.safeSubList(0, 5))
        assertEquals(items.subList(0, 5), items.safeSubList(0, 10))
        assertEquals(items.subList(2, 4), items.safeSubList(2, 4))
        assertEquals(emptyList(), items.safeSubList(0, 0))
        assertEquals(emptyList(), items.safeSubList(10, 10))
        assertEquals(emptyList(), items.safeSubList(-1, -1))
    }
}
