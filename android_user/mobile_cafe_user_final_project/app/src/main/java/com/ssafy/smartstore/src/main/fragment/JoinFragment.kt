package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ssafy.smartstore.src.main.activity.LoginActivity
import com.ssafy.smartstore.databinding.FragmentJoinBinding
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.service.UserService
import com.ssafy.smartstore.util.RetrofitCallback

// 회원 가입 화면
private const val TAG = "JoinFragment_싸피"
class JoinFragment : Fragment(){
    private var checkedId = false
    private lateinit var loginActivity: LoginActivity
    lateinit var binding: FragmentJoinBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //id 중복 확인 버튼
        binding.btnConfirm.setOnClickListener{
            var id = binding.editTextJoinID.text.toString()
            isUsed(id)
        }

        // 회원가입 버튼
        binding.btnJoin.setOnClickListener {
            var id = binding.editTextJoinID.text.toString()
            var name = binding.editTextJoinName.text.toString()
            var pass = binding.editTextJoinPW.text.toString()
            if(id == "" || name == "" || pass == "") {
                Toast.makeText(context, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                join(id, pass, name)
            }

        }
    }

    private fun join(Id: String, Pass: String, Name: String) {
        val user = User(Id, Name, Pass, 0)
        UserService().rest(user, RestCallback())
    }

    private fun isUsed(Id: String) {
        UserService().isUsedID(Id, isUsedIdCallback())
    }


    inner class RestCallback: RetrofitCallback<Boolean> {

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData) {
                Toast.makeText(context, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
                loginActivity.openFragment(3)
            } else {
                Toast.makeText(context, "회원가입 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    inner class isUsedIdCallback: RetrofitCallback<Boolean> {

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData) {
                Toast.makeText(context, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onFailure(code: Int) {
            Toast.makeText(context, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}