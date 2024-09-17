package com.example.testsdkble

data class DeviceSettings(
    val btA2dpHfpStatus: BtA2dpHfpStatus? = null,
    val childLockSetting: CigarettesSetChildLock? = null,
    val powerSetting: CigarettesGetPowerSettingReplayData? = null,
    val overPuff: CigarettesGetOverPuffSettingReplayData? = null,
    val deviceInfo: CigarettesDeviceInfo? = null,
    val puffsControl: CigarettesPuffsControl? = null,
    val sessionMode: CigarettesSessionMode? = null,
    val consciousShield: CigarettesConsciousShield? = null,
    val friendMode: CigarettesFriendMode? = null,
    val metaAI: CigarettesMetaAI? = null,
    val battery: CigarettesBattery? = null,
    val screen: CigarettesScreen? = null,
    val volume: CigarettesVolume? = null,
    val language: CigarettesLanguage? = null,
    val nightMode: CigarettesNightMode? = null,
    val puffTotalNumber: CigarettesPuffTotalNumber? = null,
    val time: CigarettesSetTheTimeTheDevice? = null
)


data class BtA2dpHfpStatus(
    val connected: Int = 0, // Подключен ли Bluetooth
)

data class CigarettesSetChildLock(
    val lockTime: Int = 0 // Время блокировки, вероятно, в минутах или секундах
)

data class CigarettesGetPowerSettingReplayData(
    val power: Int = 0 // Мощность устройства, измеряемая в ваттах
)

data class CigarettesGetOverPuffSettingReplayData(
    val enable: Boolean = false, // Включен ли контроль максимальных затяжек
    val overPuffTimeout: Int = 0, // Тайм-аут по времени для затяжки
    val puffCount: Int = 0 // Максимальное количество затяжек
)

data class CigarettesDeviceInfo(
    val batteryLevel: Int = 0, // Уровень заряда батареи
    val cartridgeId: Int = 0, // Идентификатор картриджа
    val cartridgeResistance: Int = 0, // Сопротивление картриджа
    val chargeState: Int = 0, // Состояние зарядки: 0 — не заряжается, 1 — заряжается
    val macAddress: List<Int> = listOf(), // MAC-адрес устройства
    val rssi: Int = 0, // Уровень сигнала
    val puffStartVoltage: Int = 0, // Напряжение при начале затяжки
    val puffEndVoltage: Int = 0 // Напряжение при завершении затяжки
)

data class CigarettesPuffsControl(
    val autoModeOff: Boolean = false, // Автоматический режим выключен
    val manualModeOn: Boolean = false, // Включен ручной режим
    val puffTimeInMinutes: Int = 0, // Время затяжки в минутах
    val puffNumber: Int = 0 // Количество затяжек
)

data class CigarettesSessionMode(
    val sessionMode: Boolean = false, // Режим сессии: false — выключен, true — включен
    val puffTimeInMinutes: Int = 0, // Время затяжки в минутах
    val puffNumber: Int = 0 // Количество затяжек
)

data class CigarettesConsciousShield(
    val consciousShield: Boolean = false // Включен ли защитный режим сознания
)

data class CigarettesFriendMode(
    val friendMode: Boolean = false // Включен ли дружественный режим
)

data class CigarettesMetaAI(
    val nicotineLevel: Boolean = false, // Уровень никотина
    val puffs: Boolean = false, // Количество затяжек
    val dataCollection: Boolean = false // Включен ли сбор данных
)

data class CigarettesBattery(
    val showOn: Boolean = false // Показывать ли уровень заряда
)

data class CigarettesScreen(
    val brightness: Int = 0, // Яркость экрана в процентах
    val autoLockTime: Int = 0 // Время блокировки экрана
)

data class CigarettesVolume(
    val volume: Int = 0 // Громкость устройства
)

data class CigarettesLanguage(
    val language: Int = 0 // Идентификатор языка: 1 — английский, 2 — испанский, 3 — русский
)

data class CigarettesNightMode(
    val nightMode: Boolean = false // Включен ли ночной режим
)

data class CigarettesPuffTotalNumber(
    val puffTotalNumber: Int = 0 // Общее количество затяжек
)

data class CigarettesSetTheTimeTheDevice(
    val deviceTime: Int = 0 // Время, установленное на устройстве, в Unix формате
)

