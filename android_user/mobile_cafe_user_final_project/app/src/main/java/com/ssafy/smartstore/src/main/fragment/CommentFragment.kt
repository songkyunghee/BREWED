package com.ssafy.smartstore.src.main.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.config.ApplicationClass.Companion.commentUserList
import com.ssafy.smartstore.databinding.FragmentCommentBinding
import com.ssafy.smartstore.src.main.activity.MainActivity
import com.ssafy.smartstore.src.main.adapter.CommentAdapter
import com.ssafy.smartstore.src.main.dto.Comment
import com.ssafy.smartstore.src.main.response.MenuDetailWithCommentResponse
import com.ssafy.smartstore.src.main.service.CommentService
import com.ssafy.smartstore.src.main.service.ProductService
import com.ssafy.smartstore.src.main.service.UserService
import com.ssafy.smartstore.util.RetrofitCallback
import java.text.DecimalFormat

private const val TAG = "CommentFragment_싸피"
class CommentFragment : Fragment() {
    private lateinit var binding: FragmentCommentBinding
    private lateinit var mainActivity: MainActivity
    private var commentAdapter = CommentAdapter(mutableListOf())
    private var commentList = mutableListOf<MenuDetailWithCommentResponse>()
    private lateinit var userId: String
    private var productId = -1
    private var rating: Float = 0.0F

    val numberFormat = DecimalFormat("###0.00")
    
    // comment update 관련
    var prevComment: String = ""
    var currentComment: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            productId = it.getInt("productId", -1)
            Log.d(TAG, "onCreate: $productId")
        }
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        
        return binding.root
    }
    
    private fun getUserData(): String {
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        return user.id
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = getUserData()
        
        initData()
        initListener()
        
    }

    private fun initListener() {
        binding.btnCreateComment.setOnClickListener {
            showDialogRatingStar()
        }
    }

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
                        initData()
                        commentAdapter.update = false
                    }

                    override fun onSaveClick(view: View, position: Int, commentId: Int) {
                        var cObj = commentAdapter.list[position]
                        var comment = Comment(cObj.commentId, cObj.userId!!, productId, cObj.productRating.toFloat(), cObj.commentContent!!)
                        commentAdapter.update = false
                    }

                    override fun onCancelClick(view: View, position: Int, commentId: Int) {
                        commentAdapter.list[position].commentContent = prevComment
                        commentAdapter.notifyDataSetChanged()
                        commentAdapter.update = false
                    }
                }
            }
        )
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

    private fun showDialogRatingStar() {

        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_menu_comment, null)

        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setTitle("")

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialogView.findViewById<Button>(R.id.confirmBtn).setOnClickListener {
            val comment: String = binding.editRestComment.text.toString() ?: ""
            val rBar: RatingBar = mDialogView.findViewById(R.id.ratingBarMenuDialogComment)
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

            mAlertDialog.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.cancelBtn).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun showDialogEditComment(position: Int) {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_menu_editcomment, null)

        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setTitle("")

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialogView.findViewById<Button>(R.id.editconfirmBtn).setOnClickListener {
            currentComment = mDialogView.findViewById<EditText>(R.id.dialEditText).text.toString()

            commentAdapter.list[position].commentContent = currentComment
            commentAdapter.notifyDataSetChanged()

            mAlertDialog.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.editCancelBtn).setOnClickListener {
            commentAdapter.update = false
            commentAdapter.notifyDataSetChanged()

            mAlertDialog.dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(key: String, value: Int) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }



}