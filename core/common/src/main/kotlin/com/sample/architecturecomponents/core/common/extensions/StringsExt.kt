package com.sample.architecturecomponents.core.common.extensions


val String.isEmailPattern: Boolean
    get() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()