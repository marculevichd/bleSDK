package com.example.testsdkble

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.testsdkble.ui.theme.TestSDKBLETheme
import com.ido.ble.bluetooth.device.BLEDevice
import timber.log.Timber


class MainActivity : ComponentActivity() {

    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION
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
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Timber.d("All permissions granted")
                // Вызывайте ваш метод startScan() здесь, если разрешения предоставлены
            } else {
                Timber.e("Not all permissions granted")
                // Обработка случая, когда не все разрешения предоставлены
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
}


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Состояние для хранения списка найденных устройств
    var devices by remember { mutableStateOf(listOf<BLEDevice>()) }
    var devices2 by remember { mutableStateOf(listOf<BLEDevice>()) }
    var loading by remember { mutableStateOf(false) }
    var loading2 by remember { mutableStateOf(false) }
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

        // Отображение списка устройств
        devices.forEach { device ->
            ClickableText(
                text = AnnotatedString(
                    ("девайс адрес " + device.mDeviceAddress + " имя " + device.mDeviceName)
                        ?: "Unknown Device"
                ),
                onClick = {
                    loading = true

                    println("??? MainScreen Clicked on device: ${device.mDeviceAddress}")
                    Timber.d("??? MainScreen Clicked on device: ${device.mDeviceAddress}")

                    sdkManager.connectByAddress(device,
                        onSuccess = {
                            loading = false
                            Toast.makeText(context, "Успешное подключение", Toast.LENGTH_SHORT).show()
                            sdkManager.getDeviceInfo()
                            Toast.makeText(context, "Началось getDeviceInfo", Toast.LENGTH_SHORT).show()
                        },
                        OnTrable = {
                            loading = false
                            Toast.makeText(context, "Что то пошло не так. Глянуть логи надо", Toast.LENGTH_SHORT).show()
                        }
                    )
                    // Здесь можно добавить логику, например, подключение к устройству
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (devices.size != 0) {
            Text(text = "Сверху кликабельный мак-адрес- начнется подключение")
        }

        if (loading){
            Spacer(modifier = Modifier.height(100.dp))
            CircularProgressIndicator()
        }


        Button(onClick = {
            loading2 = true
            println("??? MainScreen ButtonBYNAME clicked")
            Timber.d("??? MainScreen ButtonBYNAME clicked")
            sdkManager.startScanByName(onFind = { device ->
                println("??? MainScreen startScanBYNAME onFind")
                Timber.d("??? MainScreen startScanBYNAME onFind")
                if (device != null) {
                    loading2 = false

                    // Создаем новый список с добавленным устройством
                    devices2 = devices2 + device
                    println("??? MainScreen startScanBYNAME device added: ${device.mDeviceAddress}")
                    Timber.d("??? MainScreen startScanBYNAME device added: ${device.mDeviceAddress}")
                }
            })
        }) {
            Text(text = "Scan By Name")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Found Devices by name:")

        // Отображение списка устройств
        devices2.forEach { device ->
            ClickableText(
                text = AnnotatedString(
                    ("девайс адрес " + device.mDeviceAddress + " имя " + device.mDeviceName)
                        ?: "Unknown Device"
                ),
                onClick = {
                    loading2 = true

                    println("??? MainScreen Clicked on device2: ${device.mDeviceAddress}")
                    Timber.d("??? MainScreen Clicked on device2: ${device.mDeviceAddress}")

                    sdkManager.connectByAddress(device,
                        onSuccess = {
                            loading2 = false
                            Toast.makeText(context, "Успешное подключение", Toast.LENGTH_SHORT).show()
                            sdkManager.getDeviceInfo()
                            Toast.makeText(context, "Началось getDeviceInfo", Toast.LENGTH_SHORT).show()
                        },
                        OnTrable = {
                            loading2 = false
                            Toast.makeText(context, "Что то пошло не так. Глянуть логи надо", Toast.LENGTH_SHORT).show()
                        }
                    )
                    // Здесь можно добавить логику, например, подключение к устройству
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (devices2.size != 0) {
            Text(text = "Сверху кликабельный мак-адрес- начнется подключение")
        }

        if (loading2){
            Spacer(modifier = Modifier.height(100.dp))
            CircularProgressIndicator()
        }

    }
}
