package com.tekzee.amiggos.custom

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.finalbasket.FinalBasketActivity
import com.tekzee.amiggos.util.SharedPreference

class TipSheetFragment : BottomSheetDialogFragment() {

    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.tip_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        contentView.findViewById<Button>(R.id.cancel).text = languageData!!.klCancel
        contentView.findViewById<EditText>(R.id.edt_tip).hint = languageData!!.tip
        contentView.findViewById<TextView>(R.id.txt_title).text = languageData!!.tip
        contentView.findViewById<TextView>(R.id.btn_done).text = languageData!!.klDone
        contentView.findViewById<Button>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        contentView.findViewById<View>(R.id.btn_done).setOnClickListener {
            dialog.dismiss()
            val tip = contentView.findViewById<EditText>(R.id.edt_tip).text.toString()
            if(tip.isEmpty()){
                listener.onTipPicked("")
            }else{
                listener.onTipPicked(tip)
            }

        }

    }

    companion object {
        private lateinit var listener: FinalBasketActivity.onTipListener
        fun newInstance(mlistener: FinalBasketActivity.onTipListener): TipSheetFragment {
            listener = mlistener
            return TipSheetFragment()
        }
    }

}