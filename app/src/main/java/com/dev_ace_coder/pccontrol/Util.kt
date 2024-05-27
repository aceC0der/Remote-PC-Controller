package com.dev_ace_coder.pccontrol

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.net.PasswordAuthentication
import java.util.SimpleTimeZone

var currentUser = User("", "", "", "", "", "")

fun makeToast(context: Context, txt: String){
    Toast.makeText(context, txt, Toast.LENGTH_LONG).show()
}

fun addUser(usr: User, cntx: Context){
    val database: DatabaseReference = Firebase.database.getReference()
    val userID = database.child("Users").push().key!!
    usr.userID = userID
    database.child("Users").child(usr.userName!!).setValue(usr)
        .addOnCompleteListener{
            makeToast(cntx, "User Created Successfully")
        }
        .addOnFailureListener {
            makeToast(cntx, "Error!!! $it")
        }
    database.child("Devices").child(usr.userID!!).setValue(usr)
        .addOnCompleteListener{
//            makeToast(cntx, "Device Created Successfully")
        }
        .addOnFailureListener {
            makeToast(cntx, "Error!!! $it")
        }
    currentUser = usr
}

fun getUser(email: String, cntx: Context){
    val database = FirebaseDatabase.getInstance().getReference("Users")
    var userName: String = ""
    for(letter in email){
        if(letter == '@') break
        userName += letter
    }
    var fName: String? = "null"
    var lName: String? = "null"
    var pcPass: String? = "null"
    var userID: String? = "null"
    var enterPass : Boolean = false
    var st : String = ""
//    Log.i("firebase", "Got value ${userName}")
    database.child(userName).get().addOnSuccessListener {
            fName = it.child("firstName").value.toString()
            lName = it.child("lastName").value.toString()
            userID = it.child("userID").value.toString()
            pcPass = it.child("pcPassword").value.toString()
            val ep = it.child("enterPassword").value.toString()
            if(ep=="true"){
                enterPass = true
            }
            st = it.child("status").value.toString()
            currentUser = User(fName, lName, pcPass, email, userID, userName, enterPass, st)

    }.addOnFailureListener{
        makeToast(cntx,"firebase Error getting data $it")
    }
}

fun updatePassword(newPassword: String, context: Context){
    val database: DatabaseReference = Firebase.database.getReference()
    database.child("Users").child(currentUser.userName!!).child("pcPassword").setValue(newPassword)
    database.child("Devices").child(currentUser.userID!!).child("pcPassword").setValue(newPassword).addOnSuccessListener {
        makeToast(context, "PC Password Updated Successfully")
    }.addOnFailureListener {
        makeToast(context, "PC Password Update Failed!!!")
    }
}

fun unlockPC(context: Context){
    val database: DatabaseReference = Firebase.database.getReference()
    try {
        database.child("Users").child(currentUser.userName!!).child("enterPassword").setValue(true)
        database.child("Devices").child(currentUser.userID!!).child("enterPassword").setValue(true).addOnSuccessListener {
            makeToast(context, "Instruction Sent Successfully")
        }.addOnFailureListener {
            makeToast(context, "Failed!!!")
        }
        statusCheck(context = context)
    }catch (e: Exception){
        makeToast(context, "Error!!!!!!")
    }
}

fun statusCheck(context: Context){
    val database: DatabaseReference = Firebase.database.getReference()
    val valueEventListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.getValue(Boolean::class.java)
            if(value==false){
                makeToast(context, "PC Unlocked Successfully")
                database.child("Devices").child(currentUser.userID!!).child("enterPassword").removeEventListener(this)
//                database.child("Devices").child(currentUser.userID!!).child("status").setValue("Idle")
//                database.child("Devices").child(currentUser.userID!!).child("enterPassword").setValue(false)
            }
        }
        override fun onCancelled(error: DatabaseError) {
            makeToast(context, "Error!!!")
        }
    }
    database.child("Devices").child(currentUser.userID!!).child("enterPassword").addValueEventListener(valueEventListener)
}