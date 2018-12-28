package tko.co.jp.amoadtest

import android.os.Bundle
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.amoad.AMoAdInterstitialVideo
import com.amoad.AdResult
import tko.co.jp.amoadtest.util.Util

class InterstitialAfiOActivity: BaseActivity() {
    @BindView(R.id.sidEditText)
    open override lateinit var autoCompleteTextView: AutoCompleteTextView

    @BindView(R.id.logTextView)
    lateinit var logTextView: TextView

    @BindView(R.id.loadButton)
    lateinit var loadButton: Button

    @BindView(R.id.showButton)
    lateinit var showButton: Button

    private val tag: String = "tag"

    private var interstilaVideo: AMoAdInterstitialVideo? = null

    override var preferenceKey: String = InterstitialAfiOActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.interstitial_afio_activity)
        ButterKnife.bind(this)

        Util.logView = logTextView
        this.interstilaVideo = null

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
    }

    private fun loadAd(sid: String) {
        this.interstilaVideo = AMoAdInterstitialVideo.sharedInstance(this, sid, tag)
        this.interstilaVideo!!.load(this)
        this.interstilaVideo!!.setListener(object : AMoAdInterstitialVideo.Listener {
            override fun onLoad(amoadInterstitialVideo: AMoAdInterstitialVideo?, result: AdResult?) {
                result?.let {
                    when (it) {
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

            override fun onStart(amoadInterstitialVideo: AMoAdInterstitialVideo?) {
                Util.appendLog("動画の再生を開始しました")
            }

            override fun onComplete(amoadInterstitialVideo: AMoAdInterstitialVideo?) {
                Util.appendLog("動画の再生が完了しました")
            }

            override fun onFailed(amoadInterstitialVideo: AMoAdInterstitialVideo?) {
                Util.appendLog("動画の再生に失敗しました")
            }

            override fun onShown(amoadInterstitialVideo: AMoAdInterstitialVideo?) {
                Util.appendLog("広告を表示しました")
            }

            override fun onDismissed(amoadInterstitialVideo: AMoAdInterstitialVideo?) {
                Util.appendLog("広告を閉じました")
            }

            override fun onClick(amoadInterstitialVideo: AMoAdInterstitialVideo?) {
                Util.appendLog("広告がクリックされました")
            }
        })
        updateHistorySid(sid)
    }

    private fun showAd() {
        this.interstilaVideo?.let {
            it.show(this)
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