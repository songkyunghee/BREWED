package com.ssafy.smartstore.src.main.service

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.config.ApplicationClass.Companion.channel_id
import com.ssafy.smartstore.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.smartstore.config.ApplicationClass.Companion.uploadToken
import com.ssafy.smartstore.config.ApplicationClass.Companion.userToken
import com.ssafy.smartstore.src.main.activity.LoginActivity
import com.ssafy.smartstore.src.main.dto.Noti
import com.ssafy.smartstore.util.MainViewModel
import com.ssafy.smartstore.util.RetrofitCallback
import com.ssafy.smartstore.util.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

private const val TAG = "MsgService_싸피"
class FirebaseMsgService : FirebaseMessagingService() {
    // 새로운 토큰 생성될 때 마다 해당 콜백 호출
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")

        // 새로운 토큰 수신 시 서버로 전송
        userToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        remoteMessage.data.let { message ->
            val messageTitle = message!!.getValue("title")
            val messageContent = message!!.getValue("body")

            Log.d(TAG, "onMessageReceived: pickup ${messageTitle}")

            if (messageTitle.equals("pickup")) {

                // 픽업 완료 처리 알람 왔을 때 브로드캐스트로 알리기
                val intent = Intent("com.ssafy.shop")
                sendBroadcast(intent)
            }
            else {

                val mainIntent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                val mainPendingIntent: PendingIntent =
                    PendingIntent.getActivity(this, 0, mainIntent, 0)

                val mLargeIconForNoti =
                    BitmapFactory.decodeResource(resources, R.drawable.brewed_logo)


                val builder = NotificationCompat.Builder(this, channel_id)
                    .setLargeIcon(mLargeIconForNoti)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(messageTitle)
                    .setContentText(messageContent)
                    .setAutoCancel(true)
                    .setContentIntent(mainPendingIntent)
                var userId = sharedPreferencesUtil.getUser().id
                Log.d(TAG, "onMessageReceived: $messageTitle  $messageContent $userId")

                UserService().insertNoti(Noti(messageContent, userId), insertAlarmCallback())

                NotificationManagerCompat.from(this).apply {
                    notify(101, builder.build())
                }
            }
        }
    }

    inner class insertAlarmCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, responseData: Boolean) {
            if (responseData) {
                Log.d(TAG, "알람 등록 성공")
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "등록실패")
        }
    }

}