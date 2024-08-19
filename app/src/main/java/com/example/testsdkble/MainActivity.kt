package com.example.testsdkble

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.testsdkble.ui.theme.TestSDKBLETheme
import com.ido.ble.bluetooth.device.BLEDevice
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestSDKBLETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen()
                    RequestPermissions(LocalContext.current)
                }
            }
        }

    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Состояние для хранения списка найденных устройств
    var devices by remember { mutableStateOf(listOf<BLEDevice>()) }
    var loading by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var isBind by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sdkManager = SDKManager()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
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
        }

        item {
            Button(
                onClick = {
                    loading = true
                    println("??? Button Start Bind")
                    Timber.d("??? Button Start Bind")
                    sdkManager.bind(
                        onSuccess = {
                            loading = false
                            isConnected = false
                            isBind = true
                            Toast.makeText(context, "Успешная привязка", Toast.LENGTH_SHORT)
                                .show()
                            sdkManager.registerGetDeviceParaCallBack()
                        },
                        OnTrable = {
                            loading = false
                            isConnected = false
                            Toast.makeText(
                                context,
                                "Что-то пошло не так",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                enabled = isConnected
            ) {
                Text(text = "Start Bind (падает ошибка)")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading = true
                    println("??? Button Get Language")
                    Timber.d("??? Button Get Language")
                    sdkManager.getLanguage()
                },
                enabled = isConnected
            ) {
                Text(text = "Get Language")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading = true
                    println("??? Button Get PuffArray")
                    Timber.d("??? Button Get PuffArray")
                    sdkManager.getGetPuffArray()
                },
                enabled = isConnected
            ) {
                Text(text = "Get PuffArray")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }




        item {

            Text(text = "Found Devices:")

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                devices.forEachIndexed { device, bleDevice ->
                    ClickableText(
                        modifier = Modifier
                            .background(Color.Yellow)
                            .height(40.dp),
                        text = AnnotatedString(
                            ("девайс адрес " + devices[device].mDeviceAddress + " имя " + devices[device].mDeviceName)
                        ),
                        onClick = {
                            loading = true
                            sdkManager.connectByAddress(devices[device],
                                onSuccess = {
                                    loading = false
                                    isConnected = true
                                    Toast.makeText(
                                        context,
                                        "Успешное подключение",
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
        }

        item {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                )
            }
        }
    }
}
