package com.example.testsdkble

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.MutableState
import com.ido.ble.BLEManager
import com.ido.ble.bluetooth.connect.ConnectFailedReason
import com.ido.ble.bluetooth.device.BLEDevice
import com.ido.ble.callback.BindCallBack
import com.ido.ble.callback.ConnectCallBack
import com.ido.ble.callback.GetDeviceParaCallBack
import com.ido.ble.callback.ScanCallBack
import com.ido.ble.protocol.model.BtA2dpHfpStatus
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
import com.ido.ble.protocol.model.CigarettesPuffTotalNumber
import com.ido.ble.protocol.model.CigarettesPuffsControl
import com.ido.ble.protocol.model.CigarettesScreen
import com.ido.ble.protocol.model.CigarettesSessionMode
import com.ido.ble.protocol.model.CigarettesSetChildLock
import com.ido.ble.protocol.model.CigarettesSetTheTimeTheDevice
import com.ido.ble.protocol.model.CigarettesVolume
import timber.log.Timber


class SDKManager(
    val loading : MutableState<Boolean>
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

    fun connectByAddress(
        BLEDevice: BLEDevice,
        onSuccess: () -> Unit,
        OnTrable: () -> Unit,
    ) {
        println("??? connectByAddress")
        Timber.d("??? connectByAddress")

        BLEManager.registerConnectCallBack(object : ConnectCallBack.ICallBack {

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

            override fun onDeviceInNotBindStatus(p0: String?) {
                println("??? connectByAddress onDeviceInNotBindStatus p0=$p0")
                Timber.d("??? connectByAddress onDeviceInNotBindStatus p0=$p0")
                OnTrable()
            }

            override fun onInitCompleted(p0: String?) {
                println("??? connectByAddress onInitCompleted p0=$p0")
                Timber.d("??? connectByAddress onInitCompleted p0=$p0")
            }
        })

        BLEManager.connect(BLEDevice)
        println("??? BLEManager.connect() called with device: ${BLEDevice.mDeviceAddress}")
        Timber.d("??? BLEManager.connect() called with device: ${BLEDevice.mDeviceAddress}")
    }

    fun bind(
        onSuccess: () -> Unit,
        OnTrable: () -> Unit,
    ) {
        println("??? bind")
        Timber.d("??? bind")
        BLEManager.registerBindCallBack(object : BindCallBack.ICallBack {
            override fun onSuccess() {
                println("??? bind onSuccess")
                Timber.d("??? bind onSuccess")
                onSuccess()
            }

            override fun onFailed(p0: BindCallBack.BindFailedError?) {
                println("??? bind onFailed p0=$p0")
                Timber.d("??? bind onFailed p0=$p0")
                OnTrable()
            }

            override fun onCancel() {
                println("??? bind onCancel")
                Timber.d("??? bind onCancel")
                OnTrable()
            }

            override fun onReject() {
                println("??? bind onReject")
                Timber.d("??? bind onReject")
                OnTrable()
            }

            override fun onNeedAuth(p0: Int) {
                println("??? bind onNeedAuth p0=$p0")
                Timber.d("??? bind onNeedAuth")
                OnTrable()
            }

        })
        BLEManager.bind()
    }

    fun registerGetDeviceParaCallBack() {
        println("??? registerGetDeviceParaCallBack")
        Timber.d("??? registerGetDeviceParaCallBack")

        val callBack: GetDeviceParaCallBack.ICallBack = object : GetDeviceParaCallBack.ICallBack {

            override fun onGetBtA2dpHfpStatus(p0: BtA2dpHfpStatus?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetBtA2dpHfpStatus p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetBtA2dpHfpStatus p0=$p0")
            }

            override fun onGetPuffArray(p0: CigarettesGetPuffArrayReplyData?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetPuffArray p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetPuffArray p0=$p0")
            }

            override fun onGetChildLockSetting(p0: CigarettesSetChildLock?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetChildLockSetting p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetChildLockSetting p0=$p0")
            }

            override fun onGetPowerSetting(p0: CigarettesGetPowerSettingReplayData?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetPowerSetting p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetPowerSetting p0=$p0")
            }

            override fun onGetOverSetting(p0: CigarettesGetOverPuffSettingReplayData?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetOverSetting p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetOverSetting p0=$p0")
            }

            override fun onGetDeviceInfo(cigarettesDeviceInfo: CigarettesDeviceInfo) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetDeviceInfo cigarettesDeviceInfo=$cigarettesDeviceInfo")
                Timber.d("??? registerGetDeviceParaCallBack onGetDeviceInfo cigarettesDeviceInfo=$cigarettesDeviceInfo")
            }

            override fun onGetPuffsControl(p0: CigarettesPuffsControl?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetPuffsControl p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetPuffsControl p0=$p0")
            }

            override fun onGetSessionMode(p0: CigarettesSessionMode?) {
                loading.value = false
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
                println("??? registerGetDeviceParaCallBack onGetMetaAI p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetMetaAI p0=$p0")
            }

            override fun onGetBattery(p0: CigarettesBattery?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetBattery p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetBattery p0=$p0")
            }

            override fun onGetScreen(p0: CigarettesScreen?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack p0=$p0")
            }

            override fun onGetVolume(p0: CigarettesVolume?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetVolume p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetVolume p0=$p0")
            }

            override fun onGetLanguage(p0: CigarettesLanguage?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetLanguage p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetLanguage p0=$p0")
            }

            override fun onGetNightMode(p0: CigarettesNightMode?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetNightMode p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetNightMode p0=$p0")
            }

            override fun onGetPuffTotalNumber(p0: CigarettesPuffTotalNumber?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetPuffTotalNumber p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetPuffTotalNumber p0=$p0")
            }

            override fun onGetTime(p0: CigarettesSetTheTimeTheDevice?) {
                loading.value = false
                println("??? registerGetDeviceParaCallBack onGetTime p0=$p0")
                Timber.d("??? registerGetDeviceParaCallBack onGetTime p0=$p0")
            }
        }
        BLEManager.registerGetDeviceParaCallBack(callBack)
    }

    fun getGetPuffArray(
    ) {
        println("??? getGetPuffArray")
        Timber.d("??? getGetPuffArray")
        val cigarettesGetPuffArray = CigarettesGetPuffArray()
        cigarettesGetPuffArray.last_puff_number = 1
        cigarettesGetPuffArray.first_puff_number = 2
//        BLEManager.getGetPuffArray(cigarettesGetPuffArray)
        BLEManager.getGetPuffArray()
    }

    fun getGetChildLockSetting(
    ) {
        println("??? getGetChildLockSetting")
        Timber.d("??? getGetChildLockSetting")
        BLEManager.getGetChildLockSetting()
    }

    fun getDeviceInfo(
    ) {
        println("??? getDeviceInfo")
        Timber.d("??? getDeviceInfo")
        BLEManager.getDeviceInfo()
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
        BLEManager.getPuffsControl()
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


//    BLEManager.getGetPuffArray(CigarettesGetPuffArray cigarettesGetPuffArray)
//    BLEManager.getGetChildLockSetting();
//    BLEManager.getDeviceInfo()
//    BLEManager.getPuffsControl()
//    BLEManager.getSessionMode()
//    BLEManager.getConsciousShield()
//    BLEManager.getFriendMode()
//    BLEManager.getMetaAi()
//    BLEManager.getByttery()
//    BLEManager.getScreen()
//    BLEManager.getVolume()
//    BLEManager.getLanguage()
//    BLEManager.getNightMode()
//    BLEManager.getPowerSetting()
//    BLEManager.getOverPuffSetting()
//    BLEManager.getPuffTotalNumber()


}