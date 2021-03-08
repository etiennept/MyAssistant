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
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myassistant.ui.theme.MyAssistantTheme
import com.example.myassistant.ui.theme.shapes
import java.time.LocalDateTime

data class Message(var data: String, var date: LocalDateTime, var isresquest: Boolean)
class MainActivity : AppCompatActivity() {
    val telephtone = mutableStateOf(" ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
           ActivityCompat.requestPermissions(this, arrayOf(READ_CONTACTS , CALL_PHONE
           , SET_ALARM ),0);
        setContent {
            MyAssistantTheme {
                Surface( color = MaterialTheme.colors.background) {
                    View()
                }
            }
        }

    }


}

@SuppressLint("NewApi")
@Composable
fun View() {
    val context = LocalContext.current   as Activity

    val list = mutableListOf<Message>()

    var textField by rememberSaveable { mutableStateOf("" ) }
    var boolean by rememberSaveable { mutableStateOf(true  ) }
    Column {
        TopAppBar() {
            Text(text = stringResource(id = R.string.app_name))
        }
        Box(Modifier.fillMaxSize()) {
            LazyColumn {
                if (boolean) {
                    items(list) {
                        Box(Modifier.fillMaxWidth()) {
                            Surface(Modifier.align(if (it.isresquest) Alignment.TopStart else Alignment.TopEnd)) {
                                Surface(
                                    shape = MaterialTheme.shapes.small, /*...*/
                                ) {
                                    MaterialTheme ( shapes =  Shapes(
                                        small = RoundedCornerShape(percent = 50),
                                        )
                                    ) {
                                        Text(it.data) /*...*/
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Row(Modifier.align(Alignment.BottomStart)) {
                TextField(value = textField, onValueChange = { textField = it } , label = {Text("Enter un command")})
                Button(onClick = {
                    boolean = false
                    list.add(Message(textField, LocalDateTime.now(), true))
                    val a = parse(textField, context)
                    list.add(Message( a.string , LocalDateTime.now(), false))
                    boolean = true
                    a.intent?.run  {
                        resolveActivity(context.packageManager)?.let {
                            context.startActivity(this )
                        }
                    }

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