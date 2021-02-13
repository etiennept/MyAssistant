package com.example.myassistant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_DIAL
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AmbientContext
import android.widget.Toast

import androidx.core.content.ContextCompat.startActivity

abstract class  DataIntent
data class Data  ( val string :String? ,  val intent : Intent? ) :DataIntent(   )

fun parse(string: String, context: Activity): String?   {
    val error = "je n'ai pas compris"
    return when {
    Regex("[ ]*ouvre .+").matches(string) -> {
        var string  = string.replaceFirst(Regex("[ ]*ouvre[ ]+"), "")
        string = string.reversed().replaceFirst(Regex("[ ]*" )   , "" ).reversed()
        val a = context.packageManager.getInstalledApplications( PackageManager.GET_META_DATA )
            .filter{ it.loadLabel(context.packageManager).toString().equals(string, ignoreCase = true ) }
        when(a.size){
            0 -> "app non trouvé"
            1  ->{  context.startActivity(context.packageManager.getLaunchIntentForPackage( a.first().packageName)!! )
                "j'ouvre $string"
            }
             else ->{
                "plusieur app ont le même nom"
            }
        }
    }
    Regex("[ ]*appelle.+").matches(string) -> {

        val value = string.replaceFirst(Regex("[ ]*appelle[ ]+"), "")


        when {
            Regex("le[ ]+(0|\\+33)[1-9]([-. ]?[0-9]{2}){4}[ ]*").matches(value) -> {
                val phoneNumber =
                    Regex("(0|\\+33)[1-9]([-. ]?[0-9]{2}){4}").find(value)!!.value.replace(
                        Regex("-|.| "),
                        ""
                    )

                "j'appelle le $phoneNumber"
            }
            Regex("[a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]*[ ]*").matches(value) -> {
                val contact = Regex("[a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]").find(value)!!.value

                val phones = context.contentResolver.getContactPhone()?.filter {it.name == contact}
                phones?.run  {
                    return@run when(size ){
                        0 -> {
                            "Conctact no trouvé"
                        }
                        1 -> {
                            val contact  =  first()
                            if (phones.isEmpty() ){
                                "${contact.name} n'a pas de numéro de téléphone"
                            } else{
                                context.call( contact.phone.first() )
                                "j'appelle ${contact.name}"
                            }

                        }
                        else ->  ""

                    }
                } ?: "contact no initializer"

            }
            else -> "$error le numéro"
        }



    }
    else -> error
}

}

fun Context.call (phoneNumber : String ){
    startActivity(Intent(ACTION_CALL).apply {
        data = Uri.parse("tel:$phoneNumber")
    } )
}


