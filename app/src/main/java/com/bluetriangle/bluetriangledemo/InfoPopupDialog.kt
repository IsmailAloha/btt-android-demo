package com.bluetriangle.bluetriangledemo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class InfoPopupDialog(context: Context, val title: String, val content: String): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(R.layout.scenario_info_popup)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        findViewById<TextView>(R.id.info_title).text = title
        findViewById<TextView>(R.id.info_content).text = content
        findViewById<Button>(R.id.close_button).setOnClickListener {
            cancel()
        }
    }
}