package com.tekzee.amiggos.ui.finalbasket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.impulsiveweb.galleryview.GalleryView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.MenuAgeSheetFragment
import com.tekzee.amiggos.custom.TipSheetFragment
import com.tekzee.amiggos.databinding.FinalCartBinding
import com.tekzee.amiggos.room.database.AmiggoRoomDatabase
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.finalbasket.adapter.FinalBasketAdapter
import com.tekzee.amiggos.ui.finalbasket.model.CreateBookingResponse
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.PaymentActivity
import com.tekzee.amiggos.util.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat


class FinalBasketActivity : BaseActivity(), FinalBasketEvent, KodeinAware,
    FinalBasketClickListener {

    private lateinit var taxinfo: MenuResponse.Tax
    private lateinit var newlist: java.util.ArrayList<Menu>
    private lateinit var adapter: FinalBasketAdapter
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: FinalBasketViewModelFactory by instance<FinalBasketViewModelFactory>()
    private var binding: FinalCartBinding? = null
    private lateinit var viewModel: FinalBasketViewModel
    private var repository: ItemRepository? = null
    var totalamout = 0.0f
    var subtotalamout = 0.0f
    var tax = 0.0f
    var mtip = 0.0f
    private val df2: DecimalFormat = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.final_cart)
        viewModel = ViewModelProvider(this, factory).get(FinalBasketViewModel::class.java)
        viewModel.finalBasketEvent = this
        taxinfo = intent.getSerializableExtra(ConstantLib.TAX) as MenuResponse.Tax
        setupClickListener()
        setupLanguage()

        val itemDao = AmiggoRoomDatabase.getDatabase(this).itemDao()
        repository = ItemRepository(itemDao)
        setupAdapter()
    }


    override fun onResume() {
        super.onResume()
        getCartItems(0.0f)
    }

    override fun validateError(message: String) {
        Errortoast(message)
    }

    private fun getCartItems(tip: Float) {
        Coroutines.main {
            repository!!.getItemCount(intent.getStringExtra(ConstantLib.VENUE_ID)!!).observe(
                this,
                Observer {
                    Log.e("item list count-->", it.size.toString())
                    newlist = ArrayList<Menu>()
                    newlist.addAll(it)
                    adapter.submitList(newlist)
                    binding!!.progressCircular.visibility = View.GONE
                    binding!!.recyclerCommon.visibility = View.VISIBLE
                    calcualteAmount(tip, it)
                })
        }
    }

    private fun setupLanguage() {
        binding!!.headertitle.text = languageConstant.checkout
        binding!!.title.text = languageConstant.itemordered
        binding!!.headingSubtotal.text = languageConstant.subtotal
        binding!!.headingFee.text =
            languageConstant.feeandestimatedtax + " @ " + taxinfo.value + "%"
        binding!!.headingTip.text = languageConstant.tip
        binding!!.headingTotal.text = languageConstant.total
        binding!!.specialInstructions.hint = languageConstant.specialinstruction

        binding!!.specialInstructions.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding!!.specialInstructions.setImeActionLabel(
            resources.getString(R.string.done),
            EditorInfo.IME_ACTION_DONE
        )
        binding!!.specialInstructions.setImeOptions(EditorInfo.IME_ACTION_DONE)

        binding!!.specialInstructions.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // Capture soft enters in a singleLine EditText that is the last EditText
                        // This one is useful for the new list case, when there are no existing ListItems
                        binding!!.specialInstructions.clearFocus()
                        val inputMethodManager: InputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(
                            v.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN
                        )
                    } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        // Capture soft enters in other singleLine EditTexts
                    } else if (actionId == EditorInfo.IME_ACTION_GO) {
                    } else {
                        // Let the system handle all other null KeyEvents
                        return false
                    }
                } else if (actionId == EditorInfo.IME_NULL) {
                    // Capture most soft enters in multi-line EditTexts and all hard enters;
                    // They supply a zero actionId and a valid keyEvent rather than
                    // a non-zero actionId and a null event like the previous cases.
                    if (event.getAction() === KeyEvent.ACTION_DOWN) {
                        // We capture the event when the key is first pressed.
                    } else {
                        // We consume the event when the key is released.
                        return true
                    }
                } else {
                    // We let the system handle it when the listener is triggered by something that
                    // wasn't an enter.
                    return false
                }
                return true
            }
        })

    }

    private fun setupClickListener() {
        binding!!.icBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.tiplayout.setOnClickListener {
            showCustomTipDialog(it)
        }

        binding!!.payButton.setOnClickListener {
            if (totalamout > 0) {
                callCreateBookingApi()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Total amount can not be zero",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun callCreateBookingApi() {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID))
        input.addProperty("venue_id", intent.getStringExtra(ConstantLib.VENUE_ID))
        input.addProperty("sub_total", subtotalamout)
        input.addProperty("booking_date", intent.getStringExtra(ConstantLib.DATE))
        input.addProperty(
            "booking_method",
            intent.getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
        )
        input.addProperty("booking_time", intent.getStringExtra(ConstantLib.TIME))
        input.addProperty("instruction", binding!!.specialInstructions.text.toString())
        input.add("menu_item", createMenuItems())
        input.addProperty("price_category", "$$$")
        input.addProperty("tax", tax)
        input.addProperty("tip", mtip)
        input.addProperty("total_amount", totalamout)
        viewModel.callCreateBookingApi(input)
    }

    private fun createMenuItems(): JsonArray {
        val jsonArray = JsonArray()
        for (item in newlist) {
            val jsonobject = JsonObject()
            jsonobject.addProperty("item_id", item.id)
            jsonobject.addProperty("name", item.name)
            jsonobject.addProperty("price", item.price)
            jsonobject.addProperty("qty", item.quantity)
            jsonArray.add(jsonobject)
        }
        return jsonArray
    }

    fun showCustomTipDialog(view: View) {
        val bottomSheetDialog: TipSheetFragment = TipSheetFragment.newInstance(object :
            onTipListener {
            override fun onTipPicked(tipamout: String) {
                if (tipamout.isEmpty()) {
                    binding!!.imgAddTip.visibility = View.VISIBLE
                    binding!!.tipamout.visibility = View.GONE
                    binding!!.tipamout.visibility = View.GONE
                } else {
                    getCartItems(tipamout.toFloat())
                    binding!!.imgAddTip.visibility = View.GONE
                    binding!!.tipamout.visibility = View.VISIBLE
                    binding!!.tipAmountLayout.visibility = View.VISIBLE
                }

            }

        })
        bottomSheetDialog.show(supportFragmentManager, "tip Dialog Fragment")
    }

    private fun setupAdapter() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.recyclerCommon.visibility = View.GONE
        adapter = FinalBasketAdapter(this, repository, prefs)
        binding!!.recyclerCommon.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.recyclerCommon.adapter = adapter
    }


    override fun onItemClicked(position: Int, listItem: Menu, quantity: String) {
        Coroutines.main {
            repository!!.deleteitem(listItem.id)
        }

    }

    @SuppressLint("LongLogTag")
    private fun calcualteAmount(tip: Float, list: List<Menu>) {
        totalamout = 0.0f
        subtotalamout = 0.0f
        tax = 0.0f
        Coroutines.main {
            for (item in list) {
                subtotalamout += (item.price.toFloat() * item.quantity)
            }
            tax = ((subtotalamout * taxinfo.value.toFloat()) / 100)
            mtip = tip
            totalamout = subtotalamout + tax + mtip

            binding!!.txtFee.text = "$ " + df2.format(Utility.roundOffDecimal(tax.toDouble()))
            binding!!.txtSubtotal.text =
                "$ " + df2.format(Utility.roundOffDecimal(subtotalamout.toDouble()))
            binding!!.txtTotal.text =
                "$ " + df2.format(Utility.roundOffDecimal(totalamout.toDouble()))
            binding!!.tipamout.text = "$ " + df2.format(Utility.roundOffDecimal(tip.toDouble()))
            binding!!.txtTip.text = "$ " + df2.format(Utility.roundOffDecimal(tip.toDouble()))

        }

    }


    override fun showAgeRestrictionPopup(view: View) {
        showCustomDialog(view)
    }

    override fun viewImage(listItem: Menu?) {
        if (listItem!!.menuImage.isNotEmpty()) {
            val paths: ArrayList<String> = ArrayList()
            paths.add(listItem.menuImage)
            GalleryView.show(this, paths)
        }

    }

    fun showCustomDialog(view: View) {
        val bottomSheetDialog: MenuAgeSheetFragment = MenuAgeSheetFragment.newInstance()
        bottomSheetDialog.show(supportFragmentManager, "finalbasket fragment")
    }

    interface onTipListener {
        fun onTipPicked(tipamout: String)
    }

    override fun onStarted() {
        showProgressBar()
    }

    override fun onCreateBookingSuccess(response: CreateBookingResponse) {
        hideProgressBar()
        Successtoast(response.message)
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra(ConstantLib.FROM, ConstantLib.FINALBASKET)
        intent.putExtra(ConstantLib.PURCHASE_AMOUNT, response.data.booking.amount.toString())
        intent.putExtra(ConstantLib.BOOKING_ID, response.data.booking.bookingId.toString())
        startActivity(intent)
        Animatoo.animateSlideLeft(this)
    }

    override fun onCreateBookingFailure(message: String) {
        hideProgressBar()
        Errortoast(message)
    }

    override fun onFailure(message: String) {
        hideProgressBar()
        Errortoast(message)
    }

    override fun sessionExpired(message: String) {
        hideProgressBar()
        Utility.logOut(applicationContext, message)
    }
}
