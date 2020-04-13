package com.tekzee.amiggos.stripe

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import com.stripe.android.view.CardValidCallback
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ContentCheckoutBinding
import com.tekzee.amiggos.stripe.adapter.CheckoutPaymentAdapter
import com.tekzee.amiggos.stripe.model.APaymentSuccessResponse
import com.tekzee.amiggos.ui.friendinviteconfirmation.FriendInviteConfirmation
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import java.util.ArrayList


class CheckoutActivity : BaseActivity(),
    CheckOutActivityPresenter.CheckOutActivityPresenterMainView, CardValidCallback {
    private var isCardValid: Boolean = false
    private var customerStripIdData: String =""
    private var cardId: String=""
    private var type:Int =0
    private var paymentIntentClientSecret: String? = ""
    private var publishableKey: String? = ""
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var binding: ContentCheckoutBinding? = null
    private lateinit var stripe: Stripe
    private lateinit var checkOutActivityImplementation: CheckOutActivityImplementation
    private var adapter: CheckoutPaymentAdapter? = null
    private val data = ArrayList<CardListResponse.Data.Card>()
    private var selectedPosition = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.content_checkout)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        checkOutActivityImplementation = CheckOutActivityImplementation(this, this)
        setupUi()
        setupToolBar()
        setupClickListener()
        setUpRecyclerView()
        callGetCardList()
    }

    fun callGetCardList() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        checkOutActivityImplementation.getCardList(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }


    override fun onCardListSuccess(
        cards: List<CardListResponse.Data.Card>,
        customerStripId: String
    ) {
        customerStripIdData = customerStripId
        if (cards.isEmpty()) {
            binding!!.cardlayout.visibility = View.GONE
        } else {
            binding!!.cardlayout.visibility = View.VISIBLE
        }
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(cards)
        adapter!!.notifyDataSetChanged()
    }


    private fun setUpRecyclerView() {
        binding!!.cardRecyclerview.setHasFixedSize(true)
        binding!!.cardRecyclerview.setLayoutManager(
            LinearLayoutManager(
                this
            )
        )
        adapter = CheckoutPaymentAdapter(data, object : CheckoutPaymentClick {
            override fun onRowClick(
                model: CardListResponse.Data.Card,
                position: Int
            ) {
//                if (position != selectedPosition) {
//                    data.get(position).isSelected = true
//                    if (selectedPosition != -1)
//                        data.get(selectedPosition).isSelected = false
//                    adapter!!.notifyDataSetChanged()
//                    selectedPosition = position
//
//                }
                showPopuDialogForPayment(model)


                //                val intent = Intent(
//                    this,
//                    CardDetailsActivity::class.java
//                )
//                intent.putExtra("id", model.getId())
//                intent.putExtra("Card_Type", model.getBrand())
//                intent.putExtra("isDefault", model.getIsDefault())
//                intent.putExtra("Card_Number", model.getLast4())
//                intent.putExtra("Expiry_Month", model.getExpiryMonth())
//                intent.putExtra("Finger_Print", model.getFingerPrint())
//                intent.putExtra("Expiry_Year", model.getExpiryYear())
//                startActivity(intent)


            }

        })
        binding!!.cardRecyclerview.adapter = adapter

    }

    private fun showPopuDialogForPayment(model: CardListResponse.Data.Card) {
        val pDialog =
            SweetAlertDialog(this@CheckoutActivity, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = "Are you sure you want to use this card."
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            callPaymentByCardId(model.id)
        }
        pDialog.show()
    }

    private fun callPaymentByCardId(id: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("card_id", id)
        input.addProperty("customer_stripe_id", customerStripIdData)
        input.addProperty("booking_id",  intent.getStringExtra(ConstantLib.BOOKING_ID))
        checkOutActivityImplementation.callPaymentByCardId(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    private fun setupUi() {
        binding!!.txtAmount.text = "Total : $" + intent.getStringExtra(ConstantLib.BOOKING_AMOUNt)
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
            val cardInputWidget =
                findViewById<CardInputWidget>(R.id.cardInputWidget)
            binding!!.cardInputWidget.setCardValidCallback(this)
            if (!isCardValid) {
                Toast.makeText(
                    applicationContext,
                    "Please enter valid card details ",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                showProgressbar()
                val stripe = Stripe(this, BuildConfig.STRIPE_URL)
                stripe.createCardToken(
                    cardInputWidget.card!!,
                    sharedPreferences!!.getValueInt(ConstantLib.USER_ID)
                        .toString() + System.currentTimeMillis(),
                    object : ApiResultCallback<Token> {
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
                                "stripeToken",
                                result.id
                            )
                            jsonObject.addProperty(
                                "booking_id",
                                intent.getStringExtra(ConstantLib.BOOKING_ID)
                            )

                            chargeUser(jsonObject)
                        }

                    })


            }

        }
    }

    private fun chargeUser(jsonObject: JsonObject) {
        checkOutActivityImplementation.chargeUser(
            jsonObject,
            Utility.createHeaders(sharedPreferences)
        )
    }



    override fun onUpdatePaymentStatus(responseData: CommonResponse) {
        val intentFriendInvite = Intent(this, FriendInviteConfirmation::class.java)
        intentFriendInvite.putExtra(
            ConstantLib.BOOKING_ID,
            intent.getStringExtra(ConstantLib.BOOKING_ID)
        )
        intentFriendInvite.putExtra(ConstantLib.FROM, "CHECKOUTACTIVITY")
        startActivity(intentFriendInvite)
    }

    override fun onUpdatePaymentFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    override fun onChargeSuccess(responseData: APaymentSuccessResponse?) {
        val intentFriendInvite = Intent(this, FriendInviteConfirmation::class.java)
        intentFriendInvite.putExtra(
            ConstantLib.BOOKING_ID,
            responseData!!.data.bookingId
        )
        intentFriendInvite.putExtra(ConstantLib.FROM, "CHECKOUTACTIVITY")
        startActivity(intentFriendInvite)

    }

    override fun onChargeFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


//    private fun displayAlert(
//        activity: Activity?,
//        title: String,
//        message: String,
//        restartDemo: Boolean = false
//    ) {
//        if (activity == null) {
//            return
//        }
//        runOnUiThread {
//            val builder = AlertDialog.Builder(activity)
//            builder.setTitle(title)
//            builder.setMessage(message)
//            if (restartDemo) {
//                builder.setPositiveButton("Restart demo") { _, _ ->
//                    val cardInputWidget =
//                        findViewById<CardInputWidget>(R.id.cardInputWidget)
//                    cardInputWidget.clear()
//                    startCheckout()
//                }
//            } else {
//                builder.setPositiveButton("Ok", null)
//            }
//            val dialog = builder.create()
//            dialog.show()
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onInputChanged(isValid: Boolean, invalidFields: Set<CardValidCallback.Fields>) {
        isCardValid = isValid
    }
}