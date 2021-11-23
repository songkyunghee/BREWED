package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentCouponBinding
import com.ssafy.smartstore.databinding.FragmentOrderDetailBinding
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.CouponListAdapter
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.service.UserService


class CouponFragment : Fragment() {
    private lateinit var binding: FragmentCouponBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var couponListAdapter: CouponListAdapter
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private fun getUserData():String{
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        return user.id
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCouponBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = getUserData()
        initData()
    }

    private fun initData() {
        val userStampWithCouponList = UserService().getUserStampWithCoupon(User(userId))
        userStampWithCouponList.observe(
            viewLifecycleOwner,
            {   userStampWithCouponList ->
                userStampWithCouponList.let {
                    couponListAdapter = CouponListAdapter(userStampWithCouponList)
                }
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = couponListAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }

            }
        )

    }


}