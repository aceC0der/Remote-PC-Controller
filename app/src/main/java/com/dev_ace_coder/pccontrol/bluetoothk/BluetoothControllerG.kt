import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.dev_ace_coder.pccontrol.currentUser
import com.dev_ace_coder.pccontrol.makeToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

private const val REQUEST_ENABLE_BT = 1
private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

var isBluetoothEnabled_chk = false
var isDeviceConnected_chk = false
var connectionStatus_chk = "Disconnected"

@Composable
fun BluetoothSender(navController: NavController) {
//    val previousDestination = navController.previousBackStackEntry?.destination?.route

    var ssid by remember { mutableStateOf("") }
    var isBluetoothEnabled by remember { mutableStateOf(false) }
    var isDeviceConnected by remember { mutableStateOf(false) }
    var connectionStatus by remember { mutableStateOf("Disconnected") }
    var userToken by remember { mutableStateOf(currentUser.userID!!) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var wifiPassword by remember {
        mutableStateOf("")
    }
    val window = (LocalView.current.context as Activity).window

    // Set the status bar color to blue
    window.statusBarColor = Color(0xFF495E57).toArgb()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                if (!isBluetoothEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        (context as Activity).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                        isBluetoothEnabled = true
                        return@Button
                    }
                } else {
                    coroutineScope.launch {
                        connectToDevice(context, "HC-05")
                        isDeviceConnected = isDeviceConnected_chk
                        connectionStatus = connectionStatus_chk
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF495E57)
            ),
            enabled = !isDeviceConnected
        ) {
            Text(
                text = if (isBluetoothEnabled) "Connect to Device" else "Enable Bluetooth",
                color = Color(0xFFEDEFEE)
            )
        }
        OutlinedTextField(
            value = ssid,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            onValueChange = { ssid = it },
            placeholder = { Text("Enter the WiFi SSID")},
            label = { Text("SSID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = isDeviceConnected
        )
        OutlinedTextField(
            value = wifiPassword,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            onValueChange = { wifiPassword = it },
            placeholder = { Text("Enter the WiFi Password")},
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = isDeviceConnected
        )
        OutlinedTextField(
            value = userToken,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),2
            onValueChange = { wifiPassword = it },
            placeholder = { Text("Enter the User Token")},
            label = { Text("Token") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = false
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    val messageToSend = "$ssid#$wifiPassword#$userToken#"
                    sendStringOverBluetooth(messageToSend, coroutineScope, context)
                    ssid = ""
                    wifiPassword = ""
                }
            },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF495E57)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = isDeviceConnected && ssid.isNotBlank() && wifiPassword.isNotBlank() && userToken.isNotBlank()
        ) {
            Text(
                text = "Send",
                color = Color(0xFFEDEFEE)
            )
        }

        Text(
            text = connectionStatus,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

private lateinit var socket: BluetoothSocket

@SuppressLint("MissingPermission")
fun connectToDevice(context: Context, deviceName: String): BluetoothSocket? {
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    var bluetoothSocket: BluetoothSocket? = null
    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
    if (pairedDevices != null && pairedDevices.isNotEmpty()) {
        for (device: BluetoothDevice in pairedDevices) {
            Log.i("Device" , device.name)
            if (device.name == deviceName) {
                val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                try {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                    bluetoothSocket.connect()
                    socket = bluetoothSocket
                    isDeviceConnected_chk = true
                    connectionStatus_chk = "Connected"
                    makeToast(context, "Connected to ${device.name}")
                    return bluetoothSocket
                } catch (e: IOException) {
                    makeToast(context, "Failed to connect to ${device.name}")
                    e.printStackTrace()
                }
            }
        }
    }
    makeToast(context, "Could not find device named $deviceName")
    return null
}

suspend fun sendStringOverBluetooth(str: String, coroutineScope: CoroutineScope, context: Context) {
    withContext(Dispatchers.IO) {
        try {
            val outputStream = socket.outputStream
            outputStream.write(str.toByteArray())
//            makeToast(context, "Message Sent")
        } catch (e: IOException) {
            // Handle the exception
        }
//        finally {
//            coroutineScope.launch {
//                socket.close()
//                isDeviceConnected_chk = false
//                connectionStatus_chk = "Disconnected from device"
//            }
//        }
    }
}