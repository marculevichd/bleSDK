//package com.example.testsdkble
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import timber.log.Timber
//
//@Composable
//fun SettingsScreen() {
//    var loading = remember { mutableStateOf(false) }
//    var resultMeth =
//        remember { mutableStateOf("здесь будут результаты методов (кроме найденых устройств)") }
//    val sdkManager = SDKManager(loading = loading, resultMeth = resultMeth)
//
//    val coroutineScope = rememberCoroutineScope()
//    var settings by remember { mutableStateOf<DeviceSettings?>(null) }
//
//    // Интервал обновления настроек (например, 30 секунд)
//    val refreshIntervalMillis = 30_000L
//    var loadingTEST by remember { mutableStateOf(true) }
//
//    // Периодическое обновление данных
//    LaunchedEffect(Unit) {
//        while (true) {
//            try {
//                // Получаем настройки устройства
//                val deviceSettings = sdkManager.getDeviceSettings()
//                Timber.d("??? new deviceSettings = $deviceSettings")
//                settings = deviceSettings
//                loadingTEST = false
//            } catch (e: Exception) {
//                println("??? e: $e")
//                Timber.d("??? deviceSettings e: $e")
//                loadingTEST = false
//            }
//            // Ждем перед следующим обновлением
//            delay(refreshIntervalMillis)
//        }
//    }
//
//    if (loadingTEST) {
////         Показываем индикатор загрузки, пока данные не пришли
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            CircularProgressIndicator(
//                modifier = Modifier.align(Alignment.Center)
//            )
//            Text(
//                color = Color.Black,
//                text = "Загрузка"
//            )
//        }
//    } else {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//
//            item {
//                SettingsSetCounterBlock(
//                    name = "EvaporatorPower",
//                    value = settings?.powerSetting?.power ?: 15,
//                    saveParam = { param1 ->
//                        coroutineScope.launch {
//                            sdkManager.setPower(
//                                value = param1
//                            )
//                        }
//                    },
//                )
//            }
//
//            item {
//                SettingsSetCounterBlock(
//                    name = "LockSetting",
//                    value = settings?.childLockSetting?.lockTime ?: 5,
//                    saveParam = { param1 ->
//                        coroutineScope.launch {
//                            sdkManager.setChildLockSetting(
//                                value = param1
//                            )
//                        }
//                    },
//                )
//            }
//
//            item {
//                SettingsSetToggleWithCounterBlock(
//                    name = "PuffsControl",
//                    bool = settings?.puffsControl?.manualModeOn ?: false,
//                    toggle = { it ->
//                        coroutineScope.launch {
//                            sdkManager.setPuffControl(
//                                autoMode = !it,
//                                manualMode = it,
//                                puffNumber = null,
//                                puffTimeInMinutes = null
//                            )
//                        }
//                    },
//                    saveParam = { param1, param2 ->
//                        coroutineScope.launch {
//                            sdkManager.setPuffControl(
//                                autoMode = false,
//                                manualMode = true,
//                                puffNumber = param1,
//                                puffTimeInMinutes = param2
//                            )
//                        }
//                    },
//                    isTwoRow = true,
//                    text1 = "Время",
//                    text2 = "Затяжки"
//                )
//            }
//
//            item {
//                SettingsSetToggleWithCounterBlock(
//                    name = "sessionMode",
//                    bool = settings?.sessionMode?.sessionMode ?: false,
//                    toggle = { it ->
//                        coroutineScope.launch {
//                            sdkManager.setSessionMode(it, null, null)
//                        }
//                    },
//                    saveParam = { param1, param2 ->
//                        coroutineScope.launch {
//                            sdkManager.setSessionMode(true, param1, param2)
//                        }
//                    },
//                    isTwoRow = true,
//                    text1 = "Время",
//                    text2 = "Затяжки"
//                )
//            }
//
//            item {
//                SettingsSetToggleBlock(
//                    name = "ConsciousShield",
//                    bool = settings?.consciousShield?.consciousShield ?: false,
//                    toggle = { it ->
//                        coroutineScope.launch {
//                            sdkManager.setShield(it)
//                        }
//                    }
//                )
//            }
//
//            item {
//                SettingsSetToggleBlock(
//                    name = "GuestMode",
//                    bool = settings?.friendMode?.friendMode ?: false,
//                    toggle = { it ->
//                        coroutineScope.launch {
//                            sdkManager.setGuestMode(it)
//                        }
//                    }
//                )
//            }
//
//            item {
//                SettingsSetToggleBlock(
//                    name = "NightMode",
//                    bool = settings?.nightMode?.nightMode ?: false,
//                    toggle = { it ->
//                        coroutineScope.launch {
//                            sdkManager.setNightMode(it)
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun SettingsSetToggleBlock(
//    name: String,
//    bool: Boolean,
//    toggle: (Boolean) -> Unit
//) {
//    var isOn by remember { mutableStateOf(bool) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Заголовок с именем блока
//        Text(
//            text = name,
//            fontSize = 20.sp,
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//        )
//
//        // Контейнер с тоглом
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Текстовое описание тогла
//            Text(
//                text = if (isOn) "Включено" else "Выключено",
//                fontSize = 16.sp
//            )
//
//            // Тогл (Switch)
//            Switch(
//                checked = isOn,
//                onCheckedChange = {
//                    isOn = it
//                    toggle(it)
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun SettingsSetToggleWithCounterBlock(
//    name: String,
//    bool: Boolean,
//    toggle: (Boolean) -> Unit,
//    saveParam: (Int, Int) -> Unit,
//    isTwoRow: Boolean,
//    text1: String,
//    text2: String?
//) {
//    var isOn by remember { mutableStateOf(bool) }
//    var counter by remember { mutableIntStateOf(0) }
//    var counter2 by remember { mutableIntStateOf(0) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Заголовок с именем блока
//        Text(
//            text = name,
//            fontSize = 20.sp,
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//        )
//
//        // Контейнер с тоглом
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Текстовое описание тогла
//            Text(
//                text = if (isOn) "Включено" else "Выключено",
//                fontSize = 16.sp
//            )
//
//            // Тогл (Switch)
//            Switch(
//                checked = isOn,
//                onCheckedChange = {
//                    isOn = it
//                    toggle(it)
//                }
//            )
//        }
//
//        // Если тогл активен, отображаем блок с кнопками +1 / -1
//        if (isOn) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp),
//            ) {
//                Text(text = text1)
//
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Кнопка -1
//                    Button(
//                        onClick = { counter-- },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text(text = "-1")
//                    }
//
//                    // Текстовое поле для отображения значения
//                    Text(
//                        text = counter.toString(),
//                        fontSize = 24.sp,
//                        modifier = Modifier
//                            .weight(1f),
//                        textAlign = TextAlign.Center
//                    )
//
//                    // Кнопка +1
//                    Button(
//                        onClick = { counter++ },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text(text = "+1")
//                    }
//                }
//
//                Spacer(Modifier.size(16.dp))
//
//                if (isTwoRow) {
//                    Text(text = text2 ?: "")
//
//                    // Кнопка -1
//                    Button(
//                        onClick = { counter2-- },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text(text = "-1")
//                    }
//
//                    // Текстовое поле для отображения значения
//                    Text(
//                        text = counter2.toString(),
//                        fontSize = 24.sp,
//                        modifier = Modifier
//                            .weight(1f),
//                        textAlign = TextAlign.Center
//                    )
//
//                    // Кнопка +1
//                    Button(
//                        onClick = { counter2++ },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text(text = "+1")
//                    }
//                }
//
//                Spacer(Modifier.size(16.dp))
//
//                Button(
//                    onClick = {
//                        saveParam(
//                            counter, counter2
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(text = "Сохранить")
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun SettingsSetCounterBlock(
//    name: String,
//    value: Int,
//    saveParam: (Int) -> Unit,
//) {
//    var counter by remember { mutableIntStateOf(value) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Заголовок с именем блока
//        Text(
//            text = name,
//            fontSize = 20.sp,
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Кнопка -1
//            Button(
//                onClick = { counter-- },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(text = "-1")
//            }
//
//            // Текстовое поле для отображения значения
//            Text(
//                text = counter.toString(),
//                fontSize = 24.sp,
//                modifier = Modifier
//                    .weight(1f),
//                textAlign = TextAlign.Center
//            )
//
//            // Кнопка +1
//            Button(
//                onClick = { counter++ },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(text = "+1")
//            }
//        }
//
//        Spacer(Modifier.size(16.dp))
//
//        Button(
//            onClick = {
//                saveParam(
//                    counter
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Сохранить")
//        }
//    }
//}
