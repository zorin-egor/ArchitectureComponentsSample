package com.sample.architecturecomponents.core.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionEffect(
    permission: String,
    onSuccessResult: () -> Unit = {},
    onDeniedResult: () -> Unit = {}
) {
    if (LocalInspectionMode.current) return
    val permissionState = rememberPermissionState(permission = permission) {
        Timber.d("PermissionEffect() - rememberPermissionState($it)")
        if (it) {
            onSuccessResult()
        } else {
            onDeniedResult()
        }
    }

    LaunchedEffect(permissionState) {
        Timber.d("PermissionEffect() - LaunchedEffect($permissionState)")
        when(val status = permissionState.status) {
            PermissionStatus.Granted -> onSuccessResult()
            is PermissionStatus.Denied -> if (!status.shouldShowRationale) {
                permissionState.launchPermissionRequest()
            } else {
                onDeniedResult()
            }
        }
    }
}

@Composable
fun PermissionEffectState(
    permission: String,
    onSuccessResult: () -> Unit = {},
    onDeniedResult: () -> Unit = {}
): MutableState<Boolean> {
    val state = remember { mutableStateOf(false) }
    if (!state.value) {
        return state
    }

    PermissionEffect(
        permission = permission,
        onSuccessResult = {
            state.value = false
            onSuccessResult()
        },
        onDeniedResult = {
            state.value = false
            onDeniedResult()
        }
    )

    return state
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsEffect(
    permissions: List<String>,
    onSuccessResult: () -> Unit = {},
    onDeniedResult: (Map<String, Boolean>) -> Unit = {}
) {
    if (LocalInspectionMode.current) return

    val shouldRationale = remember { mutableListOf<String>() }
    val permissionsState = rememberMultiplePermissionsState(
        permissions = permissions,
        onPermissionsResult = { items ->
            val deniedPermissions = items.filter { !it.value }.keys.toList()
            if (deniedPermissions.isNotEmpty()) {
                val deniedPermissionsRationale = deniedPermissions.associateWith { denied ->
                    (shouldRationale.find { it == denied } != null)
                }
                Timber.d("PermissionsEffect() - onPermissionsResult() - onDeniedResult()")
                onDeniedResult(deniedPermissionsRationale)
            } else {
                Timber.d("PermissionsEffect() - onPermissionsResult() - onSuccessResult()")
                onSuccessResult()
            }
        }
    )

    Timber.d("PermissionsEffect() - shouldRationale: $shouldRationale")

    LaunchedEffect(permissionsState) {
        Timber.d("PermissionsEffect() - LaunchedEffect")

        var allGranted = true
        var needRequest = false
        permissionsState.permissions.forEach { permission ->
            if (permission.status is PermissionStatus.Denied) {
                allGranted = false
                if (permission.status.shouldShowRationale) {
                    shouldRationale.add(permission.permission)
                } else {
                    needRequest = true
                }
            }
        }

        Timber.d("PermissionsEffect() - shouldRationale - onSuccessResult()")

        when {
            allGranted -> {
                Timber.d("PermissionsEffect() - LaunchedEffect - onSuccessResult()")
                onSuccessResult()
            }
            needRequest -> {
                Timber.d("PermissionsEffect() - LaunchedEffect - launchMultiplePermissionRequest()")
                permissionsState.launchMultiplePermissionRequest()
            }
            else -> {
                val items = shouldRationale.associateWith { true }
                Timber.d("PermissionsEffect() - LaunchedEffect - onDeniedResult($items)")
                onDeniedResult(items)
            }
        }
    }
}

@Composable
fun PermissionsEffectState(
    permissions: List<String>,
    onSuccessResult: () -> Unit = {},
    onDeniedResult: (Map<String, Boolean>) -> Unit = {}
): MutableState<Boolean> {
    Timber.d("PermissionsEffectState()")

    val state = remember { mutableStateOf(false) }
    if (!state.value) {
        return state
    }

    Timber.d("PermissionsEffectState() - PermissionsEffect")

    PermissionsEffect(
        permissions = permissions,
        onSuccessResult = {
            state.value = false
            onSuccessResult()
        },
        onDeniedResult = {
            state.value = false
            onDeniedResult(it)
        }
    )

    return state
}