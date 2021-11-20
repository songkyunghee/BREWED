package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderBinding
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.MainActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter.OrderListAdapter
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.service.OrderService
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback
import java.text.SimpleDateFormat
import java.util.*


class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var orderListAdapter: OrderListAdapter
    private val list = mutableListOf<Int>()

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

        list.add(R.drawable.coffee1)
        list.add(R.drawable.coffee1)
        list.add(R.drawable.coffee1)
        list.add(R.drawable.coffee1)

        orderListAdapter = OrderListAdapter(requireContext(), list)

        binding.recyclerViewOrder.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = linearLayoutManager
            adapter = orderListAdapter

            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        var dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        dateFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        // 현재 날짜를 yyyy-MM-dd 형태의 String으로 받음
        var dateString = dateFormatter.format(System.currentTimeMillis())

        OrderService().getDateNotComOrderList(dateString, "1", getNotComOrderListCallback())

    }

    inner class getNotComOrderListCallback: RetrofitCallback<ArrayList<OrderListResponse>> {
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onSuccess(code: Int, responseData: ArrayList<OrderListResponse>) {
            Toast.makeText(context, "조회에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            if (!responseData.isEmpty()) {
                responseData.forEach {
                    Log.d(TAG, "onSuccess: $it")
                }
            } else {
                Toast.makeText(context, "조회에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }

    }

}