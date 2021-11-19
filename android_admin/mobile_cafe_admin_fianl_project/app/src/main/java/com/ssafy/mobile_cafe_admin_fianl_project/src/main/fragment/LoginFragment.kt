package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentLoginBinding
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderBinding
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.LoginActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.MainActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter.OrderListAdapter


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
        btnLogin.setOnClickListener{
            loginActivity.openFragment(1)
        }

    }

}