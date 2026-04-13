package com.example.spotify.ui.component.permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Stable
sealed interface PermissionStatus {
    data object Granted : PermissionStatus
    data class Denied(
        val shouldShowRationale: Boolean
    ) : PermissionStatus
}

val PermissionStatus.isGranted: Boolean
    get() = this == PermissionStatus.Granted

val PermissionStatus.shouldShowRationale: Boolean
    get() = when (this) {
        PermissionStatus.Granted -> false
        is PermissionStatus.Denied -> shouldShowRationale
    }


@Composable
internal fun PermissionLifecycleCheckerEffect(
    permissionState: MutablePermissionState,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME
) {
    // Check if the permission was granted when the lifecycle is resumed.
    // The user might've gone to the Settings screen and granted the permission.
    val permissionCheckerObserver = remember(permissionState) {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                // If the permission is revoked, check again.
                // We don't check if the permission was denied as that triggers a process restart.
                if (permissionState.status != PermissionStatus.Granted) {
                    permissionState.refreshPermissionStatus()
                }
            }
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionCheckerObserver) {
        lifecycle.addObserver(permissionCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionCheckerObserver) }
    }
}


@Composable
internal fun PermissionsLifecycleCheckerEffect(
    permissions: List<MutablePermissionState>,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME
) {
    // Check if the permission was granted when the lifecycle is resumed.
    // The user might've gone to the Settings screen and granted the permission.
    val permissionsCheckerObserver = remember(permissions) {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                for (permission in permissions) {
                    // If the permission is revoked, check again. We don't check if the permission
                    // was denied as that triggers a process restart.
                    if (permission.status != PermissionStatus.Granted) {
                        permission.refreshPermissionStatus()
                    }
                }
            }
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionsCheckerObserver) {
        lifecycle.addObserver(permissionsCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionsCheckerObserver) }
    }
}

/**
 * Find the closest Activity in a given Context.
 */
internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

internal fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

internal fun Activity.shouldShowRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

@Composable
fun PermissionRequired(
    permissionState: PermissionState,
    notGrantedContent: @Composable () -> Unit,
    deniedContent: @Composable () -> Unit,
    grantedContent: @Composable () -> Unit
) {
    Crossfade(targetState = permissionState.status, label = "permissionState") {
        if (it.isGranted) {
            grantedContent()
        } else if (!it.shouldShowRationale) {
            notGrantedContent()
        } else {
            deniedContent()
        }
    }

}