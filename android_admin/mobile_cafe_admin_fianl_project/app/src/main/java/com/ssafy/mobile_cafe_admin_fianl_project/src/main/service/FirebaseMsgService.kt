package com.ssafy.mobile_cafe_admin_fianl_project.src.main.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.adminToken
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.channel_id
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.uploadToken
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.LoginActivity

class FirebaseMsgService : FirebaseMessagingService() {

    // 새로운 토큰 생성될 때 마다 해당 콜백 호출
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")

        // 새로운 토큰 수신 시 서버로 전송
        adminToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.let { message ->
            val messageTitle = message!!.getValue("title")
            val messageContent = message!!.getValue("body")

            if (messageTitle.equals("주문이 접수되었습니다. 주문을 확인해주세요.")) {
                val intent = Intent("add.order")
                sendBroadcast(intent)
            }

            val mainIntent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val mainPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0)

            val mLargeIconForNoti = BitmapFactory.decodeResource(resources, R.drawable.brewed_logo)

            val builder = NotificationCompat.Builder(this, channel_id)
                .setLargeIcon(mLargeIconForNoti)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)

            Log.d(TAG, "onMessageReceived: $messageTitle")

            NotificationManagerCompat.from(this).apply {
                notify(101, builder.build())
            }
        }
    }

}