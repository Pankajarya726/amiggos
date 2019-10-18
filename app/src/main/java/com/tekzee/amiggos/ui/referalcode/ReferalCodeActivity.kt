package com.tekzee.amiggos.ui.referalcode

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ReferalActivityBinding
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggos.ui.referalcode.model.VenueResponse
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib

class ReferalCodeActivity:BaseActivity(), ReferalCodePresenter.ReferalCodeMainView {

    lateinit var binding: ReferalActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private var referalCodePresenterImplementation:ReferalCodePresenterImplementation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.referal_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        sharedPreference = SharedPreference(this)
        referalCodePresenterImplementation = ReferalCodePresenterImplementation(this,this)
        setupViewData()
        setupClickListerner()

    }

    private fun setupClickListerner() {
        binding.txtDontHaveReferalCode.setOnClickListener{
            val intent = Intent(this,AttachIdActivity::class.java)
            intent.putExtra(ConstantLib.FROM,"")
            startActivity(intent)
        }

        binding.btnSubmitCode.setOnClickListener{
           if(binding.edtReferalCode.text.toString().trim().isEmpty()){
               Toast.makeText(applicationContext,"Referal code can not be blank",Toast.LENGTH_LONG).show()
           }else{
               val input: JsonObject = JsonObject()
               input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
               input.addProperty("referral_code", binding.edtReferalCode.text.toString().trim())
               referalCodePresenterImplementation!!.doCallReferalApi(input, Utility.createHeaders(sharedPreference))
           }
        }

        binding.checkBoxVenue.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
            val input: JsonObject = JsonObject()
            input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
            input.addProperty("is_checked", b)
            referalCodePresenterImplementation!!.doCallCheckVenueApi(input, Utility.createHeaders(sharedPreference))
        }

    }

    private fun setupViewData() {
        binding.edtReferalCode.hint = languageData!!.klEnterRefferal
        binding.btnSubmitCode.text = languageData!!.klSubmitReffralCode
        binding.txtReferalTitle.text = languageData!!.klRefferalTitle
        binding.txtDontHaveReferalCode.text = languageData!!.klDonthaveRefCode
        binding.checkBoxVenue.text = languageData!!.kllblIamVenue
    }


    override fun validateError(message: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(message)
            .setConfirmText(languageData!!.klOk)
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .show()
    }

    override fun onCallReferalApiSuccess(responseData: ReferalCodeResponse?) {
        val intent = Intent(this,AttachIdActivity::class.java)
        intent.putExtra(ConstantLib.FROM,"")
        startActivity(intent)
    }

    override fun onCheckVenueSuccess(responseData: VenueResponse?) {
       Logger.d(responseData)
    }



}