package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentAlarmBinding
import com.ssafy.smartstore.databinding.FragmentCouponBinding
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.AlarmListAdapter
import com.ssafy.smartstore.src.main.adapter.CouponListAdapter
import com.ssafy.smartstore.src.main.dto.Noti
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.service.UserService
import com.ssafy.smartstore.util.RetrofitCallback


private val TAG = "AlarmFragment_싸피"
class AlarmFragment : Fragment() {
    private lateinit var binding: FragmentAlarmBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var alarmListAdapter: AlarmListAdapter
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
        alarmListAdapter = AlarmListAdapter(mainActivity, mutableListOf())

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
        binding = FragmentAlarmBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = getUserData()
        initData()

    }

    private fun initData() {
        val alarmList = UserService().getNoti(userId)

        alarmList.observe(
            viewLifecycleOwner,
            {   alarmList ->
                alarmList.let {
                    alarmListAdapter.list = alarmList
                    alarmListAdapter.notifyDataSetChanged()

                    binding.recyclerview.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = alarmListAdapter
                        //원래의 목록위치로 돌아오게함
                        adapter!!.stateRestorationPolicy =
                            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    }
                }

            }
        )


        alarmListAdapter.setItemClickListener(object : AlarmListAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, notiId: Int) {
                UserService().deleteNoti(notiId, deleteAlarmCallback())
                val alarmList = UserService().getNoti(userId)
                alarmList.observe(
                    viewLifecycleOwner,
                    {   alarmList ->
                        alarmList.let {
                            //alarmList.removeAt(position)
                            alarmListAdapter.list = alarmList
                            alarmListAdapter.notifyDataSetChanged()
                        }

                    }
                )
            }

        })

    }

    inner class deleteAlarmCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, responseData: Boolean) {
            if (responseData) {
                Log.d(TAG, "알람 삭제 성공")
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "삭제실패")
        }
    }
}