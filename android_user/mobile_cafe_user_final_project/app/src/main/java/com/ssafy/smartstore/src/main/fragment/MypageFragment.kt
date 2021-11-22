package com.ssafy.smartstore.src.main.fragment

import android.content.Context
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
    private lateinit var list : List<LatestOrderResponse>
    private lateinit var mainViewModel: MainViewModel

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
        mainViewModel = ViewModelProvider(mainActivity).get(MainViewModel::class.java)

        mainViewModel.setLastOderList(id)

        mainViewModel.userLastOrderLiveData.observe(
            viewLifecycleOwner,
            { userListOrderList ->
                userListOrderList.let {
                    mainViewModel.setUserLastOrderItems(userListOrderList)
                }

                binding.recyclerViewOrder.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    orderAdapter = OrderAdapter(context)
                    adapter = orderAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                binding.logout.setOnClickListener {
                    mainActivity.openFragment(5)
                }

                subscribeObservers()
            }
        )

    }

    private fun subscribeObservers() {
        mainViewModel.userLastOrderLiveData.observe(mainActivity, Observer{ userLastOrderLiveData ->
            Log.d(TAG, "subscribeObservers: ${userLastOrderLiveData}")
            orderAdapter.submitList(userLastOrderLiveData)

        })
    }


    private fun getUserData():String{
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.textUserName.text = user.name

        return user.id
    }

}