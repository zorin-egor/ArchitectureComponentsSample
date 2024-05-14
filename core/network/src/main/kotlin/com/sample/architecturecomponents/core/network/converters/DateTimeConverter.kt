package com.sample.architecturecomponents.core.network.converters

import kotlinx.datetime.Instant

private const val DATE_TIME_TEMPLATE = "yyyy-MM-ddTHH:mm:ssZ"

fun dateTimeConverter(dateTime: String): Instant {
    return Instant.parse(dateTime)
}