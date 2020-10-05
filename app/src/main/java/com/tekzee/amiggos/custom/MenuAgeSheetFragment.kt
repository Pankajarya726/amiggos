package com.tekzee.amiggos.custom

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.calendarview.CalendarViewActivity
import com.tekzee.amiggos.util.SharedPreference

class MenuAgeSheetFragment : BottomSheetDialogFragment() {

    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.age_menu_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        contentView.findViewById<Button>(R.id.cancel).text = languageData!!.klCancel
        contentView.findViewById<TextView>(R.id.txt_upload).text = languageData!!.upload
        contentView.findViewById<TextView>(R.id.txt_title).text = languageData!!.agerestrictionmessage
        contentView.findViewById<Button>(R.id.cancel).setOnClickListener {
            dialog.dismiss()

        }
        contentView.findViewById<View>(R.id.txt_upload).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(context,AttachIdActivity::class.java)
            requireContext().startActivity(intent)
        }

    }

    companion object {
        var venueid = ""
        fun newInstance(): MenuAgeSheetFragment {
            return MenuAgeSheetFragment()
        }
    }
}