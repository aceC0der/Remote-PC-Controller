package com.dev_ace_coder.pccontrol

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//import com.dev_ace_coder.pccontrol.ui.theme.PCControlTheme

@Composable
fun NewAccount(
    navController: NavController,
    lgClass: Login
){

    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var retypePassword by remember{
        mutableStateOf("")
    }
    var pcPassword by remember {
        mutableStateOf("")
    }
    var showPassword by remember { mutableStateOf(value = false) }
    val cntx = LocalContext.current

    val window = (LocalView.current.context as Activity).window
    window.statusBarColor = Color(0xFF495E57).toArgb()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        OutlinedTextField(
            value = firstName,
            onValueChange = {
                            firstName = it
            },
            label = { Text(text = "First Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            modifier = Modifier
                .padding(10.dp)
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = {
                            lastName = it
            },
            label = { Text(text = "Last Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            modifier = Modifier
                .padding(10.dp)
        )
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
        if(email!="" && !CredentialsValidator.isValidEmail(email)) Text(text = "Invalid Email!!! Enter a valid one.")
        OutlinedTextField(
            value = pcPassword,
            onValueChange = {
                pcPassword = it
            },
            label = { Text(text = "PC Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            modifier = Modifier
                .padding(10.dp)
        )
        OutlinedTextField(

            value = password,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF495E57)),
            onValueChange = { password = it},
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
        if(password!="" && !CredentialsValidator.isStrongPassword(password)) Text(text = "Weak Password")
        OutlinedTextField(
            value = retypePassword,
            onValueChange = {
                            retypePassword = it
            },
            label = { Text(text = "Re-type Password") },
            modifier = Modifier
                .padding(10.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if(password!=retypePassword) Text(text = "Passwords don't match!")
        Button(
            onClick = {
                email = email.lowercase()
                if(password==retypePassword){
                    if(CredentialsValidator.isValidEmail(email)){
                        if(CredentialsValidator.isStrongPassword(password)){
                            var userName: String = ""
                            for(letter in email){
                                if(letter == '@') break
                                userName += letter
                            }
                            val usr = User(firstName, lastName, pcPassword, email, userID = null, userName = userName)
                            lgClass.registerUser(email,password,cntx, navController, usr)
                        }
                        else{
                            makeToast(cntx, "Weak Password")
                        }
                    }
                    else{
                        makeToast(cntx, "Invalid Email")
                    }

                }
                else{
                    makeToast(cntx, "Passwords do not match")
                }
            },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF495E57)
            ),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Create New Account",
                color = Color(0xFFEDEFEE)
            )
        }
    }
}