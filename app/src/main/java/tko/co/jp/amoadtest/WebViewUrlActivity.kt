package tko.co.jp.amoadtest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import java.net.URL

/**
 * Created by aki on 2018/12/07.
 */

class WebViewUrlActivity: BaseActivity() {

  @BindView(R.id.urlEditText)
  open override lateinit var autoCompleteTextView: AutoCompleteTextView

  @BindView(R.id.webView)
  lateinit var webView: WebView

  @BindView(R.id.loadButton)
  lateinit var loadButton: Button

  override var preferenceKey: String = WebViewUrlActivity::class.java.simpleName

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.webview_url_activity)
    ButterKnife.bind(this)

    webView.settings.javaScriptEnabled = true
    webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
    webView.webViewClient = object: WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (view!!.hitTestResult.type > 0) {
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
          startActivity(intent)
          return false
        }
        return super.shouldOverrideUrlLoading(view, url)
      }
    }

    loadButton.setOnClickListener {
      val result = validate()
      if (result.first) {
        loadWebPage(result.second)
      }
    }
    setKeyboardHidden()
  }

  private fun validate(): Pair<Boolean, String> {
    val url = autoCompleteTextView.text.toString()
    if (url.isEmpty()) {
      return Pair(false, url)
    }
    try {
      URL(url)
    } catch (e: Exception) {
      return Pair(false, url)
    }
    return Pair(true, url)
  }

  private fun loadWebPage(url: String) {
    webView.loadUrl(url)
  }

}