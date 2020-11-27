package com.tekzee.amiggos.ui.stripepayment.addnewcard

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.braintreepayments.cardform.view.CardForm
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.AAddcardActivityBinding
import com.tekzee.amiggos.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib

class AAddCard : BaseActivity(), AAddCardPresenter.AAddCardPresenterMainView {

    private var binding: AAddcardActivityBinding? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var implementation: AAddCardImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_addcard_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        implementation = AAddCardImplementation(this, this)
        setupLanguage()
        setupClickListener()
    }

    private fun setupLanguage() {
        binding!!.savecard.text = languageData!!.klSAVE
        binding!!.heading.text = languageData!!.carddetails
    }


    private fun setupClickListener() {

        binding!!.imgClose.setOnClickListener {
            onBackPressed()
        }


        binding!!.cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .cardholderName(CardForm.FIELD_REQUIRED)
            .postalCodeRequired(false)
            .mobileNumberRequired(false)
            .actionLabel("Done")
            .maskCvv(true)
            .setup(this)


        binding!!.savecard.setOnClickListener {
            if(binding!!.cardForm.isValid){

                val card: Card = Card.create(
                    binding!!.cardForm.cardNumber,
                    binding!!.cardForm.expirationMonth.toInt(),
                    binding!!.cardForm.expirationYear.toInt(),
                    binding!!.cardForm.cvv
                )


                if (card.validateCard()) {
                    showProgressbar()
                    val stripe = Stripe(this, BuildConfig.STRIPE_URL)
                    stripe.createCardToken(card,sharedPreferences!!.getValueInt(ConstantLib.USER_ID).toString()+System.currentTimeMillis(),object : ApiResultCallback<Token>{
                        override fun onError(error: Exception) {
                            hideProgressbar()
                            error.printStackTrace()
                            Toast.makeText(
                                applicationContext,
                                error.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onSuccess(result: Token) {
                            hideProgressbar()
                            Logger.d("Stripe Token : " + result.id)
                            val jsonObject = JsonObject()
                            jsonObject.addProperty(
                                "userid",
                                sharedPreferences!!.getValueInt(ConstantLib.USER_ID).toString()
                            )
                            jsonObject.addProperty(
                                "name",
                                sharedPreferences!!.getValueString(ConstantLib.USER_NAME).toString()
                            )
                            jsonObject.addProperty(
                                "email",
                                sharedPreferences!!.getValueString(ConstantLib.USER_EMAIL)
                                    .toString()
                            )
                            jsonObject.addProperty("source_token", result.id)
                            jsonObject.addProperty("user_type", sharedPreferences!!.getValueString(ConstantLib.USER_TYPE))
                            saveCardInfo(jsonObject)
                        }

                    })
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Invalid Card Data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                binding!!.cardForm.validate()
            }

        }

    }

    private fun saveCardInfo(jsonObject: JsonObject) {
        implementation!!.saveCard(
            jsonObject,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun onChooseWeekSuccess(responseData: ChooseWeekResponse?) {
        TODO("Not yet implemented")
    }

    override fun onSaveCardSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onSaveCardFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    fun callGetCardList() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", intent.getStringExtra(ConstantLib.CLUB_ID))
        implementation!!.getCardList(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }


}