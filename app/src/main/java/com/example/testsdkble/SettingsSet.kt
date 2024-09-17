package com.example.testsdkble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun SettingsScreen() {
    var loading = remember { mutableStateOf(false) }
    var resultMeth =
        remember { mutableStateOf("здесь будут результаты методов (кроме найденых устройств)") }
    val sdkManager = SDKManager(loading = loading, resultMeth = resultMeth)

    val coroutineScope = rememberCoroutineScope()
    var settings by remember { mutableStateOf<DeviceSettings?>(null) }

    // Интервал обновления настроек (например, 30 секунд)
    val refreshIntervalMillis = 30_000L
    var loadingTEST by remember { mutableStateOf(true) }


    // Периодическое обновление данных
    LaunchedEffect(Unit) {
        while (true) {
            try {
                // Получаем настройки устройства
                val deviceSettings = sdkManager.getDeviceSettings()
                Timber.d("??? new deviceSettings = $deviceSettings")
                settings = deviceSettings
                loadingTEST= false
            } catch (e: Exception) {
                println("??? e: $e")
                Timber.d("??? deviceSettings e: $e")
                loadingTEST= false
            }
            // Ждем перед следующим обновлением
            delay(refreshIntervalMillis)
        }
    }

    if (loadingTEST) {
        Text(text = "Загрузка")
//        CircularProgressIndicator()
//         Показываем индикатор загрузки, пока данные не пришли
    }else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                SettingsSetToggleBlock(
                    name = "ConsciousShield",
                    bool = settings?.consciousShield?.consciousShield ?: false,
                    toogle = { it ->
                        coroutineScope.launch {
                            sdkManager.setShield(it)
                        }
                    }
                )
            }

            item {
                SettingsSetToggleBlock(
                    name = "GuestMode",
                    bool = settings?.friendMode?.friendMode ?: false,
                    toogle = { it ->
                        coroutineScope.launch {
                            sdkManager.setGuestMode(it)
                        }
                    }
                )
            }

            item {
                SettingsSetToggleBlock(
                    name = "NightMode",
                    bool = settings?.nightMode?.nightMode ?: false,
                    toogle = { it ->
                        coroutineScope.launch {
                            sdkManager.setNightMode(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsSetToggleBlock(name: String, bool: Boolean, toogle: (Boolean) -> Unit) {
    var isOn by remember { mutableStateOf(bool) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок с именем блока
        Text(
            text = name,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        // Контейнер с тоглом
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Текстовое описание тогла
            Text(
                text = if (isOn) "Включено" else "Выключено",
                fontSize = 16.sp
            )

            // Тогл (Switch)
            Switch(
                checked = isOn,
                onCheckedChange = { isOn = it }
            )
        }
    }
}

@Composable
fun SettingsSetToggleWithCounterBlock(name: String) {
    var isOn by remember { mutableStateOf(false) }
    var counter by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок с именем блока
        Text(
            text = name,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        // Контейнер с тоглом
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Текстовое описание тогла
            Text(
                text = if (isOn) "Включено" else "Выключено",
                fontSize = 16.sp
            )

            // Тогл (Switch)
            Switch(
                checked = isOn,
                onCheckedChange = { isOn = it }
            )
        }

        // Если тогл активен, отображаем блок с кнопками +1 / -1
        if (isOn) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка -1
                Button(
                    onClick = { counter-- },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "-1")
                }

                // Текстовое поле для отображения значения
                Text(
                    text = counter.toString(),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )

                // Кнопка +1
                Button(
                    onClick = { counter++ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "+1")
                }
            }
        }
    }
}