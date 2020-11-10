package com.tekzee.amiggos.custom

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.SharedPreference

class AlertAgeFragment : BottomSheetDialogFragment() {

    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.alert_age_menu_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        contentView.findViewById<Button>(R.id.cancel).text = languageData!!.klOk
        contentView.findViewById<TextView>(R.id.txt_title).text = message
        contentView.findViewById<Button>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }

    }

    companion object {
        var venueid = ""
        var message = ""
        fun newInstance(msg: String): AlertAgeFragment {
            message = msg
            return AlertAgeFragment()
        }
    }
}