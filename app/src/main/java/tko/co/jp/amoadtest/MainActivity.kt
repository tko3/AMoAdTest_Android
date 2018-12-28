package tko.co.jp.amoadtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import com.amoad.AMoAdBuildConfig


class MainActivity : AppCompatActivity() {

  @BindView(R.id.listview)
  lateinit var listView: ListView

  val pages = arrayOf("バナー", "インタースティシャル", "インフィードAfiO", "インタースティシャルAfiO", "WebView(URL指定)", "WebView(SID指定)")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ButterKnife.bind(this)

    val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pages)
    listView.adapter = adapter
    listView.setOnItemClickListener { parent, view, position, id ->
      when(position) {
        0 -> {
          val intent = Intent(this, BannerActivity::class.java)
          startActivity(intent)
        }
        1 -> {
          val intent = Intent(this, InterstitialActivity::class.java)
          startActivity(intent)
        }
        2 -> {
          val intent = Intent(this, InfeedAfiOActivity::class.java)
          startActivity(intent)
        }
        3 -> {
          val intent = Intent(this, InterstitialAfiOActivity::class.java)
          startActivity(intent)
        }
        4 -> {
          val intent = Intent(this, WebViewUrlActivity::class.java)
          startActivity(intent)
        }
        5 -> {
          val intent = Intent(this, WebViewSidActivity::class.java)
          startActivity(intent)
        }
      }
    }
    selectEnv()
  }

  // TODO: 接続先を可変にできるようにSDKを修正する
  fun selectEnv() {
    AlertDialog.Builder(this)
        .setTitle("接続先確認")
        .setMessage("ステージング環境に接続しますか？\n(WebViewでの表示確認にこの設定は適用されません)")
        .setPositiveButton("はい"){ dialog, which ->
          AMoAdBuildConfig.toStaging()
        }
        .setNegativeButton("いいえ") { dialog, which -> }
        .show()
  }
}

