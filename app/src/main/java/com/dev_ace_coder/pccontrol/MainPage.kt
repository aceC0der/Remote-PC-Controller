package com.dev_ace_coder.pccontrol

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun MyActivity(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                navController.navigate(Screen.LoginScreen.route)
                currentUser = User() },
            modifier = Modifier
                .padding(16.dp)
                .offset(x = (LocalConfiguration.current.screenWidthDp - 72).dp)
        ) {
            Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
        }
        // Add your other content here...
    }
}

@Composable
fun MainScreen(
    navController: NavController,
){
    val previousDestination = navController.previousBackStackEntry?.destination?.route

    if(previousDestination == Screen.LoginScreen.route || previousDestination == Screen.NewAccountScreen.route){
        navController.popBackStack(navController.graph.startDestinationId, true)
        navController.navigate(Screen.MainScreen.route)
//        currentDestination?.let {
//            val previousDestination =
//                navController.graph.findNode(currentDestination.id)?.parent?.destination
//            previousDestination?.let {
//                navController.popBackStack(previousDestination, false)
//            }
//        }
    }
    val window = (LocalView.current.context as Activity).window
    window.statusBarColor = Color(0xFF495E57).toArgb()

//    GreetingsSection()
    var changePassword by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
//    MyActivity(navController)
    Scaffold(
        topBar = { GreetingsSection() },
        content = {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(200.dp))
                    if (changePassword) {
                        ChangePassword(context = context)
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { changePassword = true },
                                colors = ButtonDefaults.buttonColors(
                                    Color(0xFF495E57)
                                ),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Change PC Password",
                                    color = Color(0xFFEDEFEE)
                                )
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = { unlockPC(context) },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF495E57)
                        ),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Unlock PC",
                            color = Color(0xFFEDEFEE)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { navController.navigate(Screen.WifiScreen.route) },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF495E57)
                        ),
                        modifier = Modifier
                    ) {
                        Text(
                            text = "Change Wifi",
                            color = Color(0xFFEDEFEE)
                        )
                    }
                }
            }
        }
    )

}



@Composable
fun GreetingsSection(
    name: String = currentUser.firstName!!
){
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Welcome, $name",
                style = MaterialTheme.typography.h2
            )
            Text(text = "We wish you have a good day!",
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun ChangePassword(
    context : Context,
    modifier: Modifier = Modifier
){
    var pcPassword by remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = pcPassword,
            onValueChange = {
                pcPassword = it
            },
            label = { Text(text = "PC Password")},
            placeholder = { Text(text = "Enter New Password")},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            modifier = Modifier.padding(10.dp)
        )
        if(pcPassword.length!=13 && pcPassword!="") {
            Text(
                text = "N.B: The Password Length must be exactly 13 characters",
                color = Color(0xFF495E57),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        OutlinedButton(
            onClick = {
                if(pcPassword.length==13){
                    updatePassword(pcPassword, context = context)
                    pcPassword = ""
                }
                else if(pcPassword.length<13){
                    makeToast(context,"Password length is less than 13 !!!")
                }
                else {
                    makeToast(context,"Password length is greater than 13 !!!")
                }
            },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF495E57)
            ),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Change Password",
                color = Color(0xFFEDEFEE)
            )
        }
    }
}