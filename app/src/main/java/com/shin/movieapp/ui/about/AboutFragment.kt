package com.shin.movieapp.ui.about

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.shin.movieapp.R
import com.shin.movieapp.databinding.FragmentAboutBinding
import com.shin.movieapp.network.WEB_URL
import kotlinx.android.synthetic.main.activity_main.*

class AboutFragment : Fragment(){

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        var view = binding.root

        binding.themoviedbWebview.webViewClient = object : WebViewClient(){

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.visibility = View.INVISIBLE
                binding.loading.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.visibility = View.VISIBLE
                binding.loading.visibility = View.INVISIBLE
            }
        }

        binding.themoviedbWebview.settings.javaScriptEnabled = true
        val settings = binding.themoviedbWebview.settings
        settings.domStorageEnabled = true

        binding.themoviedbWebview.loadUrl(WEB_URL)

        return view
    }
}