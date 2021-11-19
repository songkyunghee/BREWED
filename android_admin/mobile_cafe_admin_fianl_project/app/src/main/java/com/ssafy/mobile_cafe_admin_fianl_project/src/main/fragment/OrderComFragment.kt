package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderBinding
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderComBinding


class OrderComFragment : Fragment() {
    private lateinit var binding: FragmentOrderComBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderComBinding.inflate(inflater, container, false)
        return binding.root
    }

}