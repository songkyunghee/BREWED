package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.LatestOrderAdapter
import com.ssafy.smartstore.src.main.adapter.NoticeAdapter
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentHomeBinding
import com.ssafy.smartstore.src.main.activity.QRActivity
import com.ssafy.smartstore.src.main.adapter.ViewPageAdapter
import com.ssafy.smartstore.src.main.dto.BannerItem
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import com.ssafy.smartstore.src.main.service.OrderService
import com.ssafy.smartstore.src.main.service.ProductService
import com.ssafy.smartstore.src.main.service.StoreService
import com.ssafy.smartstore.util.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Home 탭
private const val TAG = "HomeFragment_싸피"
class HomeFragment : Fragment(){
    private lateinit var latestOrderAdapter : LatestOrderAdapter
    private var noticeAdapter: NoticeAdapter = NoticeAdapter()
    private lateinit var mainActivity: MainActivity
    private lateinit var list : List<LatestOrderResponse>

    // 가속도 센서
    private lateinit var AccelometerSensor: Sensor
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorEventListener: SensorEventListener

    // 롤링 배너
    private lateinit var viewPagerAdapter: ViewPageAdapter
    private lateinit var viewModel: HomeViewModel
    private var isRunning = true

    private lateinit var binding:FragmentHomeBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        // 가속도 센서 Listener 등록
        sensorManager.registerListener(
            sensorEventListener,
            AccelometerSensor,
            SensorManager.SENSOR_DELAY_UI
        )

        isRunning = true
    }

    override fun onPause() {
        super.onPause()
        // 가속도 센서 Listener 삭제
        sensorManager.unregisterListener(sensorEventListener)
        isRunning = false
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

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        AccelometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorEventListener = AccelometerListener()

        viewModel = ViewModelProvider(mainActivity).get(HomeViewModel::class.java)
        val bannerList = StoreService().getBannerList()
        bannerList.observe(
            viewLifecycleOwner,
            { bannerList ->
                bannerList.let {
                    viewModel.setBannerItems(bannerList)
                }

            }
        )

        var orderList = OrderService().getAllOrderList()
        orderList.forEach {
            Log.d(TAG, "onViewCreated: $it")
        }

        initUserName()
        initAdapter()
        var id = getUserData()
        initData(id)

        // 롤링 배너
        initViewPager()
        subscribeObservers()
        autoScrollViewPage()
    }

    private fun initViewPager() {
        binding.viewPager2.apply {
            viewPagerAdapter = ViewPageAdapter()
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    isRunning = true

                }
            })
        }
    }

    private fun subscribeObservers() {
        viewModel.bannerItemList.observe(mainActivity, Observer{ bannerItemList ->
            viewPagerAdapter.submitList(bannerItemList)

        })

        viewModel.currentPosition.observe(mainActivity, Observer { currentPosition ->
            binding.viewPager2.currentItem = currentPosition
        })
    }

    private fun autoScrollViewPage() {
        lifecycleScope.launch{
            whenResumed {
                while(isRunning) {
                    delay(3000)
                    viewModel.getcurrentPosition()?.let {
                        viewModel.setCurrentPosition((it.plus(1)) % 3)
                    }
                }
            }
        }
    }





    private inner class AccelometerListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
//            Log.e(TAG, "${event.values[0]}   onSensorChanged: x: " + String.format("%.4f", event.values[0])
//            + "     y: " + String.format("%.4f", event.values[1]) + "    z: " + String.format("%.4f",event.values[2]))
            if(event.values[0] > 10.0000 && event.values[1] > 10.0000) {
                startActivity(Intent(requireContext(), QRActivity::class.java))
                //Toast.makeText(requireContext(), "흔들었다.", Toast.LENGTH_SHORT).show()
            } else if(event.values[0] < -10.0000 && event.values[1] > -10.0000) {
                startActivity(Intent(requireContext(), QRActivity::class.java))
                //Toast.makeText(requireContext(), "흔들었다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }


    fun initAdapter() {
//        noticeAdapter = NoticeAdapter()
//        binding.recyclerViewNoticeOrder.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter = noticeAdapter
//            //원래의 목록위치로 돌아오게함
//            adapter!!.stateRestorationPolicy =
//                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//        }


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