package com.example.testsdkble

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.runtime.MutableState
import com.ido.ble.BLEManager
import com.ido.ble.bluetooth.connect.ConnectFailedReason
import com.ido.ble.bluetooth.device.BLEDevice
import com.ido.ble.callback.CigarettesSetCallBack
import com.ido.ble.callback.ConnectCallBack
import com.ido.ble.callback.DeviceLogCallBack
import com.ido.ble.callback.GetDeviceParaCallBack
import com.ido.ble.callback.GetPuffArrayCallBack
import com.ido.ble.callback.ScanCallBack
import com.ido.ble.callback.UnbindCallBack
import com.ido.ble.firmware.log.flash.ICollectFlashLogListener
import com.ido.ble.protocol.model.BtA2dpHfpStatus
import com.ido.ble.protocol.model.CartridgeEncryptedInfo
import com.ido.ble.protocol.model.CigarettesBattery
import com.ido.ble.protocol.model.CigarettesConsciousShield
import com.ido.ble.protocol.model.CigarettesDeviceInfo
import com.ido.ble.protocol.model.CigarettesFriendMode
import com.ido.ble.protocol.model.CigarettesGetOverPuffSettingReplayData
import com.ido.ble.protocol.model.CigarettesGetPowerSettingReplayData
import com.ido.ble.protocol.model.CigarettesGetPuffArray
import com.ido.ble.protocol.model.CigarettesGetPuffArrayReplyData
import com.ido.ble.protocol.model.CigarettesLanguage
import com.ido.ble.protocol.model.CigarettesMetaAI
import com.ido.ble.protocol.model.CigarettesNightMode
import com.ido.ble.protocol.model.CigarettesPowerSettings
import com.ido.ble.protocol.model.CigarettesPuffTotalNumber
import com.ido.ble.protocol.model.CigarettesPuffsControl
import com.ido.ble.protocol.model.CigarettesScreen
import com.ido.ble.protocol.model.CigarettesSessionMode
import com.ido.ble.protocol.model.CigarettesSetChildLock
import com.ido.ble.protocol.model.CigarettesSetTheTimeTheDevice
import com.ido.ble.protocol.model.CigarettesVolume
import com.ido.ble.protocol.model.FirmwareVersion
import com.ido.ble.protocol.model.UserGoalData
import timber.log.Timber
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random


class SDKManager(
    val loading: MutableState<Boolean>,
    val resultMeth: MutableState<String>,
    val context: Context
) {

    fun startScan(
        onFind: (BLEDevice?) -> Unit,
    ) {
        println("??? startScan")
        Timber.d("??? startScan")
        BLEManager.registerScanCallBack(object : ScanCallBack.ICallBack {
            override fun onStart() {
                println("??? startScan onStart")
                Timber.d("??? startScan onStart")
            }

            override fun onFindDevice(p0: BLEDevice?) {
                println("??? startScan onFindDevice p0 = $p0")
                Timber.d("??? startScan onFindDevice p0 = $p0")
                onFind(p0)
            }

            override fun onScanFinished() {
                println("??? startScan onScanFinished")
                Timber.d("??? startScan onScanFinished")
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            println("??? BLEManager.startScanDevices() called")
            Timber.d("??? BLEManager.startScanDevices() called")
            BLEManager.startScanDevices()
        }, 1000) // Задержка в 1 секунду
    }

    private var connectCallback: ConnectCallBack.ICallBack? = null

    fun connect(
        BLEDevice: BLEDevice,
        onSuccess: () -> Unit,
        OnTrable: () -> Unit,
    ) {
        println("??? connectByAddress")
        Timber.d("??? connectByAddress")
        connectCallback = object : ConnectCallBack.ICallBack {

            override fun onConnectStart(p0: String?) {
                println("??? connectByAddress onConnectStart p0=$p0")
                Timber.d("??? connectByAddress onConnectStart p0=$p0")
            }

            override fun onConnecting(p0: String?) {
                println("??? connectByAddress onConnecting p0=$p0")
                Timber.d("??? connectByAddress onConnecting p0=$p0")
            }

            override fun onRetry(p0: Int, p1: String?) {
                println("??? connectByAddress onRetry p0=$p0, p1=$p1")
                Timber.d("??? connectByAddress onRetry p0=$p0, p1=$p1")
            }

            override fun onConnectSuccess(p0: String?) {
                println("??? connectByAddress onConnectSuccess p0=$p0")
                Timber.d("??? connectByAddress onConnectSuccess p0=$p0")
                onSuccess()
            }

            override fun onConnectFailed(p0: ConnectFailedReason?, p1: String?) {
                println("??? connectByAddress onConnectFailed ConnectFailedReason = $p0 p1 =$p1")
                Timber.d("??? connectByAddress onConnectFailed ConnectFailedReason = $p0 p1 =$p1")
                OnTrable()
            }

            override fun onConnectBreak(p0: String?) {
                println("??? connectByAddress onConnectBreak p0=$p0")
                Timber.d("??? connectByAddress onConnectBreak p0=$p0")
                OnTrable()
            }

            override fun onInDfuMode(p0: BLEDevice?) {
                println("??? connectByAddress onInDfuMode p0=$p0")
                Timber.d("??? connectByAddress onInDfuMode p0=$p0")
            }
        }
        BLEManager.registerConnectCallBack(connectCallback)
        val randomInt = Random.nextInt(1, 9999)
        val deviceName = Settings.Global.getString(context.contentResolver, "device_name")

        BLEManager.connect(BLEDevice, "$randomInt", deviceName)
        Timber.d("??? BLEManager.connect() params BLEManager.connect${BLEDevice} ${randomInt} ${deviceName}")
        println("??? BLEManager.connect() called with device: ${BLEDevice.mDeviceAddress}")
        Timber.d("??? BLEManager.connect() called with device: ${BLEDevice.mDeviceAddress}")
    }

    fun registerGetDeviceParaCallBack() {
        println("??? registerGetDeviceParaCallBack")
        Timber.d("??? registerGetDeviceParaCallBack")

        val callBack: GetDeviceParaCallBack.ICallBack = object : GetDeviceParaCallBack.ICallBack {

            override fun onGetBtA2dpHfpStatus(p0: BtA2dpHfpStatus?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetBtA2dpHfpStatus p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetBtA2dpHfpStatus p0=$p0")
            }

            override fun onGetChildLockSetting(p0: CigarettesSetChildLock?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetChildLockSetting p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetChildLockSetting p0=$p0")
            }

            override fun onGetPowerSetting(p0: CigarettesGetPowerSettingReplayData?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetPowerSetting p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetPowerSetting p0=$p0")
            }

            override fun onGetOverSetting(p0: CigarettesGetOverPuffSettingReplayData?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetOverSetting p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetOverSetting p0=$p0")
            }

            override fun onGetDeviceInfo(cigarettesDeviceInfo: CigarettesDeviceInfo) {
                loading.value = false
                resultMeth.value = cigarettesDeviceInfo.toString()
                println("??? registerGetDeviceParaCallBack onGetDeviceInfo cigarettesDeviceInfo=$cigarettesDeviceInfo")
                Timber.d("??? registerGetDeviceParaCallBack onGetDeviceInfo cigarettesDeviceInfo=$cigarettesDeviceInfo")
            }

            override fun onGetPuffsControl(p0: CigarettesPuffsControl?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetPuffsControl p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetPuffsControl p0=$p0")
            }

            override fun onGetSessionMode(p0: CigarettesSessionMode?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetSessionMode p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetSessionMode p0=$p0")
            }

            override fun onGetConsciousShield(p0: CigarettesConsciousShield?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetConsciousShield p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetConsciousShield p0=$p0")
            }

            override fun onGetFriendMode(p0: CigarettesFriendMode?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetFriendMode p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetFriendMode p0=$p0")
            }

            override fun onGetMetaAI(p0: CigarettesMetaAI?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetMetaAI p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetMetaAI p0=$p0")
            }

            override fun onGetBattery(p0: CigarettesBattery?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetBattery p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetBattery p0=$p0")
            }

            override fun onGetScreen(p0: CigarettesScreen?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack p0=$p0")
            }

            override fun onGetVolume(p0: CigarettesVolume?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetVolume p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetVolume p0=$p0")
            }

            override fun onGetLanguage(p0: CigarettesLanguage?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetLanguage p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetLanguage p0=$p0")
            }

            override fun onGetNightMode(p0: CigarettesNightMode?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetNightMode p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetNightMode p0=$p0")
            }

            override fun onGetPuffTotalNumber(p0: CigarettesPuffTotalNumber?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetPuffTotalNumber p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetPuffTotalNumber p0=$p0")
            }

            override fun onGetTime(p0: CigarettesSetTheTimeTheDevice?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetTime p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetTime p0=$p0")
            }

            override fun onGetUserGoalData(p0: UserGoalData?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetUserGoalData p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetUserGoalData p0=$p0")
            }

            override fun onGetFirmwareVersion(p0: FirmwareVersion?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetFirmwareVersion p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetFirmwareVersion p0=$p0")
            }

            override fun onGetCartridgeEncryptedInfo(p0: CartridgeEncryptedInfo?) {
                loading.value = false
                resultMeth.value = p0.toString()
                println("??? registerGetDeviceParaCallBack onGetCartridgeEncryptedInfo p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetCartridgeEncryptedInfo p0=$p0")
            }
        }
        BLEManager.registerGetDeviceParaCallBack(callBack)
    }

    fun getGetPuffArray(
        valueFrom: Int,
        valueTo: Int
    ) {
        println("??? getGetPuffArray")
        Timber.d("??? getGetPuffArray")
        val callBack = object : GetPuffArrayCallBack.ICallBack {
            override fun onProgress(p0: Int) {
                resultMeth.value = p0.toString()
                println("??? getGetPuffArray callback onProgress p0=$p0")
                Timber.d("??? getGetPuffArray callback onProgress p0=$p0")
            }

            override fun onSucceed(p0: CigarettesGetPuffArrayReplyData?) {
                resultMeth.value = p0.toString()
                println("??? getGetPuffArray callback onSucceed p0=$p0")
                println("??? getGetPuffArray callback onSucceed p0?.puff_counter=${p0?.puff_counter}")
                println("??? getGetPuffArray callback onSucceed p0?.puff_array.size=${p0?.puff_array?.size}")
                println("??? getGetPuffArray callback onSucceed p0?.progress=${p0?.progress}")
                Timber.d("??? getGetPuffArray callback onSucceed p0=$p0")
            }

            override fun onFailed(p0: Int) {
                resultMeth.value = p0.toString()
                println("??? getGetPuffArray callback onFailed p0=$p0")
                Timber.d("??? getGetPuffArray callback onFailed p0=$p0")
            }
        }
        BLEManager.registerGetPuffArrayCallBack(callBack)
        val cigarettesGetPuffArray = CigarettesGetPuffArray()
        cigarettesGetPuffArray.first_puff_number = valueFrom
        cigarettesGetPuffArray.last_puff_number = valueTo
        BLEManager.getGetPuffArray(cigarettesGetPuffArray)
    }

    fun getGetChildLockSetting(
    ) {
        println("??? getGetChildLockSetting")
        Timber.d("??? getGetChildLockSetting")
        BLEManager.getGetChildLockSetting()
    }

    fun getCartridgeEncryptedInfo(
    ) {
        println("??? getCartridgeEncryptedInfo")
        Timber.d("??? getCartridgeEncryptedInfo")
        BLEManager.getCartridgeEncryptedInfo()
    }

    fun unbind(
    ) {
        println("??? unbind")
        Timber.d("??? unbind")

        val unbindCallBack = object : UnbindCallBack.ICallBack {
            override fun onSuccess() {
                println("??? unbind unbindCallBack onSuccess")
                Timber.d("??? unbind unbindCallBack onSuccess")
            }

            override fun onFailed() {
                println("??? unbind unbindCallBack onFailed")
                Timber.d("??? unbind unbindCallBack onFailed")
            }
        }

        BLEManager.registerUnbindCallBack(unbindCallBack)
        BLEManager.unbind()

    }

    fun getDeviceInfo(
    ) {
        println("??? getDeviceInfo")
        Timber.d("??? getDeviceInfo")
        BLEManager.getDeviceInfo()
    }

    fun getUserGoalData(
    ) {
        println("??? getUserGoalData")
        Timber.d("??? getUserGoalData")
        BLEManager.getUserGoalData()
    }

    fun getPuffsControl(
    ) {
        println("??? getPuffsControl")
        Timber.d("??? getPuffsControl")
        BLEManager.getPuffsControl()
    }

    fun getSessionMode(
    ) {
        println("??? getSessionMode")
        Timber.d("??? getSessionMode")
        BLEManager.getSessionMode()
    }

    fun getConsciousShield(
    ) {
        println("??? getConsciousShield")
        Timber.d("??? getConsciousShield")
        BLEManager.getConsciousShield()
    }

    fun getFriendMode(
    ) {
        println("??? getFriendMode")
        Timber.d("??? getFriendMode")
        BLEManager.getFriendMode()
    }

    fun getMetaAi(
    ) {
        println("??? getMetaAi")
        Timber.d("??? getMetaAi")
        BLEManager.getMetaAi()
    }

    fun getByttery(
    ) {
        println("??? getByttery")
        Timber.d("??? getByttery")
        BLEManager.getByttery()
    }

    fun getScreen(
    ) {
        println("??? getScreen")
        Timber.d("??? getScreen")
        BLEManager.getScreen()
    }

    fun getVolume(
    ) {
        println("??? getVolume")
        Timber.d("??? getVolume")
        BLEManager.getVolume()
    }

    fun getLanguage(
    ) {
        println("??? getLanguage")
        Timber.d("??? getLanguage")
        BLEManager.getLanguage()
    }

    fun getNightMode(
    ) {
        println("??? getNightMode")
        Timber.d("??? getNightMode")
        BLEManager.getNightMode()
    }

    fun getPowerSetting(
    ) {
        println("??? getPowerSetting")
        Timber.d("??? getPowerSetting")
        BLEManager.getPowerSetting()
    }

    fun getOverPuffSetting(
    ) {
        println("??? getOverPuffSetting")
        Timber.d("??? getOverPuffSetting")
        BLEManager.getOverPuffSetting()
    }

    fun getPuffTotalNumber(
    ) {
        println("??? getPuffTotalNumber")
        Timber.d("??? getPuffTotalNumber")
        BLEManager.getPuffTotalNumber()
    }

    fun getFirmwareVersion(
    ) {
        println("??? getFirmwareVersion")
        Timber.d("??? getFirmwareVersion")
        BLEManager.getFirmwareVersion()
    }

    fun disConnect(
    ) {
        println("??? disConnect")
        Timber.d("??? disConnect")
        BLEManager.unregisterConnectCallBack(connectCallback)
        println("??? disConnect unregisterConnectCallBack")
        Timber.d("??? disConnect unregisterConnectCallBack")
        BLEManager.disConnect()
        BLEManager.autoConnect()
    }

    fun autoConnect() {
        Timber.d("??? autoConnect")
        println("??? autoConnect")
        BLEManager.autoConnect()
    }

    fun collectDeviceAllFlashLog(path: String) {
        println("??? collectDeviceAllFlashLog")
        Timber.d("??? collectDeviceAllFlashLog")

        val timeoutSecond = 30

        val listenerTest: ICollectFlashLogListener = object : ICollectFlashLogListener {
            override fun onStart() {
                println("??? collectDeviceAllFlashLog ICollectFlashLogListener onStart")
                Timber.d("??? collectDeviceAllFlashLog ICollectFlashLogListener onStart")
            }

            override fun onFinish() {
                println("??? collectDeviceAllFlashLog ICollectFlashLogListener onFinish")
                Timber.d("??? collectDeviceAllFlashLog ICollectFlashLogListener onFinish")

                val file = File(path)
                if (file.exists()) {
                    println("??? Лог-файл создан: ${file.absolutePath}")
                    Timber.d("??? Лог-файл создан: ${file.absolutePath}")
                } else {
                    println("??? Лог-файл не был создан")
                    Timber.d("??? Лог-файл не был создан")
                }
            }
        }

        val deviceLogCallBack = object : DeviceLogCallBack.ICallBack {
            override fun onGetHeatLog(p0: String?) {
                println("??? collectDeviceAllFlashLog deviceLogCallBack onGetHeatLog p0 = $p0")
                Timber.d("??? collectDeviceAllFlashLog deviceLogCallBack onGetHeatLog p0 = $p0")
            }
        }
        BLEManager.collectDeviceAllFlashLog(path, timeoutSecond, listenerTest)

        BLEManager.registerDeviceLogCallBack(deviceLogCallBack)
    }

    fun collectUserOperateAllFlashLog(path: String) {
        println("??? collectDeviceAllFlashLog")
        Timber.d("??? collectDeviceAllFlashLog")

        val timeoutSecond = 30

        val listenerTest: ICollectFlashLogListener = object : ICollectFlashLogListener {
            override fun onStart() {
                println("??? collectDeviceAllFlashLog ICollectFlashLogListener onStart")
                Timber.d("??? collectDeviceAllFlashLog ICollectFlashLogListener onStart")
            }

            override fun onFinish() {
                println("??? collectUserOperateAllFlashLog ICollectFlashLogListener onFinish")
                Timber.d("??? collectUserOperateAllFlashLog ICollectFlashLogListener onFinish")

                val file = File(path)
                if (file.exists()) {
                    println("??? System Log file created at path: ${file.absolutePath}")
                    Timber.d("??? System Log file created at path: ${file.absolutePath}")
                } else {
                    println("??? System Log file dont create")
                    Timber.d("??? System Log file dont create")
                }
            }
        }

        val deviceLogCallBack = object : DeviceLogCallBack.ICallBack {
            override fun onGetHeatLog(p0: String?) {
                println("??? collectUserOperateAllFlashLog deviceLogCallBack onGetHeatLog p0 = $p0")
                Timber.d("??? collectUserOperateAllFlashLog deviceLogCallBack onGetHeatLog p0 = $p0")
            }
        }
        BLEManager.collectUserOperateAllFlashLog(path, timeoutSecond, listenerTest)

        BLEManager.registerDeviceLogCallBack(deviceLogCallBack)
    }


    fun setCigarettesStartSearchDevice(
    ) {
        println("??? setCigarettesStartSearchDevice")
        Timber.d("??? setCigarettesStartSearchDevice")

        val cigarettescallback = object : CigarettesSetCallBack.ICallBack {
            override fun onSuccess(p0: CigarettesSetCallBack.CigarettesSettingType?, p1: Any?) {
                resultMeth.value = "p0 =" + p0.toString() + "p1 =" + p1.toString()
                println("??? setCigarettesStartSearchDevice onSuccess p0=$p0 p1=$p1")
                Timber.d("??? setCigarettesStartSearchDevice p0=$p0 p1=$p1")
            }

            override fun onFailed(p0: CigarettesSetCallBack.CigarettesSettingType?) {
                println("??? setCigarettesStartSearchDevice onFailed p0=$p0")
                Timber.d("??? setCigarettesStartSearchDevice onFailed p0=$p0")
            }
        }

        BLEManager.registerCigarettesCallBack(cigarettescallback)

        BLEManager.setCigarettesStartSearchDevice()
    }

    fun setCigarettesStopSearchDeivce() {
        println("??? setCigarettesStopSearchDeivce")
        Timber.d("??? setCigarettesStopSearchDeivce")
        BLEManager.setCigarettesStopSearchDeivce()
    }

    suspend fun setShield(
        bool: Boolean,
    ): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesConsciousShield()
            setting.conscious_shield = if (bool) 1 else 0

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true)) // Возвращаем результат
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Настройка setShield успешно изменена")
                    Timber.d("Настройка setShield успешно изменена")
                    println("Настройка успешно изменена Shield")
                    Timber.d("Настройка успешно изменена Shield")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Ошибка при изменении настройки setShield")
                    Timber.e("Ошибка при изменении настройки setShield")
                    println("Ошибка при изменении настройки Shield")
                    Timber.e("Ошибка при изменении настройки Shield")
                }
            }

            // Регистрируем колбек и вызываем метод SDK
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.settingConsciousShield(setting)
        }
    }

    suspend fun setGuestMode(
        bool: Boolean,
    ): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesFriendMode()
            setting.friend_mode = if (bool) 1 else 0

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true)) // Возвращаем результат
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Настройка успешно изменена GuestMode")
                    Timber.d("Настройка успешно изменена GuestMode")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Ошибка при изменении настройки GuestMode")
                    Timber.e("Ошибка при изменении настройки GuestMode")
                }
            }

            // Регистрируем колбек и вызываем метод SDK
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.settingFriendMode(setting)
        }
    }

    suspend fun setNightMode(
        bool: Boolean,
    ): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesNightMode()
            setting.night_mode = if (bool) 1 else 0

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true)) // Возвращаем результат
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Настройка успешно изменена NightMode")
                    Timber.d("Настройка успешно изменена NightMode")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Ошибка при изменении настройки NightMode")
                    Timber.e("Ошибка при изменении настройки NightMode")
                }
            }

            // Регистрируем колбек и вызываем метод SDK
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.settingNightMode(setting)
        }
    }

    suspend fun setTiltedReminderAndSetTiltedNoSmokingReminder(
        tiltedReminder: Boolean,
        noSmokingReminder: Boolean,
    ): Result<Boolean> {
        return suspendCoroutine { continuation ->

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(p0: CigarettesSetCallBack.CigarettesSettingType?, p1: Any?) {
                    if (p0 == CigarettesSetCallBack.CigarettesSettingType.SETTINGS_POSTURE_REMINDER_SWITCH) {
                        continuation.resume(Result.success(true))
                        BLEManager.unregisterCigarettesCallBack(this)
                        println("Настройка успешно изменена setTiltedReminderAndSetTiltedNoSmokingReminder")
                        Timber.d("Настройка успешно изменена setTiltedReminderAndSetTiltedNoSmokingReminder")
                    }
                }

                override fun onFailed(p0: CigarettesSetCallBack.CigarettesSettingType?) {
                    if (p0 == CigarettesSetCallBack.CigarettesSettingType.SETTINGS_POSTURE_REMINDER_SWITCH) {
                        continuation.resume(Result.failure(Exception("Failed to set setting")))
                        BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                        println("Ошибка при изменении настройки setTiltedReminderAndSetTiltedNoSmokingReminder")
                        Timber.e("Ошибка при изменении настройки setTiltedReminderAndSetTiltedNoSmokingReminder")
                    }
                }
            }

            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.setTiltedReminder(tiltedReminder)
            BLEManager.setTiltedNoSmokingReminder(noSmokingReminder)
        }
    }

    suspend fun setChildLockSetting(value: Int?, isOn: Boolean): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesSetChildLock()
            if (isOn) { //turn on
                setting.lock_time = value ?: 0
                setting.lock_state = CigarettesSetChildLock.LOCK
            } else { //turn off
                setting.lock_state = CigarettesSetChildLock.UNLOCK
            }

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true)) // Возвращаем результат
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Настройка setChildLockSetting успешно изменена")
                    Timber.d("Настройка setChildLockSetting успешно изменена")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Ошибка при изменении настройки setChildLockSetting")
                    Timber.e("Ошибка при изменении настройки setChildLockSetting")
                }
            }

            // Регистрируем колбек и вызываем метод SDK
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.setCigarettesSetChildLockSetting(setting)
        }
    }

    suspend fun setPower(value: Int?, mode: Int): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesPowerSettings()
            setting.power = value ?: 1
            setting.power_mode = when (mode) {
                0 -> {
                    CigarettesPowerSettings.POWER_MODE_SOFT
                }

                1 -> {
                    CigarettesPowerSettings.POWER_MODE_NORMAL
                }

                else -> {
                    CigarettesPowerSettings.POWER_MODE_BOOST
                }
            }

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true)) // Возвращаем результат
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Настройка setPower успешно изменена")
                    Timber.d("Настройка setPower успешно изменена")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Ошибка при изменении настройки setPower")
                    Timber.e("Ошибка при изменении настройки setPower")
                }
            }

            // Регистрируем колбек и вызываем метод SDK
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.setCigarettesPowerSetting(setting)
        }
    }

    suspend fun setSessionMode(
        sessionMode: Boolean,
        puffNumber: Int?,
        puffTimeInMinutes: Int?
    ): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesSessionMode()
            setting.session_mode = if (sessionMode) 1 else 0
            setting.puff_number = puffNumber ?: 0
            setting.puff_time_in_minutes = puffTimeInMinutes ?: 0

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true)) // Возвращаем результат
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Настройка setSessionMode успешно изменена")
                    Timber.d("Настройка setSessionMode успешно изменена")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Ошибка при изменении настройки setSessionMode")
                    Timber.e("Ошибка при изменении настройки setSessionMode")
                }
            }

            // Регистрируем колбек и вызываем метод SDK
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.settingSessionMode(setting)
        }
    }

    suspend fun setPuffControl(
        autoMode: Boolean,
        manualMode: Boolean,
        puffNumber: Int?,
        puffTimeInMinutes: Int?
    ): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesPuffsControl()
            setting.auto_mode_off = if (autoMode) 1 else 0
            setting.manual_mode_on = if (manualMode) 1 else 0
            setting.puff_number = puffNumber ?: 0
            setting.puff_time_in_minutes = puffTimeInMinutes ?: 0

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true)) // Возвращаем результат
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Настройка setPuffControl успешно изменена")
                    Timber.d("Настройка setPuffControl успешно изменена")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this) // Убираем колбек
                    println("Ошибка при изменении настройки setPuffControl")
                    Timber.e("Ошибка при изменении настройки setPuffControl")
                }
            }

            // Регистрируем колбек и вызываем метод SDK
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.settingPuffsContro(setting)
        }
    }

    suspend fun setTime(): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val setting = CigarettesSetTheTimeTheDevice()

            val currentTime = System.currentTimeMillis() / 1000
            val zoneId = ZoneId.systemDefault()
            val currentOffsetInHours = ZonedDateTime.now(zoneId).offset.totalSeconds / 3600.0

            val humanReadableTime = Instant.ofEpochSecond(currentTime)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            Timber.d("Unix= $currentTime, в норм формете= $humanReadableTime")
            println("Unix= $currentTime, в норм формете= $humanReadableTime")
            Timber.d("zoneId= $zoneId,  в double формате= $currentOffsetInHours")
            println("zoneId= $zoneId,  в double формате= $currentOffsetInHours")

            val setCallBack = object : CigarettesSetCallBack.ICallBack {
                override fun onSuccess(
                    type: CigarettesSetCallBack.CigarettesSettingType?,
                    data: Any?
                ) {
                    continuation.resume(Result.success(true))
                    BLEManager.unregisterCigarettesCallBack(this)
                    println("Настройка setTime успешно изменена")
                    Timber.d("Настройка setTime успешно изменена")
                }

                override fun onFailed(type: CigarettesSetCallBack.CigarettesSettingType?) {
                    continuation.resume(Result.failure(Exception("Failed to set setting")))
                    BLEManager.unregisterCigarettesCallBack(this)
                    println("Ошибка при изменении настройки setTime")
                    Timber.e("Ошибка при изменении настройки setTime")
                }
            }

            setting.unix_time = currentTime
            setting.time_zone = currentOffsetInHours.toFloat()
            BLEManager.registerCigarettesCallBack(setCallBack)
            BLEManager.settingDeviceTime(setting)
        }
    }

}