package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderBinding
import com.ssafy.mobile_cafe_admin_fianl_project.databinding.FragmentOrderComBinding
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity.MainActivity
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter.OrderComListAdapter
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter.OrderListAdapter
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Order
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.OrderDetail
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.service.OrderService
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


const val TAG = "OrderComFragment_싸피"
class OrderComFragment : Fragment() {
    private lateinit var binding: FragmentOrderComBinding
    private lateinit var dateString: String
    private lateinit var mainActivity: MainActivity
    private lateinit var orderComListAdapter: OrderComListAdapter
    private val list = mutableListOf<Int>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderComBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.datePickBtn.setOnClickListener{
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                dateString = "${year}-${month+1}-${dayOfMonth}"
                binding.tvOrderDate.text = dateString

                Log.d(TAG, "onViewCreated: $dateString")

                val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)

                // 스토어번호, 날짜, 진행상태를 비교로 사용하기 위해 Path Variable 생성 후 서버와 통신
                OrderService().getDateComOrderList(dateString,"Y","1", getComOrderListCallback())

            }
            DatePickerDialog(requireContext(), R.style.MyDatePickerDialogTheme, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)



        orderComListAdapter = OrderComListAdapter(requireContext(), list)

        binding.recyclerViewOrderDetailList.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = linearLayoutManager
            adapter = orderComListAdapter

            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }




    }

    inner class getComOrderListCallback: RetrofitCallback<ArrayList<OrderListResponse>> {
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onSuccess(code: Int, responseData: ArrayList<OrderListResponse>) {
            Log.d(TAG, "onSuccess: $code, $responseData")
            if (!responseData.isEmpty()){
                Toast.makeText(context, "조회에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                responseData.forEach{
                    Log.d(TAG, "onSuccess: $it")
                }
                // setter를 통해 orderComListAdapter에 아이템 갱신 후 notify...
                orderComListAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "조회에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

}