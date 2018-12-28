package tko.co.jp.amoadtest

import android.os.Bundle
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.amoad.AMoAdView
import com.amoad.AdCallback
import tko.co.jp.amoadtest.util.Util

/**
 * Created by aki on 2018/12/07.
 */
class BannerActivity: BaseActivity() {

  @BindView(R.id.sidEditText)
  open override lateinit var autoCompleteTextView: AutoCompleteTextView

  /*
  @BindView(R.id.sizeRadioGroup)
  lateinit var sizeRadioGroup: RadioGroup
  */

  @BindView(R.id.logTextView)
  lateinit var logTextView: TextView

  @BindView(R.id.loadButton)
  lateinit var loadButton: Button

  @BindView(R.id.adContainer)
  lateinit var adContainer: RelativeLayout

  private var banner: AMoAdView? = null

  override var preferenceKey: String = BannerActivity::class.java.simpleName

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.banner_activity)
    ButterKnife.bind(this)

    Util.logView = logTextView
    this.banner = null

    loadButton.setOnClickListener {
      val result = validate()
      if (result.first) {
        loadAd(result.second)
      }
    }
    loadHistorySid()
  }

  private fun loadAd(sid: String) {
    banner?.let {
      it.removeCallbacks({})
      adContainer.removeView(it)
    }

    banner = AMoAdView(this).run {
      this.sid = sid
      updateHistorySid(sid)
      this.setCallback(object:AdCallback {
        override fun didReceiveEmptyAd() {
          Util.appendLog("表示可能な広告がありませんでした")
        }

        override fun didReceiveAd() {
          Util.appendLog("広告を取得しました")
        }

        override fun didFailToReceiveAdWithError() {
          Util.appendLog("広告の取得に失敗しました")
        }
      })

      adContainer.addView(this)
      this
    }
  }

  private fun validate(): Pair<Boolean, String> {
    val sid = autoCompleteTextView.text.toString()
    if (sid.isEmpty()) {
      Toast.makeText(this, "SIDを入力してください", Toast.LENGTH_SHORT)
      return Pair(false, sid)
    }
    return Pair(true, sid)
  }

}