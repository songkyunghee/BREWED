package com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import java.util.*


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.datePickBtn.setOnClickListener{
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                dateString = "${year}.${month+1}.${dayOfMonth}"
                binding.tvOrderDate.text = dateString

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

}