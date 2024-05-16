package com.sample.architecturecomponents.core.domain.ext

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val Instant.toFormatterDateTime: String
    get() = toLocalDateTime(TimeZone.UTC).let {
        "${it.date.year}/${it.date.monthNumber}/${it.date.dayOfMonth} ${it.time.hour}:${it.time.minute}:${it.time.second}"
    }