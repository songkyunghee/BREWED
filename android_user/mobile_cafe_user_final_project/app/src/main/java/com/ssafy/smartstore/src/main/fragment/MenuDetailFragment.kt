package com.ssafy.smartstore.src.main.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.CommentAdapter
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.databinding.FragmentMenuDetailBinding
import com.ssafy.smartstore.src.main.dto.Comment
import com.ssafy.smartstore.src.main.response.MenuDetailWithCommentResponse
import com.ssafy.smartstore.src.main.service.CommentService
import com.ssafy.smartstore.src.main.service.ProductService
import com.ssafy.smartstore.util.CommonUtils
import com.ssafy.smartstore.util.RetrofitCallback
import java.text.DecimalFormat
import kotlin.math.round

//메뉴 상세 화면 . Order탭 - 특정 메뉴 선택시 열림
private const val TAG = "MenuDetailFragment_싸피"
class MenuDetailFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var commentAdapter = CommentAdapter(mutableListOf())
    private var productId = -1
    private var rating: Float = 0.0F
    private var commentList = mutableListOf<MenuDetailWithCommentResponse>()
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
    val numberFormat = DecimalFormat("###0.00")

    // update 관련
    var prevComment: String = ""
    var currentComment: String = ""


    private lateinit var binding: FragmentMenuDetailBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)

        arguments?.let {
            productId = it.getInt("productId", -1)
            Log.d(TAG, "onCreate: $productId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListener()
    }

    //MutableLiveData<List<MenuDetailWithCommentResponse>>
    private fun initData() {
        val menuDetails = ProductService().getProductWithComments(productId)
        menuDetails.observe(
            viewLifecycleOwner,
            { menuDetails ->
                menuDetails.let {
                    commentAdapter = CommentAdapter(menuDetails)

                    Log.d(TAG, "initData: ${menuDetails}")
                    commentAdapter.notifyDataSetChanged()
                }
                commentList = menuDetails
                binding.recyclerViewMenuDetail.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = commentAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }

                commentAdapter.clickListener = object : CommentAdapter.OnItemClickListener {
                    override fun onEditClick(view: View, position: Int, commentId: Int) {
                        if (commentAdapter.update == false) {
                            prevComment = commentAdapter.list.get(position).commentContent!!
                            commentAdapter.updateIdx = commentId
                            commentAdapter.update = true
                            showDialogEditComment(position)
                        }
                    }

                    override fun onDeleteClick(view: View, position: Int, commentId: Int) {
                        ProductService().deleteComment(commentId, deleteCommnetCallback())
                        commentAdapter.update = false
                    }

                    override fun onSaveClick(view: View, position: Int, commentId: Int) {
                        var cObj = commentAdapter.list[position]
                        var comment = Comment(cObj.commentId, cObj.userId!!, productId, cObj.productRating.toFloat(), cObj.commentContent!!)
                        CommentService().update(comment)
                        commentAdapter.update = false
                    }

                    override fun onCancelClick(view: View, position: Int, commentId: Int) {
                        commentAdapter.list[position].commentContent = prevComment
                        commentAdapter.notifyDataSetChanged()
                        commentAdapter.update = false
                    }
                }

                setScreen(menuDetails)
            }
        )
    }

    // 초기 화면 설정
    private fun setScreen(menu: List<MenuDetailWithCommentResponse>) {
        Log.d(TAG, "setScreen: ${menu[0]}")
        binding.txtMenuName.text = menu[0].productName
        binding.txtMenuPrice.text = "${CommonUtils.makeComma(menu[0].productPrice)}"
        binding.txtRating.text = "${(round(menu[0].productRatingAvg * 10) / 10)}점"
        binding.ratingBar.rating = menu[0].productRatingAvg.toFloat() / 2
        Glide.with(this)
            .load("${ApplicationClass.MENU_IMGS_URL}${menu[0].productImg}")
            .into(binding.menuImage)
    }

    private fun initListener() {


        binding.btnAddList.setOnClickListener {

            if (binding.textMenuCount.text.toString().toInt() == 0){
                Toast.makeText(context, "수량을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {

                ApplicationClass.shoppingSharedPreference.putItem(
                    productId,
                    binding.textMenuCount.text.toString().toInt()
                )
                Toast.makeText(context, "상품이 장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCreateComment.setOnClickListener {
            showDialogRatingStar()
        }
        binding.btnAddCount.setOnClickListener {
            binding.textMenuCount.text =
                (binding.textMenuCount.text.toString().toInt() + 1).toString()
        }
        binding.btnMinusCount.setOnClickListener {
            if (binding.textMenuCount.text.toString() != "0") {
                binding.textMenuCount.text =
                    (binding.textMenuCount.text.toString().toInt() - 1).toString()
            }
        }

        binding.btnCreateComment.setOnClickListener {
            showDialogRatingStar()
        }
    }

    private fun insertComment(userId: String, productId: Int, rating: Float, comment: String) {

        val comment = Comment(0, userId, productId, rating, comment)
        ProductService().restComment(comment, insertCommentCallback())
    }

    inner class insertCommentCallback : RetrofitCallback<Boolean> {

        override fun onSuccess(code: Int, responseData: Boolean) {
            if (responseData) {
                initData()
                Toast.makeText(context, "별점이 등록되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onFailure(code: Int) {
            Toast.makeText(context, "별점 등록 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }

    private fun showDialogRatingStar() {
        //F10
        val builder = AlertDialog.Builder(requireContext(), R.style.DialogStyle)
        val v1 = layoutInflater.inflate(R.layout.dialog_menu_comment, null)
        builder.setView(v1)

        val listener = DialogInterface.OnClickListener { p0, p1 ->
            val alert = p0 as AlertDialog
            val rBar: RatingBar = alert.findViewById(R.id.ratingBarMenuDialogComment)

            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val comment: String = binding.editRestComment.text.toString() ?: ""
                    rating = rBar.rating * 2

                    // insert
                    insertComment(userId, productId, rating, comment)

                    // init
                    binding.editRestComment.setText("")
                    Toast.makeText(requireContext(), "별점이 등록 되었습니다.", Toast.LENGTH_SHORT).show()

                    // notify
                    //ProductService().getProductWithComments(productId, ProductWithCommentInsertCallback())
                    val menuDetails = ProductService().getProductWithComments(productId)
                    menuDetails.observe(
                        viewLifecycleOwner,
                        { menuDetails ->
                            menuDetails.let {
                                commentAdapter.list = menuDetails
                                commentAdapter.notifyDataSetChanged()
                            }
                        }
                    )


                    binding.txtRating.text =
                        numberFormat.format(commentList.get(0).productRatingAvg.toFloat())
                    binding.ratingBar.rating = commentList.get(0).productRatingAvg.toFloat()
                }
            }
        }

        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", listener)
        builder.show()

    }

    private fun showDialogEditComment(position: Int) {
        val builder = AlertDialog.Builder(requireContext(), R.style.DialogStyle)
        val v1 = layoutInflater.inflate(R.layout.dialog_menu_editcomment,null)
        builder.setView(v1)

        val listener = DialogInterface.OnClickListener { dialog, which ->
            val alert = dialog as AlertDialog

            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    currentComment = alert.findViewById<EditText>(R.id.dialEditText).text.toString()

                    commentAdapter.list[position].commentContent = currentComment
                    commentAdapter.notifyDataSetChanged()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    commentAdapter.update = false
                    commentAdapter.notifyDataSetChanged()
                }
            }
        }
        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", listener)
        builder.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(key: String, value: Int) =
            MenuDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }

    inner class deleteCommnetCallback : RetrofitCallback<Boolean> {

        override fun onSuccess(code: Int, responseData: Boolean) {
            if (responseData) {
                initData()
                Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "댓글 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}