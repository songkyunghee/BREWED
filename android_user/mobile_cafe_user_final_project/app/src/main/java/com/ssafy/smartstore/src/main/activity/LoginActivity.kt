package com.ssafy.smartstore.src.main.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.smartstore.src.main.fragment.JoinFragment
import com.ssafy.smartstore.src.main.fragment.LoginFragment
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass.Companion.channel_id
import com.ssafy.smartstore.config.ApplicationClass.Companion.sharedPreferencesUtil

private const val TAG = "LoginActivity_싸피"
class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d(TAG, "onCreate: ")

        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
        })
        createNotificationChannel(channel_id, "brewed")



        //writeTag(makeNdefMessage("T","1")!!, intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG))
        getNFCData(intent)
        //로그인 된 상태인지 확인
        var user = sharedPreferencesUtil.getUser()

        //로그인 상태 확인. id가 있다면 로그인 된 상태
        if (user.id != ""){
            openFragment(1)
        } else {
            // 가장 첫 화면은 홈 화면의 Fragment로 지정
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_login, LoginFragment())
                .commit()
        }
    }

    fun openFragment(int: Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(int){
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }
            2 -> transaction.replace(R.id.frame_layout_login, JoinFragment())
                .addToBackStack(null)

            3 -> transaction.replace(R.id.frame_layout_login, LoginFragment())
                .addToBackStack(null)
        }
        transaction.commit()
    }

    private fun getNFCData(intent: Intent){
        if(intent.action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){
            val rowMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            var aaR: NdefRecord?=null

            if(rowMsgs!=null){

                val message = arrayOfNulls<NdefMessage>(rowMsgs.size)
                for(i in rowMsgs.indices){
                    message[i] = rowMsgs[i] as NdefMessage
                }
                // 실제 저장되어 있는 데치어 추출
                val record_data = message[0]!!.records[0]
                val record_type = record_data.type
                val type = String(record_type)
                if(type=="T"){
                    aaR = NdefRecord.createApplicationRecord("com.ssafy.smartstore")
                }
            }
        }
    }

    private fun writeTag(msg:NdefMessage, tag: Tag?){
        var ndef = Ndef.get(tag)
        // write 할 메시지의 크기
        val size = msg.toByteArray().size

        if(ndef!=null){
            ndef.connect()
            if(!ndef.isWritable){
                Toast.makeText(this, "쓰기 모드를 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            if(ndef.maxSize<size){
                Toast.makeText(this, "용량을 초과했습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            ndef.writeNdefMessage(msg)
            Toast.makeText(this, "쓰기를 완료했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeNdefMessage(type:String?, data:String?):NdefMessage?{

        var ndefR:NdefRecord? = null
        var aaR:NdefRecord?=null
        if(type == "T"){
            ndefR = NdefRecord.createTextRecord("ko",data)
            aaR = NdefRecord.createApplicationRecord("com.ssafy.smartstore")
        }
//        else if(type == "U"){
//            ndefR = NdefRecord.createUri(data)
//        }else{
//            // 모르는 형태
//        }

        return NdefMessage(arrayOf(aaR,ndefR))
    }

    // Notification 수신 채널 추가
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}