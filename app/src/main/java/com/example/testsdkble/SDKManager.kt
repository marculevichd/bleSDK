package com.example.testsdkble

import android.os.Handler
import android.os.Looper
import com.ido.ble.BLEManager
import com.ido.ble.bluetooth.connect.ConnectFailedReason
import com.ido.ble.bluetooth.device.BLEDevice
import com.ido.ble.callback.ConnectCallBack
import com.ido.ble.callback.ScanCallBack
import timber.log.Timber

class SDKManager {


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

    fun getDeviceInfo() {
        println("??? getDeviceInfo")
        Timber.d("??? getDeviceInfo")

        BLEManager.getDeviceInfo()  // Вызов метода из BLEManager для получения информации об устройстве
    }

}



