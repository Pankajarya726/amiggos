package com.tekzee.amiggos.stripe

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.stripe.android.view.CardInputWidget
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ContentCheckoutBinding
import com.tekzee.amiggos.stripe.model.ClientSecretResponse
import com.tekzee.amiggos.stripe.model.PaymentResponse
import com.tekzee.amiggos.ui.friendinviteconfirmation.FriendInviteConfirmation
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import java.lang.ref.WeakReference


class CheckoutActivity : BaseActivity() , CheckOutActivityPresenter.CheckOutActivityPresenterMainView{
    private var paymentIntentClientSecret: String?=""
    private var publishableKey: String?=""
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var binding: ContentCheckoutBinding? =null
    private lateinit var stripe: Stripe
    private lateinit var checkOutActivityImplementation: CheckOutActivityImplementation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.content_checkout)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        checkOutActivityImplementation = CheckOutActivityImplementation(this,this)
        setupUi()
        setupToolBar()
        setupClickListener()
        startCheckout()
    }

    private fun setupUi() {
        binding!!.txtAmount.text = "Total : $"+intent.getStringExtra(ConstantLib.BOOKING_AMOUNt)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding!!.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }


    private fun setupClickListener() {
        binding!!.payButton.setOnClickListener {
            showProgressbar()
            val cardInputWidget =
                findViewById<CardInputWidget>(R.id.cardInputWidget)
            cardInputWidget.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret!!)
                stripe.confirmPayment(this, confirmParams)
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun startCheckout() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("currency","usd")
        jsonObject.addProperty("booking_id",intent.getStringExtra(ConstantLib.BOOKING_ID))
        jsonObject.addProperty("userid",sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        checkOutActivityImplementation.getPaymentIntentClientSecret(jsonObject, Utility.createHeaders(sharedPreferences))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weakActivity = WeakReference<Activity>(this)

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    hideProgressbar()
                    val gson = GsonBuilder().setPrettyPrinting().create().toJson(paymentIntent)
                    val paymentResponse = Gson().fromJson(gson,PaymentResponse::class.java)
                    callUpdateStatusApi(paymentResponse)
                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    hideProgressbar()
                    weakActivity.get()?.let { activity ->
                        displayAlert(
                            activity,
                            "Payment failed",
                            paymentIntent.lastPaymentError?.message.orEmpty()
                        )
                    }
                }
            }

            override fun onError(e: Exception) {
                hideProgressbar()
                weakActivity.get()?.let { activity ->
                    displayAlert(
                        activity,
                        "Payment failed",
                        e.toString()
                    )
                }
            }
        })
    }

    private fun callUpdateStatusApi(paymentResponse: PaymentResponse) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("transaction_id",paymentResponse.id)
        jsonObject.addProperty("booking_id",intent.getStringExtra(ConstantLib.BOOKING_ID))
        jsonObject.addProperty("userid",sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        checkOutActivityImplementation.updatePaymentStatus(jsonObject, Utility.createHeaders(sharedPreferences))
    }

    override fun onPaymentIntentclientSuccess(responseData: ClientSecretResponse?) {
        paymentIntentClientSecret = responseData!!.data.clientSecret
        publishableKey = responseData.data.publishableKey
        stripe = Stripe(applicationContext, publishableKey!!)
    }

    override fun onPaymentIntentclientFailure(responseData: String) {
        displayAlert(
            this,
            "Payment failed",
            "Payment failed"
        )
    }

    override fun onUpdatePaymentStatus(responseData: CommonResponse) {
        val intentFriendInvite = Intent(this, FriendInviteConfirmation::class.java )
        intentFriendInvite.putExtra(ConstantLib.BOOKING_ID,intent.getStringExtra(ConstantLib.BOOKING_ID))
        intentFriendInvite.putExtra(ConstantLib.FROM,"CHECKOUTACTIVITY")
        startActivity(intentFriendInvite)
    }

    override fun onUpdatePaymentFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }


    private fun displayAlert(activity: Activity?, title: String, message: String, restartDemo: Boolean = false) {
        if (activity == null) {
            return
        }
        runOnUiThread {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(title)
            builder.setMessage(message)
            if (restartDemo) {
                builder.setPositiveButton("Restart demo") { _, _ ->
                    val cardInputWidget =
                        findViewById<CardInputWidget>(R.id.cardInputWidget)
                    cardInputWidget.clear()
                    startCheckout()
                }
            }
            else {
                builder.setPositiveButton("Ok", null)
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}