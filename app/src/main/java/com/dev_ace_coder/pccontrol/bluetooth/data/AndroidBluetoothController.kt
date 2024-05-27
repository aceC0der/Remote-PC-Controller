package com.dev_ace_coder.pccontrol.bluetooth.data

//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothManager
//import android.content.Context
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import com.dev_ace_coder.pccontrol.bluetooth.BluetoothController
////import com.dev_ace_coder.pccontrol.bluetooth.BluetoothDevice
//import com.dev_ace_coder.pccontrol.bluetooth.BluetoothDeviceDomain
//import kotlinx.coroutines.flow.*
//
//@SuppressLint("MissingPermission")
//class AndroidBluetoothController(
//    private val context: Context
//) : BluetoothController {
//
//    private val bluetoothManager by lazy {
//        context.getSystemService(BluetoothManager::class.java)
//    }
//    private val bluetoothAdapter by lazy {
//        bluetoothManager?.adapter
//    }
//
//    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
//    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
//        get() = _scannedDevices.asStateFlow()
//
//    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
//    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
//        get() = _pairedDevices.asStateFlow()
//
//    init {
//        updatePairedDevices()
//    }
//
//    override fun startDiscovery() {
//        if(!hasPermission(android.Manifest.permission.BLUETOOTH_SCAN)){
//            return
//        }
//
//        context.registerReceiver(
//            foundDeviceReceiver,
//            IntentFilter(BluetoothDevice.ACTION_FOUND)
//        )
//        bluetoothAdapter?.startDiscovery()
//    }
//
//    private val foundDeviceReceiver = FoundDeviceReceiver {device ->
//        _scannedDevices.update { devices->
//            val newDevice = device.toBluetoothDeviceDomain()
//            if(newDevice in devices) devices else (devices + newDevice)
//        }
//    }
//
//    override fun stopDiscovery() {
//        if(!hasPermission(android.Manifest.permission.BLUETOOTH_CONNECT)){
//            return
//        }
//        bluetoothAdapter?.cancelDiscovery()
//    }
//
//    override fun startBluetoothServer(): Flow<ConnectionResult> {
//        TODO("Not yet implemented")
//    }
//
//    override fun connectToDevice(device: com.dev_ace_coder.pccontrol.bluetooth.BluetoothDevice): Flow<ConnectionResult> {
//        TODO("Not yet implemented")
//    }
//
//    override fun closeConnection() {
//        TODO("Not yet implemented")
//    }
//
//    override fun release() {
//        context.unregisterReceiver(foundDeviceReceiver)
//    }
//
//    private fun updatePairedDevices(){
//        if(!hasPermission(android.Manifest.permission.BLUETOOTH_CONNECT)){
//            return
//        }
//        bluetoothAdapter
//            ?.bondedDevices
//            ?.map { it.toBluetoothDeviceDomain() }
//            ?.also{ devices ->
//                _pairedDevices.update { devices }
//            }
//    }
//
//    private fun hasPermission(permission: String): Boolean{
//        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
//    }
//}


