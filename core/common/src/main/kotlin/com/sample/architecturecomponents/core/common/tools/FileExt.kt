package com.sample.architecturecomponents.core.common.tools

import android.content.Context
import timber.log.Timber
import java.io.File
import java.util.LinkedList
import kotlin.system.measureNanoTime


private inline fun <T> queueRec(startItem: T, action: (T) -> List<T>?) {
    val queue = LinkedList<T>()
    queue.add(startItem)

    while (queue.isNotEmpty()) {
        val item = queue.poll() ?: continue
        action(item)?.let(queue::addAll)
    }
}

private fun getDirSizeQueue(dir: File): Long {
    Timber.d("Path: ${dir.absolutePath}")

    var size = 0L
    queueRec(dir) {
        if (it.isDirectory) {
            size += it.length()
            it.listFiles()?.toList()
        } else {
            size += it.length()
            null
        }
    }
    return size
}

private fun getDirSizeRec(dir: File): Long {
    Timber.d("Path: ${dir.absolutePath}")

    val files = dir.listFiles() ?: return 0
    var size: Long = dir.length()
    for (file in files) {
        size += if (file.isDirectory) {
            getDirSizeRec(file)
        } else {
            file.length()
        }
    }
    return size
}

private val Context.cacheFolders get() = arrayOf(filesDir, codeCacheDir, cacheDir)

fun File.treeSize(): Long = walkTopDown().map { it.length() }.sum()

fun Context.getCacheSizeDefault(): Long = cacheFolders.sumOf { it.treeSize() }

fun Context.getCacheSizeQueue(): Long = cacheFolders.sumOf(::getDirSizeQueue)

fun Context.getCacheSizeRec(): Long = cacheFolders.sumOf(::getDirSizeRec)


private const val THRESHOLD = 1024

private val UNITS = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")

fun getFormatedSize(size: Long, decimals: Int = 2): String {
    var value = size.toDouble()
    var index = 0
    while (index < UNITS.size - 1 && value >= THRESHOLD) {
        value /= 1024.0
        ++index
    }
    return String.format("%." + decimals + "f %s", value, UNITS[index])
}

fun Context.getCacheSize(roundToZero: Boolean = false): String {
    var size: Long
    val time = measureNanoTime { size = getCacheSizeDefault() }
    if (roundToZero && size < 100000) {
        size = 0
    }
    Timber.tag("getCacheSize").d("time: ${time / 1000000}")
    return getFormatedSize(size)
}

fun Context.clearCache(): Boolean {
    return cacheFolders.fold(true) { acc, item ->
        acc && item.deleteRecursively()
    }
}
