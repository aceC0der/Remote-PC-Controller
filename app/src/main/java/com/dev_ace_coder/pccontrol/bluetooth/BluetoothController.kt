package com.dev_ace_coder.pccontrol.bluetooth

//import com.dev_ace_coder.pccontrol.data.ConnectionResult
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.StateFlow
//
//interface BluetoothController{
//    val scannedDevices: StateFlow<List<BluetoothDevice>>
//    val pairedDevices: StateFlow<List<BluetoothDevice>>
//
//    fun startDiscovery()
//    fun stopDiscovery()
//    fun startBluetoothServer(): Flow<ConnectionResult>
//
//    fun connectToDevice(device: BluetoothDevice): Flow<ConnectionResult>
//
//    fun closeConnection()
//
//    fun release()
//}