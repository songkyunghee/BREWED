package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.MenuAdapter
import com.ssafy.smartstore.databinding.FragmentOrderBinding
import com.ssafy.smartstore.src.main.dto.Product
import com.ssafy.smartstore.src.main.service.ProductService
import com.ssafy.smartstore.util.RetrofitCallback

// 하단 주문 탭
private const val TAG = "OrderFragment_싸피"
class OrderFragment : Fragment(){
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var mainActivity: MainActivity
    private lateinit var prodList:List<Product>
    private lateinit var binding:FragmentOrderBinding

    // mode == 0 :: 기본 정렬
    // mode == 1 :: 인기순 정렬
    var mode = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        mode = 0 // create... 기본값으로 초기화
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

}