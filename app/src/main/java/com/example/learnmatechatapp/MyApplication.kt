package com.example.learnmatechatapp
import android.app.Application
import android.util.Log
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appID = "277979d8436be7e0"
        val region = "in"
        val authKey = "db82801d68f0d16b5a44fa3eb2e86700a73d474b"

        val uiKitSettings = UIKitSettings.UIKitSettingsBuilder()
            .setRegion(region)
            .setAppId(appID)
            .setAuthKey(authKey)
            .subscribePresenceForAllUsers()
            .build()

        CometChatUIKit.init(this, uiKitSettings, object : CometChat.CallbackListener<String?>() {
            override fun onSuccess(successString: String?) {
                Log.d("CometChatInit", "UIKit Initialized Successfully")
            }

            override fun onError(e: CometChatException?) {
                Log.e("CometChatInit", "UIKit Init Failed: ${e?.message}")
            }
        })
    }
}
