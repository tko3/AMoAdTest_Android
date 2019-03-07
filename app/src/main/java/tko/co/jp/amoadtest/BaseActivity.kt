package tko.co.jp.amoadtest

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import butterknife.ButterKnife
import tko.co.jp.amoadtest.util.Util
import tko.co.jp.amoadtest.util.getArray
import tko.co.jp.amoadtest.util.putArray

open class BaseActivity: AppCompatActivity() {

    protected open lateinit var autoCompleteTextView: AutoCompleteTextView

    protected open var preferenceKey: String = ""

    private var historySids: ArrayList<String> = ArrayList()

    protected fun loadHistorySid() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        historySids = pref.getArray(preferenceKey)
        autoCompleteTextView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, historySids))
    }

    protected fun updateHistorySid(newSid: String) {
        val newHistory = Util.updateHistorySid(newSid, historySids)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putArray(preferenceKey, newHistory)
        editor.apply()
        historySids = pref.getArray(preferenceKey)
        autoCompleteTextView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, historySids))
    }

    private fun hideKeyboard(v: View) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    protected fun setKeyboardHidden() {
        autoCompleteTextView.setOnKeyListener { v, keyCode, event ->
            if ((keyCode !== KeyEvent.KEYCODE_ENTER) && (event.action !== KeyEvent.ACTION_UP)) {
                 false
            } else {
                hideKeyboard(v)
                true
            }
        }
        autoCompleteTextView.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
    }

}

