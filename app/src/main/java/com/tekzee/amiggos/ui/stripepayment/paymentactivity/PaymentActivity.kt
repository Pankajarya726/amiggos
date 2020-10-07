package com.tekzee.amiggos.ui.stripepayment.paymentactivity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.braintreepayments.cardform.view.ErrorEditText
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.PaymentActivityBinding
import com.tekzee.amiggos.room.database.AmiggoRoomDatabase
import com.tekzee.amiggos.ui.friendinviteconfirmation.FriendInviteConfirmation
import com.tekzee.amiggos.ui.invitefriendnew.InviteFriendNewActivity
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.adapter.PaymentActivityAdapter
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.BookingPaymentResponse
import com.tekzee.amiggos.util.*
import java.util.*

class PaymentActivity : BaseActivity(), PaymentActivityPresenter.APaymentMethodPresenterMainView {

    private var customStripeId: String? = ""
    private var adapter: PaymentActivityAdapter? = null
    private var binding: PaymentActivityBinding? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aPaymentMehtodImplementation: PaymentActivityImplementation? = null
    private val data = ArrayList<CardListResponse.Data.Card>()
    private var repository: ItemRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.payment_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aPaymentMehtodImplementation = PaymentActivityImplementation(this, this)
        setupToolBar()
        setupRepository()
        setupClickListener()
        setupLanguage()
        setUpRecyclerView()

    }

    private fun setupRepository() {
        val itemDao = AmiggoRoomDatabase.getDatabase(this).itemDao()
        repository = ItemRepository(itemDao)
    }

    private fun setupLanguage() {
        binding!!.paynow.text = languageData!!.pay
        binding!!.headertitle.text = languageData!!.ppaymentmethod
        binding!!.headerAmount.text = "$ " + intent.getStringExtra(ConstantLib.PURCHASE_AMOUNT)
        binding!!.savedCards.text = languageData!!.savedcards
    }

    override fun onResume() {
        super.onResume()
        data.clear()
        adapter!!.notifyDataSetChanged()
        callGetCardList()
    }


    private fun setUpRecyclerView() {
        binding!!.cardRecyclerview.setHasFixedSize(true)
        binding!!.cardRecyclerview.setLayoutManager(
            LinearLayoutManager(
                this
            )
        )
        adapter = PaymentActivityAdapter(
            data, intent.getStringExtra(ConstantLib.FROM)
        ) { model, position ->
            selectCard(position)
            binding!!.paynow.visibility = View.VISIBLE
        }
        binding!!.cardRecyclerview.adapter = adapter

    }

    private fun selectCard(position: Int) {

        for(i in 0 until data.size){
            data[i].isSelected = i==position
        }
        adapter!!.notifyDataSetChanged()
    }


    fun createBookingPayment() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
       aPaymentMehtodImplementation!!.createBookingPayment(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
    }

    private fun setupClickListener() {
        binding!!.paynow.setOnClickListener {
            if(intent.getStringExtra(ConstantLib.PURCHASE_AMOUNT).toFloat()>0){
                setDefaultCard()
            }else{
                Errortoast("Amount can not be zero")
            }
        }
    }

    private fun setDefaultCard() {
        val input: JsonObject = JsonObject()
        val cardSelected = getSelectedCard()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("strip_customerId", customStripeId)
        input.addProperty("card_Id", cardSelected.id)
        aPaymentMehtodImplementation!!.setDefaultCard(
                  input,
                  Utility.createHeaders(sharedPreferences)
              )

    }

    private fun getSelectedCard(): CardListResponse.Data.Card {
        var cardSeleted: CardListResponse.Data.Card? = null
        for(i in 0 until data.size){
            if(data[i].isSelected){
                cardSeleted = data[i]
            }
        }
       return cardSeleted!!
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding!!.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
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
        input.addProperty("user_type", sharedPreferences!!.getValueString(ConstantLib.USER_TYPE))
        aPaymentMehtodImplementation!!.getCardList(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun onCardListSuccess(
        responseData: List<CardListResponse.Data.Card>,
        customerStripId: String
    ) {
        customStripeId = customerStripId;
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    override fun onBookingSuccess(response: BookingPaymentResponse?) {
        Successtoast(response!!.message)
        Coroutines.main {
            repository!!.clearCart()
        }
        val intent = Intent(applicationContext, InviteFriendNewActivity::class.java)
        intent.putExtra(ConstantLib.MESSAGE,response.message)
        intent.putExtra(ConstantLib.FROM,ConstantLib.FINALBASKET)
        intent.putExtra(ConstantLib.BOOKING_ID,getIntent().getStringExtra(ConstantLib.BOOKING_ID))
        startActivity(intent)
    }

    override fun onBookingFailure(message: String) {
       Errortoast(message)
    }

    override fun onSetDefaultCardSuccess(message: String) {
       // Successtoast(message)
        createBookingPayment()
    }

    override fun onSetDefaultCardFailure(message: String) {
        Errortoast(message)
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}