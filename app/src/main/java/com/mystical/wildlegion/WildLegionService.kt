package com.mystical.wildlegion

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WildLegionService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage?) {
        val data = message?.data
        if (data != null) {
            Notifications.notify(this, data["title"]!!, data["body"]!!)
        }
    }
}