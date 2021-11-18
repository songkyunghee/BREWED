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
import com.ssafy.smartstore.src.main.adapter.LatestOrderAdapter
import com.ssafy.smartstore.src.main.adapter.NoticeAdapter
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentHomeBinding
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import com.ssafy.smartstore.src.main.service.OrderService

// Home 탭
private const val TAG = "HomeFragment_싸피"
class HomeFragment : Fragment(){
    private lateinit var latestOrderAdapter : LatestOrderAdapter
    private var noticeAdapter: NoticeAdapter = NoticeAdapter()
    private lateinit var mainActivity: MainActivity
    private lateinit var list : List<LatestOrderResponse>

    private lateinit var binding:FragmentHomeBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var orderList = OrderService().getAllOrderList()
        orderList.forEach {
            Log.d(TAG, "onViewCreated: $it")
        }
        initUserName()
        initAdapter()
        var id = getUserData()
        initData(id)
    }

    fun initAdapter() {
        noticeAdapter = NoticeAdapter()
        binding.recyclerViewNoticeOrder.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = noticeAdapter
            //원래의 목록위치로 돌아오게함
            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }


//        latestOrderAdapter = LatestOrderAdapter()
//        //메인화면에서 최근 목록 클릭시 장바구니로 이동
//        latestOrderAdapter.setItemClickListener(object : LatestOrderAdapter.ItemClickListener{
//            override fun onClick(view: View, position: Int) {
//                mainActivity!!.openFragment(1)
//            }
//        })
//        binding.recyclerViewLatestOrder.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = latestOrderAdapter
//            //원래의 목록위치로 돌아오게함
//            adapter!!.stateRestorationPolicy =
//                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//        }
    }

    private fun initData(id:String){

        val userLastOrderLiveData = OrderService().getLastMonthOrder(id)
        Log.d(TAG, "onViewCreated: ${userLastOrderLiveData.value}")
        userLastOrderLiveData.observe(
            viewLifecycleOwner,
            {
                list = it

                latestOrderAdapter = LatestOrderAdapter(mainActivity, list)
                //메인화면에서 최근 목록 클릭시 장바구니로 이동
                latestOrderAdapter.setItemClickListener(object : LatestOrderAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, orderId:Int) {
                        ApplicationClass.shoppingSharedPreference.deleteList()
                        setShoppingList(orderId)



                    }
                })
                binding.recyclerViewLatestOrder.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = latestOrderAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
//
//                latestOrderAdapter = LatestOrderAdapter(mainActivity, list)
//                latestOrderAdapter.setItemClickListener(object : LatestOrderAdapter.ItemClickListener{
//                    override fun onClick(view: View, position: Int, orderid:Int) {
//                        mainActivity.openFragment(2, "orderId", orderid)
//                    }
//                })
//
//                binding.recyclerViewLatestOrder.apply {
//                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                    adapter = orderAdapter
//                    //원래의 목록위치로 돌아오게함
//                    adapter!!.stateRestorationPolicy =
//                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//                }

            }
        )

    }

    private fun setShoppingList(orderId:Int){
        val orderDetails = OrderService().getOrderDetails(orderId)
        orderDetails.observe(
            viewLifecycleOwner,
            { orderDetails ->
                orderDetails.let {
                    for (i in it.indices) {
                        Log.d(TAG, "orderDetails[i]: ${orderDetails[i]}")
                        Log.d(TAG, "orderDetails[i].productId: ${orderDetails[i].productId}")
                        var pId = 0
                        if(orderDetails[i].productName=="coffee1") pId = 1
                        else if(orderDetails[i].productName=="coffee2") pId = 2
                        else if(orderDetails[i].productName=="coffee3") pId = 3
                        else if(orderDetails[i].productName=="coffee4") pId = 4
                        else if(orderDetails[i].productName=="coffee5") pId = 5
                        else if(orderDetails[i].productName=="coffee6") pId = 6
                        else if(orderDetails[i].productName=="coffee7") pId = 7
                        else if(orderDetails[i].productName=="coffee8") pId = 8
                        else if(orderDetails[i].productName=="coffee9") pId = 9
                        else if(orderDetails[i].productName=="coffee10") pId = 10
                        else if(orderDetails[i].productName=="tea1") pId = 11
                        else if(orderDetails[i].productName=="cookie") pId = 12
                        ApplicationClass.shoppingSharedPreference.putItem(pId, orderDetails[i].quantity)
                    }
                    mainActivity.openFragment(1)
                }
            }
        )
    }
    private fun initUserName(){
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.textUserName.text = "${user.name} 님"
    }
    private fun getUserData():String{
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.textUserName.text = user.name

        return user.id
    }
}