package com.ssafy.smartstore.src.main.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.activity.MainActivity

// Order 탭 - 지도 화면
class MapFragment : Fragment(){
    private lateinit var mainActivity: MainActivity
    private lateinit var map : ImageView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map,null)
        map = view.findViewById(R.id.map)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.setOnClickListener {
            showDialogStore()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }
    private fun showDialogStore() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.DialogStyle)
        builder.apply {

            setView(R.layout.dialog_map_store)
            setCancelable(true)
            setPositiveButton("전화걸기") { dialog, _ ->
                dialog.cancel()
            }
            setNegativeButton("길찾기") { dialog, _ ->
                dialog.cancel()
            }
        }

        builder.create().show()

    }
}