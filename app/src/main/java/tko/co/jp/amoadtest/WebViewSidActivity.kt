package tko.co.jp.amoadtest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.URL

/**
 * Created by aki on 2018/12/07.
 */

class WebViewSidActivity: BaseActivity() {

  @BindView(R.id.sidEditText)
  open override lateinit var autoCompleteTextView: AutoCompleteTextView

  @BindView(R.id.webView)
  lateinit var webView: WebView

  @BindView(R.id.envRadioGroup)
  lateinit var envRadioGroup: RadioGroup

  @BindView(R.id.tagRadioGroup)
  lateinit var tagRadioGroup: RadioGroup

  @BindView(R.id.swIdfa)
  lateinit var swIdfa: Switch

  @BindView(R.id.loadButton)
  lateinit var loadButton: Button

  private val baseUrl = "https://www.amoad.com"

  override var preferenceKey: String = WebViewSidActivity::class.java.simpleName

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.webview_sid_activity)
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
        val isProd = (envRadioGroup.checkedRadioButtonId == R.id.podEnvRadio)
        val isBanner = (tagRadioGroup.checkedRadioButtonId == R.id.bannerTagRadio)
        initWebPage(result.second, isProd, isBanner)
      }
    }
  }

  private fun validate(): Pair<Boolean, String> {
    val sid = autoCompleteTextView.text.toString()
    if (sid.isEmpty()) {
      return Pair(false, sid)
    }

    return Pair(true, sid)
  }

  private fun initWebPage(sid: String, isProd: Boolean, isBanner: Boolean) {
    val domain = if (isProd) { "j.amoad.com" } else { "stg-j.amoad.net" }
    if (swIdfa.isChecked) {
      getAdvertisingId(this)  { adId ->
        loadWebPage(sid, domain, isBanner, adId)
      }
    } else {
      loadWebPage(sid, domain, isBanner, null)
    }
  }

  private fun loadWebPage(sid: String, domain: String, isBanner: Boolean, advertisingId: String?) {
    val dataAdId = if (advertisingId != null) { "data-idfa=${advertisingId}" } else { "" }
    val tag = if (isBanner) {
      "<div class='amoad_frame sid_${sid} container_div color_#0000CC-#444444-#FFFFFF-#0000FF-#009900 sp wv ${dataAdId}'></div><script src='https://${domain}/js/aa.js' type='text/javascript' charset='utf-8' ></script>"
    } else {
      "<div class='amoad_native' data-sid='${sid}' ${dataAdId}></div><script src='https://${domain}/js/n.js' type='text/javascript' charset='utf-8'></script>"
    }
    val html = "<!doctype html><html lang='ja'><head><meta charset='utf-8'><meta name='viewport' content='width=device-width,initial-scale=1.0,user-scalable=no,shrink-to-fit=no'></head><body style='margin: 0; padding: 0'>${tag}</body></html>"

    runOnUiThread {
      webView.loadDataWithBaseURL(baseUrl, html, "text/html", "utf−8", null)
    }
  }


  private fun getAdvertisingId(context: Context, callBack:(String?) -> Unit) {
    Thread(Runnable {
      val id = try {
        AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
      } catch (e: Exception) {
        null
      }
      callBack(id)
    }).start()
  }

}