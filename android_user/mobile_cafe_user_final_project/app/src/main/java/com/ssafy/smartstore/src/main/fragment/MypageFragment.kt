package com.ssafy.smartstore.src.main.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.OrderAdapter
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentMypageBinding
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import com.ssafy.smartstore.src.main.service.OrderService
import com.ssafy.smartstore.util.MainViewModel

// MyPage 탭
private const val TAG = "MypageFragment_싸피"
class MypageFragment : Fragment(){
    private lateinit var orderAdapter : OrderAdapter
    private lateinit var mainActivity: MainActivity

    private lateinit var binding:FragmentMypageBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var id = getUserData()
        initData(id)
        listener()
    }

    private fun listener() {
        binding.btnOrderMyPage.setOnClickListener{
            mainActivity.openFragment(6)
        }

        binding.btnSettingsMyPage.setOnClickListener{

        }

        binding.btnAlarmMyPage.setOnClickListener{
            mainActivity.openFragment(10)
        }

        binding.btnCouponMyPage.setOnClickListener{
            mainActivity.openFragment(8)
        }

        binding.btnCenterMyPage.setOnClickListener{

        }

        binding.btnPolicyMyPage.setOnClickListener{

        }
    }

    private fun initData(id:String){
        // 픽업 완료 처리가 되었을 때 백그라운드 푸쉬 알림 오고나면 브로드캐스트 받기
        val intentFilter = IntentFilter("com.ssafy.shop")
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent!!.action
                initData(id)
                Log.d(TAG, "receive : $action")
            }
        }
        mainActivity.registerReceiver(receiver, intentFilter)

        val userLastOrderList = OrderService().getLastMonthOrder(id)

        userLastOrderList.observe(
            viewLifecycleOwner,
            { userLastOrderList ->
                userLastOrderList.let {
                    orderAdapter = OrderAdapter(mainActivity, userLastOrderList)
                    orderAdapter.notifyDataSetChanged()
                }

                orderAdapter.setItemClickListener(object : OrderAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, orderid:Int) {
                        mainActivity.openFragment(2, "orderId", orderid)
                    }
                })

                binding.recyclerViewOrder.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = orderAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }


                binding.logout.setOnClickListener {
                    mainActivity.openFragment(5)
                }

            }
        )

    }

    private fun getUserData():String{
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.textUserName.text = user.name

        return user.id
    }

}