package com.sample.architecturecomponents.core.notifications

enum class NotificationImageSize(val width: Int, val height: Int) {
    Minimum(512, 256),
    Balanced(1024, 512),
    Maximum(2048, 1024);
}