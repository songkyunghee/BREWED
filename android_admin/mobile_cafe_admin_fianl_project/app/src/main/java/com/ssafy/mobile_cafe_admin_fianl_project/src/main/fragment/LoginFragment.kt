package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentLoginBinding
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderBinding
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.LoginActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.MainActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter.OrderListAdapter
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Admin
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.service.AdminService
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback


class LoginFragment : Fragment() {
    private lateinit var loginActivity: LoginActivity
    lateinit var binding: FragmentLoginBinding
    lateinit var btnLogin : Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin = view.findViewById(R.id.btnLogin)
        var id = view.findViewById<EditText>(R.id.editTextLoginID)
        var password = view.findViewById<EditText>(R.id.editTextLoginPW)
        btnLogin.setOnClickListener{
            login(id.text.toString(), password.text.toString())
        }

    }

    private fun login(loginId: String, loginPass: String) {
        val admin = Admin(loginId, loginPass)
        AdminService().login(admin, LoginCallback())

    }

    inner class LoginCallback: RetrofitCallback<Admin> {
        override fun onSuccess(code: Int, responseData: Admin) {
           if(responseData.aId != null) {
               Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()

               Log.d(TAG, "onSuccess: ${responseData}")

               ApplicationClass.sharedPreferencesUtil.addAdmin(responseData)
               loginActivity.openFragment(1)
           } else {
               Toast.makeText(context,"ID 또는 패스워드를 확인해 주세요.", Toast.LENGTH_SHORT).show()
           }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "관리자 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }

    }


}