package com.sample.architecturecomponents.core.network.dev

import java.io.InputStream

fun interface DevAssetManager {
    fun open(fileName: String): InputStream
}
