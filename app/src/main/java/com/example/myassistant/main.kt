package com.example.myassistant

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.util.Log

import android.provider.ContactsContract




data class Contact(val id: String, val name: String, val phone: List<String>)

fun ContentResolver.getContactPhone(): MutableList<Contact>? {
    val cur = query(
        ContactsContract.Contacts.CONTENT_URI,
        null, null, null, null
    )
    val list = cur?.run {
        val list = mutableListOf<Contact>()
        if (count > 0) {
            while (cur.moveToNext()) {

                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                Log.i(TAG, "Name: $name")

                val phoneList = mutableListOf<String>()
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
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
        return@run list
    }
    cur?.close()
    return list

}