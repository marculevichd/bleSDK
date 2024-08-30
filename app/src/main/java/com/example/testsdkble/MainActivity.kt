package com.example.testsdkble

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.testsdkble.ui.theme.TestSDKBLETheme
import com.ido.ble.bluetooth.device.BLEDevice
import timber.log.Timber
import java.io.File

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
    var loading = remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var isStartSearch by remember { mutableStateOf(false) }
    var isBind by remember { mutableStateOf(false) }
    var resultMeth =
        remember { mutableStateOf("здесь будут результаты методов (кроме найденых устройств)") }
    val context = LocalContext.current
    val sdkManager = SDKManager(loading = loading, resultMeth = resultMeth)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        item {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .background(Color.Yellow)
            ) {
                if (loading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Text(
                    color = Color.Black,
                    text = resultMeth.value
                )
            }
        }
        item {
            Button(onClick = {
                loading.value = true
                println("??? MainScreen Button clicked")
                Timber.d("??? MainScreen Button clicked")
                sdkManager.startScan(onFind = { device ->
                    println("??? MainScreen startScan onFind")
                    Timber.d("??? MainScreen startScan onFind")
                    if (device != null) {
                        loading.value = true

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
                loading.value = true
                println("??? MainScreen Button logs")
                Timber.d("??? MainScreen Button logs")


                    val logsDirectory = File(context.getExternalFilesDir(null), "MyLogs")
                    if (!logsDirectory.exists()) {
                        logsDirectory.mkdirs()  // Создаем каталог, если его нет
                    }
                    val filePath = "${logsDirectory.absolutePath}/flashlog.txt"

                    sdkManager.collectDeviceAllFlashLog(filePath)
                },
                enabled = isConnected
            ) {
                Text(text = "Start collect logs")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button Get PuffArray")
                    Timber.d("??? Button Get PuffArray")
//                    sdkManager.getGetPuffArray()
                },
                enabled = isConnected
            ) {
                Text(text = "Get PuffArray")
                Text(text = "Недоступно")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row {
                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button setCigarettesStartSearchDevice")
                        Timber.d("??? Button setCigarettesStartSearchDevice")
                        sdkManager.setCigarettesStartSearchDevice()
                        isStartSearch = true
                    },
                    enabled = isConnected && !isStartSearch,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "setCigarettesStartSearchDevice")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button setCigarettesStopSearchDeivce")
                        Timber.d("??? Button setCigarettesStopSearchDeivce")
                        sdkManager.setCigarettesStopSearchDeivce()
                        isStartSearch = false
                    },
                    enabled = isConnected && isStartSearch,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "setCigarettesStopSearchDeivce")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getGetChildLockSetting")
                    Timber.d("??? Button getGetChildLockSetting")
                    sdkManager.getGetChildLockSetting()
                },
                enabled = isConnected
            ) {
                Text(text = "getGetChildLockSetting")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getDeviceInfo")
                    Timber.d("??? Button getDeviceInfo")
                    sdkManager.getDeviceInfo()
                },
                enabled = isConnected
            ) {
                Text(text = "getDeviceInfo")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getPuffsControl")
                    Timber.d("??? Button getPuffsControl")
                    sdkManager.getPuffsControl()
                },
                enabled = isConnected
            ) {
                Text(text = "getPuffsControl")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getSessionMode")
                    Timber.d("??? Button getSessionMode")
                    sdkManager.getSessionMode()
                },
                enabled = isConnected
            ) {
                Text(text = "getSessionMode")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getConsciousShield")
                    Timber.d("??? Button getConsciousShield")
                    sdkManager.getConsciousShield()
                },
                enabled = isConnected
            ) {
                Text(text = "getConsciousShield")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getFriendMode")
                    Timber.d("??? Button getFriendMode")
                    sdkManager.getFriendMode()
                },
                enabled = isConnected
            ) {
                Text(text = "getFriendMode")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getMetaAi")
                    Timber.d("??? Button getMetaAi")
                    sdkManager.getMetaAi()
                },
                enabled = isConnected
            ) {
                Text(text = "getMetaAi")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getByttery")
                    Timber.d("??? Button getByttery")
                    sdkManager.getByttery()
                },
                enabled = isConnected
            ) {
                Text(text = "getByttery")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getScreen")
                    Timber.d("??? Button getScreen")
                    sdkManager.getScreen()
                },
                enabled = isConnected
            ) {
                Text(text = "getScreen")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getVolume")
                    Timber.d("??? Button getVolume")
                    sdkManager.getVolume()
                },
                enabled = isConnected
            ) {
                Text(text = "getVolume")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getLanguage")
                    Timber.d("??? Button getLanguage")
                    sdkManager.getLanguage()
                },
                enabled = isConnected
            ) {
                Text(text = "getLanguage")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getNightMode")
                    Timber.d("??? Button getNightMode")
                    sdkManager.getNightMode()
                },
                enabled = isConnected
            ) {
                Text(text = "getNightMode")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getPowerSetting")
                    Timber.d("??? Button getPowerSetting")
                    sdkManager.getPowerSetting()
                },
                enabled = isConnected
            ) {
                Text(text = "getPowerSetting")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getOverPuffSetting")
                    Timber.d("??? Button getOverPuffSetting")
                    sdkManager.getOverPuffSetting()
                },
                enabled = isConnected
            ) {
                Text(text = "getOverPuffSetting")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getPuffTotalNumber")
                    Timber.d("??? Button getPuffTotalNumber")
                    sdkManager.getPuffTotalNumber()
                },
                enabled = isConnected
            ) {
                Text(text = "getPuffTotalNumber")
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
                            loading.value = true
                            sdkManager.connectByAddress(devices[device],
                                onSuccess = {
                                    sdkManager.registerGetDeviceParaCallBack()
                                    loading.value = true
                                    isConnected = true
                                    Toast.makeText(
                                        context,
                                        "Успешное подключение",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                                OnTrable = {
                                    loading.value = true
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

    }
}
