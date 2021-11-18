package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.OrderAdapter
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentMypageBinding
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import com.ssafy.smartstore.src.main.service.OrderService

// MyPage 탭
private const val TAG = "MypageFragment_싸피"
class MypageFragment : Fragment(){
    private lateinit var orderAdapter : OrderAdapter
    private lateinit var mainActivity: MainActivity
    private lateinit var list : List<LatestOrderResponse>

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
    }

    private fun initData(id:String){

        val userLastOrderLiveData = OrderService().getLastMonthOrder(id)
        Log.d(TAG, "onViewCreated: ${userLastOrderLiveData.value}")
        userLastOrderLiveData.observe(
            viewLifecycleOwner,
            {
                list = it

                orderAdapter = OrderAdapter(mainActivity, list)
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

                Log.d(TAG, "onViewCreated: $it")
            }
        )

    }

    private fun getUserData():String{
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.textUserName.text = user.name

        return user.id
    }

}