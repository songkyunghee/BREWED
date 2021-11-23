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
import com.ssafy.smartstore.databinding.FragmentOrderDetailBinding
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.CouponListAdapter
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.service.UserService
import kotlin.math.log


class CouponFragment : Fragment() {
    private lateinit var binding: FragmentCouponBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var couponListAdapter: CouponListAdapter
    private lateinit var userId: String
    private var couponId = -1
    var couponSize = 0
    var isEnable = 0


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
                    couponSize = userStampWithCouponList.size
                    if (couponSize == 0) {
                        binding.imgNoCoupon.visibility = View.VISIBLE
                        binding.recyclerview.visibility = View.INVISIBLE
                        binding.btnCouponApply.visibility = View.INVISIBLE

                    } else {
                        Log.d("couponFragment", "couponSize initData: $couponSize")
                        binding.imgNoCoupon.visibility = View.INVISIBLE
                        binding.recyclerview.visibility = View.VISIBLE
                        binding.btnCouponApply.visibility = View.VISIBLE
                        couponListAdapter = CouponListAdapter(mainActivity, userStampWithCouponList)

                        binding.recyclerview.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = couponListAdapter
                            //원래의 목록위치로 돌아오게함
                            adapter!!.stateRestorationPolicy =
                                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                        }


                        couponListAdapter.clickListener = object : CouponListAdapter.OnItemClickListener {
                            override fun onClick(view: View, position: Int, cId: Int, mode: Boolean) {
                                Log.d("coupon", "onClick: $mode")
                                couponListAdapter.notifyDataSetChanged()
                                couponId = cId
                                if (couponListAdapter.selectCheck[position] == 1) {
                                    binding.btnCouponApply.background.setTint(
                                        ContextCompat.getColor(
                                            mainActivity,
                                            R.color.brewed_green
                                        )
                                    )
                                    binding.btnCouponApply.isEnabled = true
                                }
                                if(mode) {
                                        var size = 0
                                        for(i in 0 until couponListAdapter.selectCheck.size) {
                                            if(couponListAdapter.selectCheck[i] == 1) {
                                                size++
                                            }
                                        }
                                    if(size == 0)
                                        binding.btnCouponApply.background.setTint(ContextCompat.getColor(
                                            mainActivity,
                                            R.color.coffee_dark_gray
                                        ))
                                    binding.btnCouponApply.isEnabled = false
                                }
                            }

                        }

                            // 쿠폰 적용하기를 눌렀을때
                            binding.btnCouponApply.setOnClickListener {
                                if(couponListAdapter.checkMode == 1) {
                                    mainActivity.openFragment(9, "couponId", couponId)
                                }
                            }

                    }
                }



            }
        )

    }


}