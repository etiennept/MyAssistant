package com.example.myassistant

fun main(){
     println(Regex("[ ]*ouvre.+").matchEntire( "ouvre contact" )?.value )
}
