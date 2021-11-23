package com.ssafy.smartstore.src.main.adapter

import android.graphics.Color
import android.media.Rating
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.smartstore.src.main.response.MenuDetailWithCommentResponse


class CommentAdapter(var list:MutableList<MenuDetailWithCommentResponse>) :RecyclerView.Adapter<CommentAdapter.CommentHolder>(){
    lateinit var clickListener: OnItemClickListener
    //현재 로그인한 유저의 아이디
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
    var commentText = ""

    var update: Boolean = false
    var updateIdx: Int = -1

    interface OnItemClickListener {
        fun onEditClick(view: View, position: Int, commentId: Int)
        fun onDeleteClick(view: View, position: Int, commentId: Int)
        fun onSaveClick(view: View, position: Int, commentId: Int)
        fun onCancelClick(view: View, position: Int, commentId: Int)
    }

    inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var comment: TextView = itemView.findViewById(R.id.textNoticeContent)
        var acceptImg: ImageView = itemView.findViewById(R.id.iv_modify_accept_comment) // comment 수정 저장 버튼
        var cancelImg: ImageView = itemView.findViewById(R.id.iv_modify_cancel_comment) // comment 수정 취소 버튼
        var modifyImg: ImageView = itemView.findViewById(R.id.iv_modify_comment) // comment 수정 버튼
        var deleteImg: ImageView = itemView.findViewById(R.id.iv_delete_comment) // comment 삭제 버튼

        // 코멘트를 단 유저이름
        var userNameTv: TextView = itemView.findViewById(R.id.tvCommentUser)
        var userRatingBar: RatingBar = itemView.findViewById(R.id.commentRate)

        fun bindInfo(data :MenuDetailWithCommentResponse){
            comment.text = data.commentContent
            userNameTv.text = data.userId
            userRatingBar.rating = data.productRating

            if (data.userId == null) {
                comment.text = "아직 리뷰가 없습니다. 리뷰를 추가해주세요"
                comment.setTextColor(Color.LTGRAY)
            }


            if(userId != data.userId) { // 로그인한 유저 댓글이 아닐 때
                acceptImg.visibility= View.GONE
                cancelImg.visibility= View.GONE
                modifyImg.visibility= View.GONE
                deleteImg.visibility= View.GONE
            } else {

                if(update == false) { // 댓글 수정 버튼을 안누른 상태
                    acceptImg.visibility = View.GONE
                    cancelImg.visibility = View.GONE
                    modifyImg.visibility = View.VISIBLE
                    deleteImg.visibility = View.VISIBLE
                } else {
                    if (updateIdx == data.commentId) {
                        modifyImg.visibility = View.GONE
                        deleteImg.visibility = View.GONE
                        acceptImg.visibility = View.VISIBLE
                        cancelImg.visibility = View.VISIBLE
                    } else {
                        modifyImg.visibility = View.VISIBLE
                        deleteImg.visibility = View.VISIBLE
                        acceptImg.visibility = View.GONE
                        cancelImg.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_comment, parent, false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])

            // 수정
            modifyImg.setOnClickListener {
                if (update == false) {
                    modifyImg.visibility = View.GONE
                    deleteImg.visibility = View.GONE
                    acceptImg.visibility = View.VISIBLE
                    cancelImg.visibility = View.VISIBLE
                    clickListener.onEditClick(it, position, list[position].commentId)
                }
            }

            // 삭제
            deleteImg.setOnClickListener {
                clickListener.onDeleteClick(it, position, list[position].commentId)
            }

            // 수정완료
            acceptImg.setOnClickListener {
                if (update == true) {
                    acceptImg.visibility = View.GONE
                    cancelImg.visibility = View.GONE
                    modifyImg.visibility = View.VISIBLE
                    deleteImg.visibility = View.VISIBLE
                    clickListener.onSaveClick(it, position, list[position].commentId)
                }
            }

            // 취소
            cancelImg.setOnClickListener {
                if (update == true) {
                    acceptImg.visibility = View.GONE
                    cancelImg.visibility = View.GONE
                    modifyImg.visibility = View.VISIBLE
                    deleteImg.visibility = View.VISIBLE
                    clickListener.onCancelClick(it, position, list[position].commentId)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}

