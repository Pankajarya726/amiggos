package com.tekzee.amiggos.ui.agreement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.LicenceAgreementActvitiyBinding
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib

class LicenceAgreementActivity:BaseActivity() {

    lateinit var binding: LicenceAgreementActvitiyBinding
    private var languageData: LanguageData? = null
    private var sharedPreferences: SharedPreference? = null
    private var checkBoxOpenMided: Boolean = false
    private var checkBoxTurnUp: Boolean = false
    private var checkBoxSpread: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.licence_agreement_actvitiy)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupViewData()
        setupClickListener()

    }

    private fun setupClickListener() {

        binding.layoutOpenMinded.setOnClickListener{
            binding.checkboxOpenMinded.isChecked = !binding.checkboxOpenMinded.isChecked
            sharedPreferences!!.save(ConstantLib.OPENMIDED_CHECKBOX,binding.checkboxOpenMinded.isChecked)
            allCheckBoxChecked()
        }
        binding.layoutSpread.setOnClickListener{
            binding.checkboxSpread.isChecked = !binding.checkboxSpread.isChecked
            sharedPreferences!!.save(ConstantLib.SPREAD_CHECKBOX,binding.checkboxSpread.isChecked)
            allCheckBoxChecked()
        }
        binding.layoutTurnUp.setOnClickListener{
            binding.checkboxTurnUp.isChecked = !binding.checkboxTurnUp.isChecked
            sharedPreferences!!.save(ConstantLib.TURNTUP_CHECKBOX,binding.checkboxTurnUp.isChecked)
            allCheckBoxChecked()
        }



    }

    fun allCheckBoxChecked(){
        if(binding.checkboxOpenMinded.isChecked && binding.checkboxSpread.isChecked && binding.checkboxTurnUp.isChecked){
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewData() {
        binding.txtOpenMindedTitle.text = languageData!!.klTermsTitle1
        binding.txtTurntUpTitle.text = languageData!!.klTermsTitle2
        binding.txtSpreadTitle.text = languageData!!.klTermsTitle3

        binding.txtOpenMindedDescription.text = languageData!!.klTermssubtitle1
        binding.txtTurntUpDescription.text = languageData!!.klTermssubtitle2
        binding.txtSpreadDescription.text = languageData!!.klTermssubtitle3

        binding.maintitle.text = languageData!!.klTermsHeading1
        binding.mainsubtitle.text = languageData!!.klTermsHeading2


        binding.checkboxOpenMinded.isChecked = sharedPreferences!!.getValueBoolean(ConstantLib.OPENMIDED_CHECKBOX,false)
        binding.checkboxTurnUp.isChecked = sharedPreferences!!.getValueBoolean(ConstantLib.TURNTUP_CHECKBOX,false)
        binding.checkboxSpread.isChecked = sharedPreferences!!.getValueBoolean(ConstantLib.SPREAD_CHECKBOX,false)


    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klAgree

    }


    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(ConstantLib.LICENCE_AGREEMENT_COMPLETED,getLicencAgreement())
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    private fun getLicencAgreement(): Boolean? {
        return binding.checkboxOpenMinded.isChecked && binding.checkboxTurnUp.isChecked && binding.checkboxSpread.isChecked
    }
}