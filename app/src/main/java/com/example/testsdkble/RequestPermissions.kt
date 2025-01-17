package com.example.testsdkble

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import timber.log.Timber

@Composable
fun RequestPermissions(context: Context) {

    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.INTERNET,
        // для андройда 8 и 9
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    // Лаунчер для запроса разрешений
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        // Обработка результата запроса разрешений
        val deniedPermissions = permissions.filterValues { !it }.keys
        if (deniedPermissions.isEmpty()) {
            Timber.d("All permissions granted")
            // Вызывайте ваш метод startScan() здесь, если разрешения предоставлены
        } else {
            Timber.e("Not all permissions granted: $deniedPermissions")
            // Вы можете уведомить пользователя о том, какие разрешения не были предоставлены
            Toast.makeText(
                context,
                "Не предоставлены разрешения: $deniedPermissions",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(key1 = Unit) {
        // Проверка разрешений
        val allPermissionsGranted = REQUIRED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (!allPermissionsGranted) {
            multiplePermissionsLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            Timber.d("Permissions already granted")
        }
    }
}