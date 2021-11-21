package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderDetailBinding
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.MainActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter.OrderDetailListAdapter
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderDetailResponse
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.service.OrderService
import java.text.SimpleDateFormat
import java.util.*

// 주문상세화면, My탭  - 주문내역 선택시 팝업
class OrderDetailFragment : Fragment(){
    private lateinit var orderDetailListAdapter: OrderDetailListAdapter
    private lateinit var mainActivity: MainActivity
    private var orderId = -1

    private lateinit var binding: FragmentOrderDetailBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)

        arguments?.let {
            orderId = it.getInt("orderId")
            Log.d(TAG, "onCreate: $orderId")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
    }

    private fun initData(){
        val orderDetails = OrderService().getOrderDetails(orderId)
        Log.d(TAG, "initData: $orderDetails")
        orderDetails.observe(
            viewLifecycleOwner,
            { orderDetails ->
                orderDetails.let {
                    Log.d(TAG, "orderDetails[i]: ${orderDetails[0]}")
                    Log.d(TAG, "orderDetails[i].productId: ${orderDetails[0].productId}")
                    orderDetailListAdapter = OrderDetailListAdapter(mainActivity, orderDetails)
                }

                binding.recyclerViewOrderDetailList.apply {
                    val linearLayoutManager = LinearLayoutManager(context)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    layoutManager = linearLayoutManager
                    adapter = orderDetailListAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }

                setOrderDetailScreen(orderDetails)

                Log.d(TAG, "onViewCreated: $orderDetails")
            }
        )
    }

    // OrderDetail 페이지 화면 구성
    private fun setOrderDetailScreen(orderDetails: List<OrderDetailResponse>){
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH시 mm분 ss초")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        binding.tvOrderUser.text = "아이디: ${orderDetails[0].userId}"
        binding.tvOrderId.text = "주문번호: ${orderDetails[0].orderId}"
        binding.tvOrderDate.text = "주문일시: ${dateFormat.format(orderDetails[0].orderDate)}"
        var totalPrice = 0
        orderDetails.forEach { totalPrice += it.totalPrice }
        binding.tvTotalPrice.text = "결제금액: $totalPrice 원"
    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:Int) =
            OrderDetailFragment().apply {
                Log.d(TAG, "newInstance: 주문 상세 페이지 $key $value")
                
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}