package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.app.AlertDialog
import android.content.*
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.dateString
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderBinding
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.MainActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter.OrderListAdapter
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Order
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.service.OrderService
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.service.PushService
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback
import java.text.SimpleDateFormat
import java.util.*


class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var orderListAdapter : OrderListAdapter
    private var storeId = "1"
    var size = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderListAdapter = OrderListAdapter(requireContext(), mutableListOf())

        var dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        dateFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        // 현재 날짜를 yyyy-MM-dd 형태의 String으로 받음
        dateString = dateFormatter.format(System.currentTimeMillis())
        initData(dateString)

    }

    private fun initData(date: String) {

        val intentFilter = IntentFilter("add.order")
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent!!.action
                initData(date)
                Log.d(TAG, "receive : $action")
            }
        }
        mainActivity.registerReceiver(receiver, intentFilter)

        val notComOrderList = OrderService().getDateNotComOrderList(date, storeId)


        Log.d(TAG, "initData: ${notComOrderList.value}")
        if (viewLifecycleOwner != null) {
            notComOrderList.observe(
                viewLifecycleOwner,
                { notComOrderList ->
                    size = notComOrderList.size
                    Log.d(TAG, "initData: $size")
                    if (size == 0) {
                        binding.noOrderLayout.visibility = View.VISIBLE
                        binding.recyclerViewOrder.visibility = View.GONE
                    } else {
                        binding.noOrderLayout.visibility = View.GONE
                        binding.recyclerViewOrder.visibility = View.VISIBLE
                    }

                    notComOrderList.let {

                        orderListAdapter.notComOrderList = notComOrderList
                        orderListAdapter.notifyDataSetChanged()
                    }

                    binding.recyclerViewOrder.apply {
                        val linearLayoutManager = LinearLayoutManager(context)
                        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                        layoutManager = linearLayoutManager
                        adapter = orderListAdapter

                        adapter!!.stateRestorationPolicy =
                            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    }

                    orderListAdapter.clickListener = object : OrderListAdapter.OnItemClickListener {
                        override fun onOrderTakeClick(view: View, position: Int, orderID: Int) {
                            var o = orderListAdapter.notComOrderList[position]

                            Log.d(TAG, "onOrderTakeClick: position = $position  $o")
                            var order = Order(o.o_id, o.user_id, o.s_id, o.order_table, "M")
                            OrderService().update(order)

                            PushService().sendMessageTo(
                                o.token,
                                "Brewed Coffee",
                                "Brewed Coffee에서 주문을 접수했습니다."
                            )

                            orderListAdapter.process = "M"
                        }

                        override fun onOrderMakeClick(view: View, position: Int, orderID: Int) {
                            var o = orderListAdapter.notComOrderList[position]
                            Log.d(TAG, "onOrderTakeClick: position = $position $o")
                            var order = Order(o.o_id, o.user_id, o.s_id, o.order_table, "P")
                            OrderService().update(order)
                            PushService().sendMessageTo(
                                o.token,
                                "Brewed Coffee",
                                "메뉴가 나왔습니다. 픽업해주세요."
                            )
                        }

                        override fun onOrderComClick(view: View, position: Int, orderID: Int) {
                            var builder = AlertDialog.Builder(mainActivity)
                            builder.setTitle("")
                            builder.setMessage("픽업 완료 처리를 하시겠습니까?")

                            var listener = object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, w: Int) {
                                    when (w) {
                                        DialogInterface.BUTTON_POSITIVE -> {
                                            var o = orderListAdapter.notComOrderList[position]
                                            Log.d(TAG, "onOrderTakeClick: position = $position $o")
                                            var order =
                                                Order(o.o_id, o.user_id, o.s_id, o.order_table, "Y")
                                            OrderService().update(order, UpdateCallback())
                                            PushService().sendMessageTo(
                                                o.token,
                                                "pickup",
                                                "pickup body"
                                            )
                                        }
                                        DialogInterface.BUTTON_NEGATIVE -> {

                                        }
                                    }
                                }

                            }
                            builder.setPositiveButton("확인", listener)
                            builder.setNegativeButton("취소", listener)
                            builder.show()

//                        var o = orderListAdapter.notComOrderList[position]
//                        Log.d(TAG, "onOrderTakeClick: position = $position $o")
//                        var order = Order(o.o_id, o.user_id, o.s_id, o.order_table, "Y")
//                        OrderService().update(order)
                        }
                    }
                }
            )
        }


    }

    inner class UpdateCallback(): RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: Error $t")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            Log.d(TAG, "onSuccess: update Success")
            initData(dateString)
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure Code :: $code")
        }

    }
}