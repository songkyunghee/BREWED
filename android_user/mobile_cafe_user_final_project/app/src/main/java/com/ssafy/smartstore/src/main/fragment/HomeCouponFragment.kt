package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentCouponBinding
import com.ssafy.smartstore.databinding.FragmentHomeCouponBinding
import com.ssafy.smartstore.databinding.FragmentOrderDetailBinding
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.CouponListAdapter
import com.ssafy.smartstore.src.main.adapter.HomeCouponListAdapter
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.service.UserService


class HomeCouponFragment : Fragment() {
    private lateinit var binding: FragmentHomeCouponBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var couponListAdapter: HomeCouponListAdapter
    private lateinit var userId: String
    var couponSize = 0


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
        binding = FragmentHomeCouponBinding.inflate(inflater, container, false)

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
                    Log.d("couponFragment", "userStampWithCouponList : ${userStampWithCouponList.size}")
                    couponSize = userStampWithCouponList.size
                    if (couponSize == 0) {
                        binding.imgNoCoupon.visibility = View.VISIBLE
                        binding.couponView.visibility = View.INVISIBLE

                    } else {
                        Log.d("couponFragment", "couponSize initData: $couponSize")
                        binding.imgNoCoupon.visibility = View.INVISIBLE
                        binding.couponView.visibility = View.VISIBLE
                        binding.textCouponNum.text = "총 보유쿠폰 ${userStampWithCouponList.size.toString()}개"
                        couponListAdapter = HomeCouponListAdapter(mainActivity, userStampWithCouponList)

                        binding.recyclerview.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = couponListAdapter
                            //원래의 목록위치로 돌아오게함
                            adapter!!.stateRestorationPolicy =
                                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                        }


                    }
                }

            }
        )

    }


}