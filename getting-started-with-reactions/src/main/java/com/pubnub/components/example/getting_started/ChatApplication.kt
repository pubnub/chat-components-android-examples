package com.pubnub.components.example.getting_started

import android.app.Application
import com.pubnub.components.data.Database

class ChatApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Database.initialize(this)
    }
}
