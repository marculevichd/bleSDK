package com.example.testsdkble

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.testsdkble.ui.theme.TestSDKBLETheme
import com.ido.ble.bluetooth.device.BLEDevice
import timber.log.Timber


class MainActivity : ComponentActivity() {

    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,

        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.NEARBY_WIFI_DEVICES,
        Manifest.permission.BLUETOOTH_ADVERTISE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestSDKBLETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen()
                    RequestPermissions()
                }
            }
        }

    }


    @Composable
    fun RequestPermissions() {
        val context = this@MainActivity
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
                Toast.makeText(context, "Не предоставлены разрешения: $deniedPermissions", Toast.LENGTH_LONG).show()
            }


//            // Обработка результата запроса разрешений
//            val allGranted = permissions.values.all { it }
//            if (allGranted) {
//                Timber.d("All permissions granted")
//                // Вызывайте ваш метод startScan() здесь, если разрешения предоставлены
//            } else {
//                Timber.e("Not all permissions granted")
//                // Обработка случая, когда не все разрешения предоставлены
//            }
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
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Состояние для хранения списка найденных устройств
    var devices by remember { mutableStateOf(listOf<BLEDevice>()) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sdkManager = SDKManager()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = {
            loading = true
            println("??? MainScreen Button clicked")
            Timber.d("??? MainScreen Button clicked")
            sdkManager.startScan(onFind = { device ->
                println("??? MainScreen startScan onFind")
                Timber.d("??? MainScreen startScan onFind")
                if (device != null) {
                    loading = false

                    // Создаем новый список с добавленным устройством
                    devices = devices + device
                    println("??? MainScreen device added: ${device.mDeviceAddress}")
                    Timber.d("??? MainScreen device added: ${device.mDeviceAddress}")
                }
            })
        }) {
            Text(text = "Start Scan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Found Devices:")

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(devices.size) { device ->
                ClickableText(
                    modifier = Modifier
                        .background(Color.Yellow)
                        .height(40.dp),
                    text = AnnotatedString(
                        ("девайс адрес " + devices[device].mDeviceAddress + " имя " + devices[device].mDeviceName)
                            ?: "Unknown Device"
                    ),
                    onClick = {
                        loading = true
                        sdkManager.connectByAddress(devices[device],
                            onSuccess = {
                                loading = false
                                Toast.makeText(context, "Успешное подключение", Toast.LENGTH_SHORT)
                                    .show()
                                sdkManager.getDeviceInfo()
                                Toast.makeText(
                                    context,
                                    "Началось getDeviceInfo",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            },
                            OnTrable = {
                                loading = false
                                Toast.makeText(
                                    context,
                                    "Что-то пошло не так. Глянуть логи надо",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (devices.size != 0) {
            Text(text = "Сверху кликабельный мак-адрес- начнется подключение")
        }

        if (loading) {
            Spacer(modifier = Modifier.height(100.dp))
            CircularProgressIndicator()
        }

    }
}
