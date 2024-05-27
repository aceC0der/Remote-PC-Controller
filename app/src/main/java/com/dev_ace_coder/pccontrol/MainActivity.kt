package com.dev_ace_coder.pccontrol

import BluetoothSender
import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
//import com.dev_ace_coder.pccontrol.presentation.BluetoothViewModel
//import com.dev_ace_coder.pccontrol.presentation.DeviceScreen
import com.dev_ace_coder.pccontrol.ui.theme.PCControlTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


//@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var lgClass = Login()

    override fun onCreate(savedInstanceState: Bundle?) {
        lgClass.auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                color = MaterialTheme.colors.background
            ) {
                Navigation(lgClass)
            }
        }
    }
}

@Composable
fun LoginScreen(
    navController: NavController,
    lgClass: Login
){
    var showPassword by remember {
        mutableStateOf(false)
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val cntx = LocalContext.current
    val window = (LocalView.current.context as Activity).window

    // Set the status bar color to blue
    window.statusBarColor = Color(0xFF495E57).toArgb()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = "Email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            modifier = Modifier
                .padding(10.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            label = { Text(text = "Password") },
            modifier = Modifier
                .padding(10.dp),
            visualTransformation = if(!showPassword){
                PasswordVisualTransformation()
            }else{
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )
        OutlinedButton(
            onClick = {
                email = email.lowercase()
                getUser(email, cntx)
                lgClass.loginUser(email, password, cntx, navController)
//                test_DB()
            },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF495E57)
            ),
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = "Login",
                color = Color(0xFFEDEFEE)
            )
        }
        OutlinedButton(
            onClick = { navController.navigate(Screen.NewAccountScreen.route) },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF495E57)
            )
        ) {
            Text(
                text = "Create New Account",
                color = Color(0xFFEDEFEE)
            )
        }

    }
}