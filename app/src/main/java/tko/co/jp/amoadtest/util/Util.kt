package tko.co.jp.amoadtest.util

import android.text.format.DateFormat
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by aki on 2018/12/07.
 */

object Util {
  var logView: TextView? = null

  fun appendLog(message: String) {
    logView ?: return
    val date = DateFormat.format("yyyy/MM/dd, E, kk:mm:ss", Calendar.getInstance())
    val log = date.toString() + " " + message + "\n"
    logView!!.append(log)
  }

  fun updateHistorySid(newSid: String, currentHistory: ArrayList<String>): ArrayList<String> {
    var list = currentHistory
    list.add(newSid)
    if (list.count() > HISTORY_SID_MAX) {
      list.removeAt(0)
    }
    return list
  }
}



