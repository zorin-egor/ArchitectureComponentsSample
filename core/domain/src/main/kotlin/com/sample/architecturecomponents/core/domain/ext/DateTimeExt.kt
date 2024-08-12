package com.sample.architecturecomponents.core.domain.ext

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val Instant.toFormatterDateTime: String?
    get() = kotlin.runCatching {
        val dateTime = toLocalDateTime(TimeZone.UTC)
        "${dateTime.date.dayOfMonth} ${dateTime.date.month.toString().lowercase()} ${dateTime.date.year} " +
            "${dateTime.time.hour}:${dateTime.time.minute}:${dateTime.time.second}"
    }.getOrNull()