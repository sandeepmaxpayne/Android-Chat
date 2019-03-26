package com.sandeep.androidchat.model

class ChatMessage(val id: String, val text: String, val toID: String, val fromID: String, val timestamp: Long){
    constructor(): this("", "", "", "", -1)
}