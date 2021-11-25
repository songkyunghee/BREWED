package com.ssafy.smartstore.src.main.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.MenuAdapter
import com.ssafy.smartstore.databinding.FragmentOrderBinding
import com.ssafy.smartstore.src.main.dto.Product
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse
import com.ssafy.smartstore.src.main.service.ProductService
import com.ssafy.smartstore.src.main.service.UserService
import kotlinx.coroutines.*
import org.w3c.dom.Text

// 하단 주문 탭
private const val TAG = "OrderFragment_싸피"
class OrderFragment : Fragment(){
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var mainActivity: MainActivity
    private lateinit var prodList:List<Product>
    private lateinit var binding: FragmentOrderBinding
    private lateinit var userId: String
    private var preCouponNum = -1


    private lateinit var list: LiveData<MutableList<StampWithCouponResponse>>

    // mode == 0 :: 기본 정렬
    // mode == 1 :: 인기순 정렬
    var mode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = getUserData()
        arguments?.let {
            preCouponNum = it.getInt("preCouponNum")
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private fun getUserData():String{
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        return user.id
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        mode = 0 // create... 기본값으로 초기화

        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "onCreateView: ")
            list = UserService().getUserStampWithCoupon(User(userId))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity.hideBottomNav(false)






        initToolbar()
        initData()

        binding.floatingBtn.setOnClickListener {
            //장바구니 이동
            mainActivity.openFragment(1)
        }

        binding.btnMap.setOnClickListener{
            mainActivity.openFragment(4)
        }
    }

    fun initToolbar() {
        var toolbar = binding.toolbarLayout
        toolbar.inflateMenu(R.menu.order_fragment_items)

        toolbar.setOnMenuItemClickListener{
            onOptionsItemSelected(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_cart -> {
                Log.d(TAG, "onOptionsItemSelected: 장바구니")
                mainActivity.openFragment(1)
                return true
            }
            R.id.action_hot -> {
                Log.d(TAG, "onOptionsItemSelected: 인기순")
                mode = 1
                initData()
                return true
            }
            R.id.action_default -> {
                Log.d(TAG, "onOptionsItemSelected: 기본값")
                mode = 0
                initData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData(){

        if (mode == 0) {
            val productList = ProductService().getProductList()
            productList.observe(
                viewLifecycleOwner,
                { productList ->
                    productList.let {
                        menuAdapter = MenuAdapter(productList)
                    }
                    binding.recyclerViewMenu.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = menuAdapter
                        //원래의 목록위치로 돌아오게함
                        adapter!!.stateRestorationPolicy =
                            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    }

                    menuAdapter.setItemClickListener(object : MenuAdapter.ItemClickListener {
                        override fun onClick(view: View, position: Int, productId: Int) {
                            mainActivity.openFragment(3, "productId", productId)
                        }
                    })
                }
            )
        } else {
            val productList = ProductService().getHotProductList()
            Log.d(TAG, "initData: ${productList}")
            productList.observe(
                viewLifecycleOwner,
                { productList ->
                    productList.let {
                        menuAdapter = MenuAdapter(productList)
                    }
                    binding.recyclerViewMenu.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = menuAdapter
                        //원래의 목록위치로 돌아오게함
                        adapter!!.stateRestorationPolicy =
                            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    }

                    menuAdapter.setItemClickListener(object : MenuAdapter.ItemClickListener {
                        override fun onClick(view: View, position: Int, productId: Int) {
                            mainActivity.openFragment(3, "productId", productId)
                        }
                    })
                }
            )
        }

        Log.d(TAG, "TEST1::onCreate: OrderFragment 전 쿠폰 개수 $preCouponNum")

        val list = UserService().getUserStampWithCoupon(User(userId))
        list.observe(
            viewLifecycleOwner,
            {

                var nowCouponNum = it.size
                Log.d(TAG, "TEST1::onCreate: OrderFragment 현재 쿠폰 개수 ${nowCouponNum}")

                if (preCouponNum != -1) {
                    if (preCouponNum < nowCouponNum) {

                        val mDialogView =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.coupon_dialog, null)
                        mDialogView.findViewById<TextView>(R.id.textCouponDialogMsg).text =
                            "쿠폰이 ${nowCouponNum - preCouponNum}개 생겼습니다. 짝짝짝!"

                        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setView(mDialogView)
                            .setTitle("")

                        if (!mainActivity.isFinishing) {
                            val mAlertDialog = mBuilder.show()
                            mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            mDialogView.findViewById<TextView>(R.id.dialog_btn).setOnClickListener {
                                mAlertDialog.dismiss()
                            }
                        }
                        preCouponNum = -1
                    }
                }

            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:Int) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }

}