package com.example.myassistant

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.util.Log

import android.provider.ContactsContract




data class Contact(val id: String, val name: String, val phone: List<String>)

fun ContentResolver.getContactPhone(): MutableList<Contact>? {
    val list =  query(
        ContactsContract.Contacts.CONTENT_URI,
        null, null, null, null
    )?.run {
        val list = mutableListOf<Contact>()
        if (count > 0) {
            while (moveToNext()) {

                val id = getString(getColumnIndex(ContactsContract.Contacts._ID))
                val name = getString(getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))


                val phoneList = mutableListOf<String>()
                if (getInt(getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )!!
                    while (pCur.moveToNext()) {
                        phoneList.add(pCur.getString(pCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER))
                        )
                    }
                    pCur.close()
                }
                list.add(Contact(id, name, phoneList.toList()))
            }
        }
        close()
        return@run list
    }
    return list

}