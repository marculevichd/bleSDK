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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.testsdkble.ui.theme.TestSDKBLETheme
import com.ido.ble.bluetooth.device.BLEDevice
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestSDKBLETheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationComponent(navController = navController)
                    RequestPermissions(LocalContext.current)
                }
            }
        }

    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    // Состояние для хранения списка найденных устройств
    var devices by remember { mutableStateOf(listOf<BLEDevice>()) }
    var loading = remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var isStartSearch by remember { mutableStateOf(false) }
    var resultMeth =
        remember { mutableStateOf("здесь будут результаты методов (кроме найденых устройств)") }
    val context = LocalContext.current
    val sdkManager = SDKManager(loading = loading, resultMeth = resultMeth)
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Button(
                onClick = {
                    Timber.d("??? MainScreen Button navigate clicked")
                    // Переход на экран установки настроек
                    navController.navigate("settings_screen")
                },
                enabled = isConnected
            ) {
                Text(text = "Перейти на экран установки настроек")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


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
                    sdkManager.getGetPuffArray()
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

            Row {
                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button setChildLockSetting 1")
                        Timber.d("??? Button setChildLockSetting 1")
                        scope.launch {
                            sdkManager.setChildLockSetting(0)
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "setChildLockSetting 0 min")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button setChildLockSetting 2 ")
                        Timber.d("??? Button setChildLockSetting 2")
                        scope.launch {
                            sdkManager.setChildLockSetting(2)
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "setChildLockSetting 2 min")
                }
                Spacer(modifier = Modifier.height(16.dp))
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

            Button(
                onClick = {
                    loading.value = true
                    println("??? Button set PuffsControl true puffNumber 15 puffTimeInMinutes 5")
                    Timber.d("??? Button set sessionMod true autoMode = false manualMode = true puffNumber 15 puffTimeInMinutes 5")
                    scope.launch {
                        sdkManager.setPuffControl(
                            autoMode = false,
                            manualMode = true,
                            puffNumber = 15,
                            puffTimeInMinutes = 5
                        )
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "set PuffsControl autoMode false manualMode true + 15/5")
            }
            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    loading.value = true
                    println("??? Button set sessionMod true autoMode = true manualMode = false puffNumber 15 puffTimeInMinutes 5")
                    Timber.d("??? Button set sessionMod true autoMode = true manualMode = false puffNumber 15 puffTimeInMinutes 5")
                    scope.launch {
                        sdkManager.setPuffControl(
                            autoMode = true,
                            manualMode = false,
                            puffNumber = 15,
                            puffTimeInMinutes = 5
                        )
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "set PuffsControl autoMode true manualMode false + 15/5")
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

            Row {
                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set sessionMod true puffNumber 15 puffTimeInMinutes 5")
                        Timber.d("??? Button set sessionMod true puffNumber 15 puffTimeInMinutes 5")
                        scope.launch {
                            sdkManager.setSessionMode(
                                sessionMode = false,
                                puffNumber = 15,
                                puffTimeInMinutes = 5
                            )
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set sessionMod true + 15/5")
                }

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set sessionMod false")
                        Timber.d("??? Button set sessionMod false")
                        scope.launch {
                            sdkManager.setSessionMode(
                                sessionMode = false,
                                puffNumber = null,
                                puffTimeInMinutes = null
                            )
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set sessionMod false")
                }
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


            Row {
                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set shield true")
                        Timber.d("??? Button set shield true")
                        scope.launch {
                            sdkManager.setShield(
                                bool = true,
                            )
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set shield true")
                }

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set shield false")
                        Timber.d("??? Button set shield false")
                        scope.launch {
                            sdkManager.setShield(
                                bool = false,
                            )
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set shield false")
                }
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

            Row {
                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set FriendMode true")
                        Timber.d("??? Button set FriendMode true")
                        scope.launch {
                            sdkManager.setGuestMode(
                                bool = true,
                            )
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set FriendMode true")
                }

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set FriendMode false")
                        Timber.d("??? Button set FriendMode false")
                        scope.launch {
                            sdkManager.setGuestMode(
                                bool = false,
                            )
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set FriendMode false")
                }
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

            Row {
                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set NightMode true")
                        Timber.d("??? Button set NightMode true")
                        scope.launch {
                            sdkManager.setNightMode(true)
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set NightMode true")
                }

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set NightMode false")
                        Timber.d("??? Button set NightMode false")
                        scope.launch {
                            sdkManager.setNightMode(false)
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set NightMode false")
                }
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

            Row {
                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set power 15")
                        Timber.d("??? Button set power 15")
                        scope.launch {
                            sdkManager.setPower(15)
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set power 15")
                }

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button set power 20")
                        Timber.d("??? Button set power 20")
                        scope.launch {
                            sdkManager.setPower(20)
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "set power 20")
                }
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
