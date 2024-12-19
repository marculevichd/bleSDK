package com.example.testsdkble

import android.bluetooth.BluetoothAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.compose.foundation.layout.size
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
import kotlinx.coroutines.delay
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
    val sdkManager = SDKManager(loading = loading, resultMeth = resultMeth, context = context)
    val scope = rememberCoroutineScope()

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
                val deviceName = Settings.Global.getString(context.contentResolver, "device_name")
                println("??? deviceName = ${deviceName ?: "${Build.MANUFACTURER} ${Build.MODEL}"}")
                Timber.d("??? deviceName = ${deviceName ?: "${Build.MANUFACTURER} ${Build.MODEL}"}")
                resultMeth.value = deviceName ?: "${Build.MANUFACTURER} ${Build.MODEL}"
            }) {
                Text(text = "GET DEVICE NAME")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(onClick = {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val blueToothName = bluetoothAdapter.name
                println("??? blueToothName = $blueToothName")
                resultMeth.value = blueToothName
            }) {
                Text(text = "GET DEVICE BLUETOOTH NAME")
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                        val updatedDevices = if (device.mDeviceName == "META") {
                            devices + listOf(device)
                        } else {
                            devices
                        }
                        devices = updatedDevices
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
                    println("??? Button Get firmware version")
                    Timber.d("??? Button Get firmware version")
                    sdkManager.getFirmwareVersion()
                },
                enabled = isConnected
            ) {
                Text(text = "Get firmware version")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            var counterGetPuffArray1 by remember { mutableStateOf(0) }
            var counterGetPuffArray2 by remember { mutableStateOf(0) }

            Column {
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (counterGetPuffArray1 > 10) {
                                counterGetPuffArray1-=10
                            }
                        },
                        enabled = isConnected && counterGetPuffArray2 > 10
                    ) {
                        Text(text = "-10")
                    }
                    Button(
                        onClick = {
                            if (counterGetPuffArray1 > 1) {
                                counterGetPuffArray1--
                            }
                        },
                        enabled = isConnected && counterGetPuffArray1 > 1
                    ) {
                        Text(text = "-")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "from $counterGetPuffArray1",
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { counterGetPuffArray1++ },
                        enabled = isConnected
                    ) {
                        Text(text = "+")
                    }
                    Button(
                        onClick = { counterGetPuffArray1+=10 },
                        enabled = isConnected
                    ) {
                        Text(text = "+10")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (counterGetPuffArray2 > 10) {
                                counterGetPuffArray2-=10
                            }
                        },
                        enabled = isConnected && counterGetPuffArray2 > 10
                    ) {
                        Text(text = "-10")
                    }

                    Button(
                        onClick = {
                            if (counterGetPuffArray2 > 1) {
                                counterGetPuffArray2--
                            }
                        },
                        enabled = isConnected && counterGetPuffArray2 > 1
                    ) {
                        Text(text = "-")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "to $counterGetPuffArray2"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { counterGetPuffArray2++ },
                        enabled = isConnected
                    ) {
                        Text(text = "+")
                    }
                    Button(
                        onClick = { counterGetPuffArray2+=10 },
                        enabled = isConnected
                    ) {
                        Text(text = "+10")
                    }
                }


                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button Get PuffArray counterGetPuffArray1 = $counterGetPuffArray1 counterGetPuffArray2 = $counterGetPuffArray2")
                        Timber.d("??? Button Get PuffArray")
                        sdkManager.getGetPuffArray(counterGetPuffArray1, counterGetPuffArray2)
                    },
                    enabled = isConnected
                ) {
                    Text(text = "Get PuffArray from $counterGetPuffArray1 to $counterGetPuffArray2")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getUserGoalData")
                    Timber.d("??? Button getUserGoalData")
                    sdkManager.getUserGoalData()
                },
                enabled = isConnected
            ) {
                Text(text = "GetUserGoalData")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button getCartridgeEncryptedInfo")
                    Timber.d("??? Button getCartridgeEncryptedInfo")
                    sdkManager.getCartridgeEncryptedInfo()
                },
                enabled = isConnected
            ) {
                Text(text = "getCartridgeEncryptedInfo")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button Set Time")
                    Timber.d("??? Set Time")
                    scope.launch {
                        sdkManager.setTime()
                    }
                },
                enabled = isConnected
            ) {
                Text(text = "Set Time")
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
            var counterChildLock by remember { mutableStateOf(1) }

            Spacer(modifier = Modifier.size(32.dp))
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

            Button(
                onClick = {
                    loading.value = true
                    println("??? Button setChildLockSetting OFF ")
                    Timber.d("??? Button setChildLockSetting OFF")
                    scope.launch {
                        sdkManager.setChildLockSetting(0, isOn = false)
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "setChildLockSetting OFF")
            }

            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (counterChildLock > 1) {
                                counterChildLock--
                            }
                        },
                        enabled = isConnected && counterChildLock > 1
                    ) {
                        Text(text = "-")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = counterChildLock.toString(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { counterChildLock++ },
                        enabled = isConnected
                    ) {
                        Text(text = "+")
                    }
                }

                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button setChildLockSetting ON")
                        Timber.d("??? Button setChildLockSetting ON")
                        scope.launch {
                            sdkManager.setChildLockSetting(counterChildLock, isOn = true)
                        }
                    },
                    enabled = isConnected,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Blue,
                        containerColor = Color.Green,
                    )
                ) {
                    Text(text = "setChildLockSetting ON $counterChildLock")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
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
            Spacer(modifier = Modifier.height(32.dp))

            var counterPuffControlPuffNumber by remember { mutableStateOf(1) }
            var counterPuffControlPuffTimeInMinutes by remember { mutableStateOf(1) }
            var counterAutoMode by remember { mutableStateOf(false) }
            var counterManualMode by remember { mutableStateOf(false) }

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

            Column {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            counterManualMode = false
                        },
                        enabled = isConnected
                    ) {
                        Text(text = "set false")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "manualMode = $counterManualMode",
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            counterManualMode = true
                        },
                        enabled = isConnected
                    ) {
                        Text(text = "set true")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            counterAutoMode = false
                        },
                        enabled = isConnected
                    ) {
                        Text(text = "set false")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "autoMode = $counterAutoMode",
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            counterAutoMode = true
                        },
                        enabled = isConnected
                    ) {
                        Text(text = "set true")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (counterPuffControlPuffTimeInMinutes > 1) {
                                counterPuffControlPuffTimeInMinutes--
                            }
                        },
                        enabled = isConnected && counterPuffControlPuffTimeInMinutes > 1
                    ) {
                        Text(text = "-")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "puffTimeInMinute = $counterPuffControlPuffTimeInMinutes",
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { counterPuffControlPuffTimeInMinutes++ },
                        enabled = isConnected
                    ) {
                        Text(text = "+")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (counterPuffControlPuffNumber > 1) {
                                counterPuffControlPuffNumber--
                            }
                        },
                        enabled = isConnected && counterPuffControlPuffNumber > 1
                    ) {
                        Text(text = "-")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "puffNumber = $counterPuffControlPuffNumber",
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { counterPuffControlPuffNumber++ },
                        enabled = isConnected
                    ) {
                        Text(text = "+")
                    }
                }
            }

            Button(
                onClick = {
                    loading.value = true
                    println("??? Button setPuffsControl autoMode = $counterAutoMode manualMode = $counterManualMode puffNumber = $counterPuffControlPuffNumber puffTimeInMinutes = $counterPuffControlPuffTimeInMinutes")
                    Timber.d("??? Button setPuffsControl autoMode = $counterAutoMode manualMode = $counterManualMode puffNumber = $counterPuffControlPuffNumber puffTimeInMinutes = $counterPuffControlPuffTimeInMinutes")
                    scope.launch {
                        sdkManager.setPuffControl(
                            autoMode = counterAutoMode,
                            manualMode = counterManualMode,
                            puffNumber = counterPuffControlPuffNumber,
                            puffTimeInMinutes = counterPuffControlPuffTimeInMinutes
                        )
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "setPuffsControl autoMode = $counterAutoMode manualMode = $counterManualMode  puffNumber = $counterPuffControlPuffNumber puffTimeInMinutes = $counterPuffControlPuffTimeInMinutes")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            var counterSessionModePuffNumber by remember { mutableStateOf(1) }
            var counterSessionModePuffTimeInMinutes by remember { mutableStateOf(1) }

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
                Text(text = "set sessionMod OFF")
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (counterSessionModePuffNumber > 1) {
                            counterSessionModePuffNumber--
                        }
                    },
                    enabled = isConnected && counterSessionModePuffNumber > 1
                ) {
                    Text(text = "-")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "puffNumber = $counterSessionModePuffNumber"
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { counterSessionModePuffNumber++ },
                    enabled = isConnected
                ) {
                    Text(text = "+")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (counterSessionModePuffTimeInMinutes > 1) {
                            counterSessionModePuffTimeInMinutes--
                        }
                    },
                    enabled = isConnected && counterSessionModePuffTimeInMinutes > 1
                ) {
                    Text(text = "-")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "PuffTimeInMinutes = $counterSessionModePuffTimeInMinutes"
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { counterSessionModePuffTimeInMinutes++ },
                    enabled = isConnected
                ) {
                    Text(text = "+")
                }
                Spacer(modifier = Modifier.height(4.dp))

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    loading.value = true
                    println("??? Button setSessionMod ON puffNumber = $counterSessionModePuffNumber puffTimeInMinutes = $counterSessionModePuffTimeInMinutes")
                    Timber.d("??? Button setSessionMod ON puffNumber = $counterSessionModePuffNumber puffTimeInMinutes = $counterSessionModePuffTimeInMinutes")
                    scope.launch {
                        sdkManager.setSessionMode(
                            sessionMode = true,
                            puffNumber = counterSessionModePuffNumber,
                            puffTimeInMinutes = counterSessionModePuffTimeInMinutes
                        )
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "setSessionMod ON puffNumber = $counterSessionModePuffNumber puffTimeInMinutes = $counterSessionModePuffTimeInMinutes")
            }

            Spacer(modifier = Modifier.height(32.dp))
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
            Spacer(modifier = Modifier.height(32.dp))

            var counterPower by remember { mutableStateOf(10) }

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

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (counterPower > 1) {
                            counterPower -= 10
                        }
                    },
                    enabled = isConnected && counterPower > 1
                ) {
                    Text(text = "-")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = counterPower.toString(),
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { counterPower += 10 },
                    enabled = isConnected
                ) {
                    Text(text = "+")
                }
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "BE CAREFUL!!! DONT SET VALUE LESS THEN 10 AND MORE THEN 25",
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    loading.value = true
                    println("??? Button set power")
                    Timber.d("??? Button set power")
                    scope.launch {
                        sdkManager.setPower(value = null, mode = 0)
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "SetPower soft")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    loading.value = true
                    println("??? Button set power")
                    Timber.d("??? Button set power")
                    scope.launch {
                        sdkManager.setPower(counterPower, 1)
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "SetPower normal $counterPower")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    loading.value = true
                    println("??? Button set power")
                    Timber.d("??? Button set power")
                    scope.launch {
                        sdkManager.setPower(value = null, mode = 2)
                    }
                },
                enabled = isConnected,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Blue,
                    containerColor = Color.Green,
                )
            ) {
                Text(text = "SetPower boost")
            }

            Spacer(modifier = Modifier.height(32.dp))
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
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button unbind")
                    Timber.d("??? Button unbind")
                    sdkManager.unbind()
                },
                enabled = isConnected
            ) {
                Text(text = "unbind")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            var tiltedReminder by remember { mutableStateOf(false) }
            var noSmokingReminder by remember { mutableStateOf(false) }

            Column {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            tiltedReminder = false
                        },
                        modifier = Modifier.weight(1f),
                        enabled = isConnected
                    ) {
                        Text(text = "set false")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "tiltedReminder = $tiltedReminder",
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            tiltedReminder = true
                        },
                        enabled = isConnected,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "set true")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            noSmokingReminder = false
                        },
                        enabled = isConnected,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "set false")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "noSmokingReminder=$noSmokingReminder",
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            noSmokingReminder = true
                        },
                        enabled = isConnected,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "set true")
                    }
                }


                Button(
                    onClick = {
                        loading.value = true
                        println("??? Button setReminder")
                        Timber.d("??? Button setReminder")
                        scope.launch {
                            sdkManager.setTiltedReminderAndSetTiltedNoSmokingReminder(
                                tiltedReminder,
                                noSmokingReminder
                            )
                        }
                    },
                    enabled = isConnected
                ) {
                    Text(text = "setReminder tiltedReminder=$tiltedReminder noSmokingReminder=$noSmokingReminder")
                }

                Spacer(modifier = Modifier.height(32.dp))

            }
        }


        item {
            Button(
                onClick = {
                    loading.value = true
                    println("??? Button disConnect")
                    Timber.d("??? Button disConnect")
                    sdkManager.disConnect()
                    scope.launch {
                        delay(3000)
                        sdkManager.autoConnect()
                    }
                },
                enabled = isConnected
            ) {
                Text(text = "disConnect")
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
                            sdkManager.connect(devices[device],
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
            if (devices.isNotEmpty()) {
                Text(text = "Сверху кликабельный мак-адрес- начнется подключение")
            }
        }

    }
}
