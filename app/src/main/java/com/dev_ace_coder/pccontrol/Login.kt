package com.dev_ace_coder.pccontrol

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class Login {
    lateinit var auth: FirebaseAuth

    fun registerUser(email: String, password: String,
                     context: Context,
                     navController: NavController, user: User){

//        auth = FirebaseAuth.getInstance()
        if(email.isNotEmpty() && password.isNotEmpty()){

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        makeToast(context, "Successfully Created a New Account.")
//                        val navOptions = NavOptions.Builder().apply {
//                            setPopUpTo(navController.graph.startDestinationId, true)
//                        }.build()
                        navController.navigate(Screen.MainScreen.route)
                    }
                    addUser(user, context)
                }catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        makeToast(context, "Error.. Try Again!!!")
                    }
                }
            }
        }
    }

    fun loginUser(email: String, password: String, context: Context, navController: NavController){
//        auth = FirebaseAuth.getInstance()
        if(email.isNotEmpty() && password.isNotEmpty()){
            val result = CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
//                        currentUser = getUser(email, context)
                        makeToast(context, "Sign In Successful")
                        navController.navigate(Screen.MainScreen.route)
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        makeToast(context, "Sign In Failed. Enter Valid Credentials")
                        Log.i("Login", e.toString())
                    }
                }
            }
        }
    }
}
