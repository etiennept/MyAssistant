package com.example.myassistant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_DIAL
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.compose.runtime.Composable

import android.widget.Toast

import androidx.core.content.ContextCompat.startActivity

val error get() = "je n'ai pas compris"

sealed class DataIntent()
data class Data(val string: String, val intent: Intent? = null) : DataIntent()

fun parse(string: String, context: Activity) :Data =
    Regex("[ ]*crée[ ]+une[ ]+arlame(.*)").matchEntire(string)?.run {

        return@run Data("" )
    } ?: Regex("[ ]*ouvre[ ]+(.+)[ ]*").matchEntire(string)?.run {
        val value = groupValues.last()
        val a = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter {
                it.loadLabel(context.packageManager).toString().equals(value, ignoreCase = true)
            }
        return@run when (a.size) {
            0 -> Data("app non trouvé")
            1 -> {
                Data(
                    "J'ouvre $string",
                    context.packageManager.getLaunchIntentForPackage(a.first().packageName)!!
                )
            }
            else -> {
                Data("Plusieur app ont le même nom")
            }
        }

    } ?: Regex("[ ]*appelle[ ]+(.+) ").matchEntire(string)?.run {
        val value = groupValues.last()
        return@run Regex("le[ ]+(.*)[ ]*").matchEntire(value)?.run {

            val string = groupValues.last()
            val error = Data("je n'ai pas compris le numéro ")
            return@run PhoneNumberUtils.normalizeNumber(string )?.let {
               return@let if ( it == "" ){
                   error
                } else{
                   Data("j'appelle le $it", phoneIntent(it))
                }
            } ?: error




        } ?: Regex("[a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]*[ ]*").matchEntire(value)?.run {
            val contact = Regex("[a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]").find(this.value)!!.value
            val phones = context.contentResolver.getContactPhone()
                ?.filter { it.name.equals(contact, ignoreCase = true) }
            phones?.run {
                return@run when (size) {
                    0 -> {
                       return@run Data("Conctact no trouvé")
                    }
                    1 -> {
                        val contact = first()
                        if (phones.isEmpty()) {
                           Data( "${contact.name} n'a pas de numéro de téléphone" )
                        } else {
                           Data ("j'appelle ${contact.name}" , phoneIntent( contact.phone.first() ) )
                        }
                    }
                    else -> Data("")
                }
            }
        } ?: Data("$error le numéro")
    } ?: Data(error )

fun phoneIntent(phoneNumber: String) = Intent(ACTION_CALL, Uri.parse("tel:$phoneNumber"))
