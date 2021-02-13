package com.example.myassistant


import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.example.myassistant.ui.theme.MyAssistantTheme
import java.time.LocalDateTime

data class Message(var data: String, var date: LocalDateTime, var isresquest: Boolean)
class MainActivity : AppCompatActivity() {
    val telephtone = mutableStateOf(" ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
           /* ActivityCompat.requestPermissions(this, arrayOf(READ_CONTACTS , CALL_PHONE ),0); */



        setContent {
            MyAssistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    View()
                }
            }
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
             /*   1 -> {
                    val contactUri = data?.data
                    val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    contentResolver.query(contactUri!!, projection, null, null, null)
                        .use { cursor ->
                            // If the cursor returned is valid, get the phone number
                            if (cursor!!.moveToFirst()) {
                                val numberIndex =
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                val number = cursor.getString(numberIndex)
                                Log.i("phonenumbre", number)

                            }
                        }
                    }
                }*/
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun View() {
    val context = AmbientContext.current as Activity

    val list = mutableListOf<Message>()
    var textField by savedInstanceState { "" }
    var boolean by savedInstanceState { true }
    Scaffold(topBar = {
        TopAppBar() {
            Text(text = stringResource(id = R.string.app_name))
        }
    }) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn() {
                if (boolean) {
                    items(list.toList()) {
                        Box(Modifier.fillMaxWidth()) {
                            Surface(Modifier.align(if (it.isresquest) Alignment.TopStart else Alignment.TopEnd)) {
                                Text(it.data)
                            }
                        }
                    }
                }
            }
            Row(Modifier.align(Alignment.BottomStart)) {
                TextField(value = textField, onValueChange = { textField = it })
                Button(onClick = {
                    boolean = false
                    list.add(Message(textField, LocalDateTime.now(), true))
                    parse(textField, context)?.let {
                        list.add(Message(it, LocalDateTime.now(), false))
                    }
                    boolean = true
                }) {
                    Text(text = "Envoyer")

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyAssistantTheme {
        View()
    }
}