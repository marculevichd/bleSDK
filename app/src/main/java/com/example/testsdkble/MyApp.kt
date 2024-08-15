package com.example.testsdkble

import android.app.Application
import android.os.Build
import android.util.Log
import com.ido.ble.BLEManager
import com.ido.ble.InitParam
import com.realsil.sdk.bbpro.core.BuildConfig
import com.willowtreeapps.hyperion.core.Hyperion
import timber.log.Timber

public class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Логирование поддерживаемых архитектур процессора устройства
        val abis = Build.SUPPORTED_ABIS
        abis.forEach { abi ->
            Log.d("Device ABI", abi)
        }



        if (BuildConfig.DEBUG) {
            Log.d("MyApp", "BuildConfig.DEBUG")
            Timber.plant(Timber.DebugTree())
        }

        // Инициализация BLEManager
        BLEManager.onApplicationCreate(this)

        // Параметры инициализации
        val param = InitParam().apply {
            isSaveDeviceDataToDB = false
            isNeedSoLibAutoSyncConfigIfReboot = false
            isEncryptedSPData = true
        }

        BLEManager.init(param)

        Log.d("MyApp", "BLEManager initialized")

    }
}
