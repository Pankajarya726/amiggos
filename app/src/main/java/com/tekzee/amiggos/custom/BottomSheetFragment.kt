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
import com.tekzee.amiggos.ui.calendarview.CalendarViewActivity
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import com.tekzee.amiggos.util.SharedPreference

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.customdinein_dialog, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        contentView.findViewById<Button>(R.id.cancel).text = languageData!!.klCancel
        contentView.findViewById<TextView>(R.id.txt_dinin).text = languageData!!.dining
        contentView.findViewById<TextView>(R.id.txt_togo).text = languageData!!.togo
        contentView.findViewById<Button>(R.id.cancel).setOnClickListener {
            dialog.dismiss()

        }
        contentView.findViewById<View>(R.id.togo).setOnClickListener {
            dialog.dismiss()
            sharedPreference!!.save(ConstantLib.SELECTED_VENUE_DIN_TOGO,ConstantLib.TOGO)
            val intent = Intent(requireContext(), CalendarViewActivity::class.java)
            intent.putExtra(ConstantLib.VENUE_ID, venueid)
            intent.putExtra(ConstantLib.CALENDAR_DATA, responseData)
            intent.putExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO, ConstantLib.TOGOTEXT)
            startActivity(intent)
        }
        contentView.findViewById<View>(R.id.dinin).setOnClickListener { dialog.dismiss()
            sharedPreference!!.save(ConstantLib.SELECTED_VENUE_DIN_TOGO,ConstantLib.RESERVATION)
            val intent = Intent(requireContext(), CalendarViewActivity::class.java)
            intent.putExtra(ConstantLib.CALENDAR_DATA, responseData)
            intent.putExtra(ConstantLib.VENUE_ID, venueid)
            intent.putExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO, ConstantLib.DINEIN)
            startActivity(intent)}
    }

    companion object {
        var venueid = ""
        var responseData:VenueDetails.Data? = null
        fun newInstance(clubId: Int, response: VenueDetails.Data): BottomSheetFragment {
            venueid = clubId.toString()
            responseData = response
            return BottomSheetFragment()
        }
    }
}