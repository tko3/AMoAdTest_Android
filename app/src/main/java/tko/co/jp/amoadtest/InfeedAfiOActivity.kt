package tko.co.jp.amoadtest

import android.os.Bundle
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.amoad.AMoAdNativeListener
import com.amoad.AMoAdNativeListener.Result
import com.amoad.AMoAdNativeViewManager
import tko.co.jp.amoadtest.util.Util

class InfeedAfiOActivity: BaseActivity() {

    @BindView(R.id.sidEditText)
    open override lateinit var autoCompleteTextView: AutoCompleteTextView

    @BindView(R.id.logTextView)
    lateinit var logTextView: TextView

    @BindView(R.id.loadButton)
    lateinit var loadButton: Button

    @BindView(R.id.clearButton)
    lateinit var clearButton: Button

    @BindView(R.id.adContainer)
    lateinit var adContainer: RelativeLayout

    private var sid: String? = null

    private val tag: String = "tag"

    override var preferenceKey: String = InfeedAfiOActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infeed_afio_activity)
        ButterKnife.bind(this)

        Util.logView = logTextView

        this.sid = null

        loadButton.setOnClickListener {
            val result = validate()
            if (result.first) {
                loadAd(result.second)
            }
        }

        clearButton.setOnClickListener {
            clearAd()
        }

        loadHistorySid()
    }

    private fun validate(): Pair<Boolean, String> {
        val sid = autoCompleteTextView.text.toString()
        if (sid.isEmpty()) {
            Toast.makeText(this, "SIDを入力してください", Toast.LENGTH_SHORT)
            return Pair(false, sid)
        }
        if (this.adContainer.childCount > 0) {
            Toast.makeText(this, "広告を削除してから読み込んでください", Toast.LENGTH_SHORT)
            return Pair(false, sid)
        }
        return Pair(true, sid)
    }

    private fun loadAd(sid: String) {
        this.sid = sid
        updateHistorySid(sid)
        AMoAdNativeViewManager.getInstance(this).prepareAd(sid, true, true)
        val view = AMoAdNativeViewManager.getInstance(this).createView(sid, this.tag, R.layout.afio_layout, object : AMoAdNativeListener {
            override fun onClicked(sid: String?, tag: String?, templateView: View?) {
                Util.appendLog("広告をタップしました")
            }

            override fun onReceived(sid: String?, tag: String?, templateView: View?, result: Result?) {
                when (result) {
                    Result.Success -> {
                        Util.appendLog("広告を取得しました")
                    }
                    Result.Empty -> {
                        Util.appendLog("表示可能な広告がありませんでした")
                    }
                    Result.Failure -> {
                        Util.appendLog("広告の取得に失敗しました")
                    }
                }
            }

            override fun onIconReceived(sid: String?, tag: String?, templateView: View?, result: Result?) {
                when (result) {
                    Result.Success -> {
                        Util.appendLog("アイコン画像を取得しました")
                    }
                    Result.Empty -> {
                        Util.appendLog("表示可能なアイコン画像がありませんでした")
                    }
                    Result.Failure -> {
                        Util.appendLog("アイコン画像の取得に失敗しました")
                    }
                }
            }

            override fun onImageReceived(sid: String?, tag: String?, templateView: View?, result: Result?) {
                when (result) {
                    Result.Success -> {
                        Util.appendLog("メイン動画を取得しました")
                    }
                    Result.Empty -> {
                        Util.appendLog("表示可能なメイン動画がありませんでした")
                    }
                    Result.Failure -> {
                        Util.appendLog("メイン動画の取得に失敗しました")
                    }
                }
            }
        })
        this.adContainer.addView(view)
    }

    private fun clearAd() {
        this.sid?.let {
            AMoAdNativeViewManager.getInstance(this).clearAd(it, this.tag)
            this.adContainer.removeAllViews()
        }
    }
}

