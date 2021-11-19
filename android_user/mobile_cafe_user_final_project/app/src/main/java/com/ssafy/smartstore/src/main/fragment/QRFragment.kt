package com.ssafy.smartstore.src.main.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.ssafy.smartstore.R
import com.ssafy.smartstore.databinding.FragmentOrderDetailBinding
import com.ssafy.smartstore.databinding.FragmentQRBinding
import com.ssafy.smartstore.src.main.activity.MainActivity


class QRFragment : Fragment() {
    lateinit var binding: FragmentQRBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQRBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val webview = binding.webview
        val webview = WebView(requireContext())
        webview.loadUrl("https://nid.naver.com/login/privacyQR")

        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().flush()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String
            ): Boolean {
                if (view!!.canGoBack()) {
                    view.loadUrl(url)
                    return true
                }
                return false
            }
        }
        val settings = webview.settings

        settings.javaScriptEnabled = true
        settings.setSupportMultipleWindows(false)
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = false
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true

        //webview.loadUrl("https://nid.naver.com/login/privacyQR")

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(webview)

        builder.setOnKeyListener { dialog, keyCode, event ->
            if(event.action != KeyEvent.ACTION_DOWN)
                true
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                if(webview.canGoBack()) {
                    webview.goBack()
                } else {
                    dialog.dismiss()
                }
                true
            }
            false
        }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
            dialog.dismiss()
            mainActivity.openFragment(8)
        }
    }

}