package com.yamadanihon.tiktokhybrid

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

/**
 * Interim TV-first TikTok web client.
 * This does not pretend to be the native TikTok TV APK or use private mobile APIs.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var web: WebView
    private lateinit var status: TextView
    private lateinit var loading: TextView
    private var lastUrl = "https://www.tiktok.com/foryou"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(8, 8, 10)
        window.navigationBarColor = Color.rgb(8, 8, 10)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(8, 8, 10))
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }
        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(24, 10, 24, 10)
            setBackgroundColor(Color.rgb(18, 18, 22))
        }
        val brand = TextView(this).apply {
            text = "TikTok  TV"
            textSize = 23f
            setTextColor(Color.WHITE)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
        status = TextView(this).apply {
            text = "Đang chuẩn bị bảng tin…"
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER_VERTICAL
            setPadding(24, 0, 16, 0)
        }
        val refresh = Button(this).apply {
            text = "Tải lại"
            isFocusable = true
            setOnClickListener { web.reload() }
        }
        val home = Button(this).apply {
            text = "Dành cho bạn"
            isFocusable = true
            setOnClickListener { loadFeed() }
        }
        top.addView(brand, LinearLayout.LayoutParams(0, -2, 1f))
        top.addView(status, LinearLayout.LayoutParams(0, -2, 2f))
        top.addView(home)
        top.addView(refresh)
        root.addView(top, LinearLayout.LayoutParams(-1, -2))

        val frame = FrameLayout(this)
        web = WebView(this)
        web.setBackgroundColor(Color.BLACK)
        web.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadsImagesAutomatically = true
            mediaPlaybackRequiresUserGesture = false
            javaScriptCanOpenWindowsAutomatically = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            userAgentString = WebSettings.getDefaultUserAgent(this@MainActivity)
                .replace(Regex("; wv\\).*"), ")")
        }
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true)
        web.webChromeClient = WebChromeClient()
        web.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                status.text = "Đang tải bảng tin…"
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url != null) lastUrl = url
                status.text = if (url?.contains("tiktok.com") == true) "TikTok Web · Dùng remote để điều hướng" else "Đã mở trang"
            }
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                return !(url.startsWith("https://") || url.startsWith("http://"))
            }
        }
        frame.addView(web, FrameLayout.LayoutParams(-1, -1))
        loading = TextView(this).apply {
            text = "TikTok TV\n\nĐang kết nối bảng tin trực tuyến…"
            textSize = 22f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.BLACK)
        }
        // Keep the loading overlay out of the way once the page starts rendering.
        frame.addView(loading, FrameLayout.LayoutParams(-1, -1))
        root.addView(frame, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)

        web.setOnTouchListener { _, _ -> false }
        web.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                status.text = "Đang tải bảng tin…"
                loading.visibility = View.VISIBLE
            }
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                loading.visibility = View.GONE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url != null) lastUrl = url
                status.text = if (url?.contains("tiktok.com") == true) "TikTok Web · Dùng remote để điều hướng" else "Đã mở trang"
                loading.visibility = View.GONE
            }
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                return !(url.startsWith("https://") || url.startsWith("http://"))
            }
        }
        loadFeed()
    }

    private fun loadFeed() {
        loading.visibility = View.VISIBLE
        web.loadUrl("https://www.tiktok.com/foryou")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (::web.isInitialized && web.canGoBack()) {
                    web.goBack()
                    return true
                }
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                if (::web.isInitialized) web.requestFocus()
            }
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                web.evaluateJavascript("(function(){var v=document.querySelector('video');if(v){v.paused?v.play():v.pause();}})();", null)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        if (::web.isInitialized) web.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (::web.isInitialized) web.onResume()
    }

    override fun onDestroy() {
        if (::web.isInitialized) {
            web.stopLoading()
            web.loadUrl("about:blank")
            web.removeAllViews()
            web.destroy()
        }
        super.onDestroy()
    }
}
