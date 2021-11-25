package com.ssafy.smartstore.src.main.fragment

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.*
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.ShoppingListAdapter
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.config.ApplicationClass.Companion.storeId
import com.ssafy.smartstore.src.main.dto.Order
import com.ssafy.smartstore.src.main.dto.OrderDetail
import com.ssafy.smartstore.src.main.dto.Product
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse
import com.ssafy.smartstore.src.main.service.OrderService
import com.ssafy.smartstore.src.main.service.ProductService
import com.ssafy.smartstore.src.main.service.PushService
import com.ssafy.smartstore.src.main.service.UserService
import com.ssafy.smartstore.util.CommonUtils.makeComma
import com.ssafy.smartstore.util.MainViewModel
import com.ssafy.smartstore.util.RetrofitCallback
import kotlin.collections.ArrayList

//장바구니 Fragment
private const val TAG = "ShoppingListFragment_싸피"
class ShoppingListFragment : Fragment(){
    // 주문했을 때 NFC 다이얼로그
    lateinit var dialog: AlertDialog

    private lateinit var shoppingListRecyclerView: RecyclerView
    private lateinit var shoppingListAdapter : ShoppingListAdapter
    private lateinit var mainActivity: MainActivity
    private lateinit var btnShop : Button
    private lateinit var btnTakeout : Button
    private lateinit var btnOrder : Button
    private lateinit var btnCoupon: Button
    private var isShop : Boolean = true
    private var isCoupon: Boolean = false
    private lateinit var userId: String
    private var couponId = -1
    private var couponNum = 0

    private var userCouponList: MutableList<StampWithCouponResponse> = mutableListOf()
    private var prodList:MutableList<Product> = mutableListOf()
    private var prodCntList:MutableList<Int> = mutableListOf()
    private lateinit var shoppingList:MutableList<Int>
    private lateinit var mainViewModel: MainViewModel

    private lateinit var textShoppingCount : TextView
    private lateinit var textShoppingMoney : TextView
    private lateinit var textDiscount: TextView
    private var shoppingCount : Int = 0
    private var shoppingMoney : Int = 0

    var table: String = "takeOut"
    var pIntent: PendingIntent? = null
    lateinit var filters: Array<IntentFilter>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)

        arguments?.let {
            couponId = it.getInt("couponId")
            Log.d(TAG, "onCreate: ShoppingListFragment $couponId")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_list,null)
        shoppingListRecyclerView =view.findViewById(R.id.recyclerViewShoppingList)
        btnShop = view.findViewById(R.id.btnShop)
        btnTakeout = view.findViewById(R.id.btnTakeout)
        btnOrder = view.findViewById(R.id.btnOrder)
        btnCoupon = view.findViewById(R.id.btnCoupon)
        textShoppingCount = view.findViewById(R.id.textShoppingCount)
        textShoppingMoney = view.findViewById(R.id.textShoppingMoney)
        textDiscount = view.findViewById(R.id.textDiscount)
        return view
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.hideBottomNav(true)
        mainViewModel = ViewModelProvider(mainActivity).get(MainViewModel::class.java)
        userId = getUserData()
        shoppingCount = 0
        shoppingMoney = 0

        if(couponId == 0) {
            textDiscount.visibility = View.INVISIBLE
        } else {
            textDiscount.visibility = View.VISIBLE
        }

        var i = Intent(requireContext(), MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        pIntent = PendingIntent.getActivity(requireContext(), 0, i, 0)

        val ndf_filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        ndf_filter.addDataType("text/plain")
        filters = arrayOf(ndf_filter)

        //장바구니에서 리스트 가져옴
        shoppingList = ApplicationClass.shoppingSharedPreference.getList()


        val productList = ProductService().getProductList()
        productList.observe(
            viewLifecycleOwner,
            { productList ->
                setShoppingListScreen(productList)

            }
        )

        val list = UserService().getUserStampWithCoupon(User(userId))

        list.observe(
            viewLifecycleOwner,
            { list ->
                userCouponList = list
                couponNum = list.size
                Log.d(TAG, "TEST2:: 사용자의 전 쿠폰 개수 completedOrder: ${couponNum}")
            }
        )


        btnShop.setOnClickListener {
            btnShop.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_color)
            btnTakeout.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_non_color)
            isShop = true
        }
        btnTakeout.setOnClickListener {
            btnTakeout.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_color)
            btnShop.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_non_color)
            isShop = false
        }
        btnOrder.setOnClickListener {
            if (isShop) {
                if (shoppingCount == 0){
                    Toast.makeText(requireContext(),"주문할 상품이 없습니다.",Toast.LENGTH_SHORT).show()
                } else {
                    yourStore()
                }
            } else {
                //거리가 200이상이라면
                if (true) {

                    showDialogForOrderTakeoutOver200m()
                }
            }
        }
        btnCoupon.setOnClickListener {
            mainActivity.openFragment(7)
        }
    }


    private fun getUserData():String{
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        return user.id
    }

    private fun setShoppingListScreen(productList: List<Product>) {
        prodList.clear()
        prodCntList.clear()

        for(i in 1..12){
            var product = productList[i-1]
            if(shoppingList[i] != 0){
                prodList.add(product)
                prodCntList.add(shoppingList[i])
                shoppingCount += shoppingList[i]
                shoppingMoney += product.price*shoppingList[i]
            }
        }
        setViewListener()
    }

    fun setViewListener(){
        textShoppingCount.text = ("총 "+shoppingCount.toString()+"개")

        if(couponId == 0) {
            textShoppingMoney.text = makeComma(shoppingMoney)
        } else {
            textShoppingMoney.text = makeComma(shoppingMoney - 2000)
        }


        //어댑터에 in
        shoppingListAdapter = ShoppingListAdapter(requireContext(), prodList, prodCntList)

        //어댑터에서 아이템마다 붙어있는 X표시 클릭해서 삭제할 때 리스너
        shoppingListAdapter!!.setItemClickListener(object : ShoppingListAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, productId:Int) {
                ApplicationClass.shoppingSharedPreference.deleteItem(productId)

                shoppingCount -= prodCntList[position]
                shoppingMoney -= prodList[position].price*prodCntList[position]
                textShoppingCount.text = ("총 "+shoppingCount.toString()+"개")
                if(shoppingCount == 0) {
                    textDiscount.visibility = View.INVISIBLE
                    textShoppingMoney.text = makeComma(shoppingMoney)
                } else {
                    textShoppingMoney.text = makeComma(shoppingMoney - 2000)
                }
                prodList.removeAt(position)
                prodCntList.removeAt(position)

                shoppingListRecyclerView.adapter = shoppingListAdapter
            }
        })
        shoppingListRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = linearLayoutManager
            adapter = shoppingListAdapter
            //원래의 목록위치로 돌아오게함
            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }


    private fun showDialogForOrderInShop() {

        ApplicationClass.nfcAdapter.enableForegroundDispatch(requireActivity(), pIntent, filters, null)
        val orderBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        orderBuilder.setTitle("알림")
        orderBuilder.setMessage(
            "Table NFC를 찍어주세요.\n"
        )
        orderBuilder.setCancelable(true)
        orderBuilder.setNegativeButton("취소") { dialog, _ ->
            ApplicationClass.nfcAdapter.disableForegroundDispatch(requireActivity())
            dialog.cancel()
        }
        dialog = orderBuilder.create()
        dialog.show()
    }

    private fun showDialogForOrderTakeoutOver200m() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("알림")
        builder.setMessage(
            "현재 고객님의 위치가 매장과 200m 이상 떨어져 있습니다.\n정말 주문하시겠습니까?"
        )
        builder.setCancelable(true)
        builder.setPositiveButton("확인") { _, _ ->
            completedOrder()

        }
        builder.setNegativeButton("취소"
        ) { dialog, _ -> dialog.cancel() }
        builder.create().show()
    }

    private fun yourStore(){

        if (storeId == "0") {
            showDialogForStoreId()
        } else {
            showDialogForOrderInShop()
        }

    }
    private fun showDialogForStoreId() {
        var storeArray: Array<String> = arrayOf("브루드 1호점", "브루드 2호점")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("매장 선택")
        builder.setItems(storeArray, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        storeId = "1"
                        showDialogForOrderInShop()
                    }
                    1 -> {
                        storeId = "2"
                        showDialogForOrderInShop()
                    }
                }
            })
        builder.create().show()

    }

    private fun completedOrder(){

        var order_table = if(table!="takeOut") table else "takeOut"

        var details = ArrayList<OrderDetail>()
        for(i in 0 until prodList.size){
            if(prodCntList[i] != 0){
                Log.d(TAG, "prodList[i].id: ${prodList[i].id}")
                details.add(OrderDetail(prodList[i].id, prodCntList[i]))
                ProductService().updateSalesProduct(Product(prodList[i].id,"","","",0,"",prodCntList[i]))
            }
        }
        val order = Order(ApplicationClass.sharedPreferencesUtil.getUser().id, 1, order_table,  "N", details)

        OrderService().makeOrder(order)

        prodList.clear()
        prodCntList.clear()
        shoppingListRecyclerView.adapter = shoppingListAdapter
        ApplicationClass.shoppingSharedPreference.deleteList()

        UserService().selectAdminToken("1", AdminTokenCallback())
        if(couponId != 0) {
            UserService().deleteCoupon(couponId, deleteCouponCallback())
        }

        mainActivity.openFragment(6, "preCouponNum", couponNum)
    }
    inner class deleteCouponCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, responseData: Boolean) {
            Log.d(TAG, "onSuccess: ${couponId} 쿠폰이 적용되었습니다.")
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
    
    inner class AdminTokenCallback: RetrofitCallback<String> {
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "어드민 토큰 정보 불러오는 중 통신오류")
        }

        override fun onSuccess(code: Int, responseData: String) {
//            Toast.makeText(requireContext(),"주문이 완료되었습니다.",Toast.LENGTH_SHORT).show()
            PushService().sendMessageTo(responseData,"Brewed Coffee","주문이 접수되었습니다.\n주문을 확인해주세요.")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }

    }

    fun getNdefMessages(intent: Intent) {
        if(intent.action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){
            var msgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if(msgs!=null){

                val message = arrayOfNulls<NdefMessage>(msgs.size)
                for(i in msgs.indices){
                    message[i] = msgs[i] as NdefMessage
                }
                val record_data = message[0]!!.records[0]
                val record_type = record_data.type
                val type = String(record_type)
                if(type=="T"){
                    val data = message[0]!!.records[0].payload
                    table = String(data,3,data.size-3)
                    dialog.dismiss()
                    completedOrder()

                    //ApplicationClass.nfcAdapter.disableForegroundDispatch(requireActivity())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:Int) =
            ShoppingListFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}