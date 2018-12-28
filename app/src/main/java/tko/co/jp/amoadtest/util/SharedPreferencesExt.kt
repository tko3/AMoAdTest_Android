package tko.co.jp.amoadtest.util

import android.content.SharedPreferences
import org.json.JSONArray


fun SharedPreferences.getArray(key: String): ArrayList<String> {
    val json = JSONArray(this.getString(key, "[]"))
    var array = ArrayList<String>()
    for (i in 0 until json.length()) {
        array.add(json.get(i).toString())
    }
    return array
}

fun SharedPreferences.Editor.putArray(key: String, array: ArrayList<String>) {
    val json = JSONArray(array)
    this.putString(key, json.toString())
    this.apply()
}
