package com.sample.architecturecomponents.core.common.extensions

import kotlin.math.max
import kotlin.math.min

fun <T> List<T>.safeIndex(index: Int): Int = max(0, min(index, lastIndex))

fun <T> List<T>.safeSubList(from: Int, to: Int): List<T> {
    val indexOf = fun(index: Int) = max(0, min(index, size))
    return subList(indexOf(from), indexOf(to))
}