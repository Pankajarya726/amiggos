package com.tekzee.amiggoss.ui.signup.stepone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.StepOneBinding
import com.tekzee.amiggoss.ui.settings.model.SettingsResponse
import com.tekzee.amiggoss.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggoss.ui.signup.StepOneModel
import com.tekzee.amiggoss.ui.signup.steptwo.StepTwo
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import java.util.*


class StepOne: BaseActivity(), StepOnePresenter.StepOnePresenterMainView,
    DatePickerDialog.OnDateSetListener {

    private var dateOfBirth: String?=""
    private var binding: StepOneBinding? =null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.step_one)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupLanguage()
        setupClickListener()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickListener() {
        binding!!.btnCancelStepOne.setOnClickListener {
            onBackPressed()
        }

        binding!!.btnNextStepOne.setOnClickListener {
            if(validateFields()){
                val stepOneModel = StepOneModel(binding!!.semail.text.toString().trim(),binding!!.spassword.text.toString().trim(),binding!!.susername.text.toString().trim(),dateOfBirth)
                val intent = Intent(applicationContext,StepTwo::class.java)
                intent.putExtra("steponedata",stepOneModel)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
        }

        binding!!.sdateOfBirth.setOnTouchListener { v: View, event: MotionEvent ->
           if(event.action == MotionEvent.ACTION_UP){

               val todaysDate = Calendar.getInstance()
               todaysDate.add(Calendar.YEAR,-18)
               SpinnerDatePickerDialogBuilder()
                   .context(this)
                   .callback(this)
                   .showTitle(true)
                   .showDaySpinner(true)
                   .spinnerTheme(R.style.NumberPickerStyle)
                   .defaultDate(todaysDate.get(Calendar.YEAR), todaysDate.get(Calendar.MONTH), todaysDate.get(Calendar.DAY_OF_MONTH))
                   .maxDate(todaysDate.get(Calendar.YEAR), todaysDate.get(Calendar.MONTH), todaysDate.get(Calendar.DAY_OF_MONTH))
                   .minDate(1900, 0, 1)
                   .build()
                   .show()

           }
            true
        }
    }

    private fun validateFields(): Boolean {
        if(binding!!.semail.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Email can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(!Utility.isEmailValid(binding!!.semail.text.toString().trim())){
            Toast.makeText(applicationContext,"Please provide valid email..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.spassword.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Password can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.sconfirmpassword.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Confirm password can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.spassword.text.toString().trim()!= binding!!.sconfirmpassword.text.toString().trim()){
            Toast.makeText(applicationContext,"Password and Confirm password should be same..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.susername.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Username can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.sdateOfBirth.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Date of birth can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun setupLanguage() {
        binding!!.semail.hint = languageData!!.PEMAIL
        binding!!.spassword.hint = languageData!!.PPASSWORD
        binding!!.sconfirmpassword.hint = languageData!!.PCONFIRMPASSWORD
        binding!!.susername.hint = languageData!!.PUSERNAME
        binding!!.sdateOfBirth.hint = languageData!!.PDATEOFBIRTH
        binding!!.btnNextStepOne.text = languageData!!.PNEXT
        binding!!.btnCancelStepOne.text = languageData!!.PCANCEL
        binding!!.txtStepOne.text = languageData!!.PSTEPONE
    }

    override fun onSettingsSuccess(responseData: SettingsResponse?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpdateSettingsSuccess(responseData: UpdateSettingsResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var month = monthOfYear + 1
        binding!!.sdateOfBirth.setText("$dayOfMonth-$month-$year")
        dateOfBirth = "$year-$month-$dayOfMonth"
    }
}