package com.ssafy.smartstore.src.main.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var toolbar = binding.toolbarLayout
        toolbar.inflateMenu(R.menu.order_fragment_items)

        initData()

        binding.floatingBtn.setOnClickListener{
            //장바구니 이동
            mainActivity.openFragment(1)
        }

        binding.btnMap.setOnClickListener{
            mainActivity.openFragment(4)
        }
    }

    private fun initData(){
        val productList = ProductService().getProductList()
        productList.observe(
            viewLifecycleOwner,
            { productList ->
                productList.let{
                    menuAdapter = MenuAdapter(productList)
                }
                binding.recyclerViewMenu.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = menuAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }

                menuAdapter.setItemClickListener(object : MenuAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, productId:Int) {
                        mainActivity.openFragment(3, "productId", productId)
                    }
                })
            }
        )

    }
}