package tko.co.jp.amoadtest

import android.content.res.Configuration
import android.os.Bundle
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.amoad.AdResult
import com.amoad.InterstitialAd
import com.amoad.InterstitialAd.Result
import tko.co.jp.amoadtest.util.Util

/**
 * Created by aki on 2018/12/07.
 */

class InterstitialActivity: BaseActivity() {

  @BindView(R.id.sidEditText)
  open override lateinit var autoCompleteTextView: AutoCompleteTextView

  @BindView(R.id.logTextView)
  lateinit var logTextView: TextView

  @BindView(R.id.loadButton)
  lateinit var loadButton: Button

  @BindView(R.id.showButton)
  lateinit var showButton: Button

  private var sid: String? = null

  override var preferenceKey: String = InterstitialActivity::class.java.simpleName

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.interstitial_activity)
    ButterKnife.bind(this)

    Util.logView = logTextView
    sid = null

    loadButton.setOnClickListener {
      val result = validate()
      if (result.first) {
        loadAd(result.second)
      }
    }

    showButton.setOnClickListener {
      showAd()
    }

    loadHistorySid()
    setKeyboardHidden()
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    super.onConfigurationChanged(newConfig)
    InterstitialAd.onConfigurationChanged(newConfig)
  }

  override fun onDestroy() {
    super.onDestroy()
    this.sid?.let {
      InterstitialAd.close(it)
    }
  }

  private fun loadAd(sid: String) {
    this.sid = sid
    updateHistorySid(this.sid!!)
    try {
      InterstitialAd.register(this.sid!!)
    } catch (e: IllegalArgumentException) {
      Util.appendLog("不正なSIDだったため広告を取得しませんでした")
      return
    }
    InterstitialAd.load(this, this.sid!!) { _, result, _ ->
      when(result) {
        AdResult.Success -> {
          Util.appendLog("広告を取得しました")
        }
        AdResult.Empty -> {
          Util.appendLog("表示可能な広告がありませんでした")
        }
        AdResult.Failure -> {
          Util.appendLog("広告の取得に失敗しました")
        }
      }
    }
  }

  private fun showAd() {
    if (this.sid == null || !InterstitialAd.isLoaded(this.sid)) {
      Toast.makeText(this,"広告の読み込みが完了していません", Toast.LENGTH_SHORT)
      return
    }

    InterstitialAd.show(this, this.sid) { result ->
      when (result) {
        Result.Click -> {
          Util.appendLog("リンクがタップされたため広告を閉じました")
        }
        Result.Close -> {
          Util.appendLog("閉じるボタンが押下されたため広告を閉じました")
        }
        Result.CloseFromApp -> {
          Util.appendLog("アプリから広告を閉じました")
        }
        Result.Duplicated -> {
          Util.appendLog("既に広告が表示されているため表示しませんでした")
        }
        Result.Failure -> {
          Util.appendLog("広告の取得に失敗しました")
        }
      }
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