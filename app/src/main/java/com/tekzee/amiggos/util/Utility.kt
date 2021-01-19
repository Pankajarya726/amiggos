package com.tekzee.amiggos.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Base64
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import org.json.JSONArray
import java.math.RoundingMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap


class Utility {

    companion object {

        var jsonArray: JSONArray = JSONArray()

//        fun showDialogBooking(title: String, context: Context?, ridelist: Ridelist) {
//            val dialog = Dialog(context!!)
//            dialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);
//            dialog.setContentView(R.layout.custom_booking_dialog);
//            dialog.getWindow()!!.setLayout(-1, -2);
//            dialog.setCancelable(false);
//            dialog.getWindow()!!.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            val txttitle = dialog.findViewById(R.id.title) as TextView
//            txttitle.text = title
//            val userName = dialog.findViewById(R.id.name) as TextView
//            userName.text = ridelist.user_name
//            val mobile = dialog.findViewById(R.id.mobile) as TextView
//            mobile.text = ridelist.user_num
//
//            val btnCarreras = dialog.findViewById(R.id.cerrar) as Button
//            btnCarreras.setOnClickListener {
//                dialog.dismiss()
//            }
//            dialog.show()
//        }




        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }



        fun checkMinimumAndMaximumPasswordCharacter(password: String): Boolean {
            return password.length in 7..15
        }

        fun checkFirstName_lastname_phone_CharacterCount(email: String): Boolean {
            return email.length <=15
        }

        fun checkMinimumAndMaximumUsernameCharacter(username: String): Boolean {
            return username.length in 4..13
        }

        fun checkEmailCharacter(email: String): Boolean {
            return email.length <=255
        }


        fun createHeaders(sharedPreferences: SharedPreference?): HashMap<String, String?> {
            val tz = TimeZone.getDefault()
            val headers = HashMap<String, String?>()
            headers["language-code"] = if(sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_CODE).isNullOrEmpty()){"en"}else{
                sharedPreferences.getValueString(ConstantLib.LANGUAGE_CODE)}
            headers["api-key"] = sharedPreferences.getValueString(ConstantLib.API_TOKEN)
            headers["device_type"] = ConstantLib.DEVICETYPE
            headers["time_zone"] = tz.id.toString()
            return headers
        }

        fun getShaKey(context: Context){
            try {
                val info = context.packageManager.getPackageInfo(
                    "com.tekzee.amiggos",
                    PackageManager.GET_SIGNATURES
                )
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Logger.d("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            } catch (e: PackageManager.NameNotFoundException) {

            } catch (e: NoSuchAlgorithmException) {

            }
        }

        fun getCurrentLanguageCode(sharedPreferences: SharedPreference?): String {
            var languageCode = "en"
            if(sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_CODE).isNullOrEmpty()){
                languageCode = "en"
            }else{
                languageCode = sharedPreferences.getValueString(ConstantLib.LANGUAGE_CODE).toString()
            }
            return languageCode
        }




//        fun createSampleData(applicationContext: Context): ArrayList<CountryCodeModel> {
//            var countryJson = JSONObject(loadJSONFromAsset(applicationContext))
//            var countryJsonArray = countryJson.getJSONArray("countries")
//            var countryCode = ArrayList<CountryCodeModel>();
//
//            for (i in 0 until countryJsonArray.length()) {
//                val item = countryJsonArray.getJSONObject(i)
//                countryCode.add(CountryCodeModel(item.getString("code"), item.getString("name")))
//            }
//
//            return countryCode
//        }
//
//        private fun loadJSONFromAsset(applicationContext: Context): String? {
//            var json: String? = null
//            try {
//                val isdata = applicationContext.getAssets().open("countrycode.json")
//                val size = isdata.available()
//                val buffer = ByteArray(size)
//                isdata.read(buffer)
//                isdata.close()
//                json = String(buffer)
//            } catch (ex: IOException) {
//                ex.printStackTrace()
//                return null
//
//            }
//            return json
//        }


        fun roundOffDecimal(number: Double): Double? {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        fun logOut(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            val sharedPreference = SharedPreference(context)
            FirebaseAuth.getInstance().signOut()
            sharedPreference.clearSharedPreference()
            val intent = Intent(context, MainSplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun showLogoutPopup(context: Context, message: String) {

            val sharedPreference = SharedPreference(context)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            sharedPreference.clearSharedPreference()
            val intent = Intent(context, MainSplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)

//            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText(message)
//                .setConfirmText(languageData!!.klOk)
//                .setConfirmClickListener { sDialog ->
//                    sDialog.dismissWithAnimation()
//                    FirebaseAuth.getInstance().signOut()
//                    sharedPreference.clearSharedPreference()
//                    val intent = Intent(context, MainSplashActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent)
//                }
//                .show()
        }

        fun checkNullorEmptyData(data: String?): String {
            return data ?: ""
        }

        fun addSelectedId(id: Int, isChecked: Boolean){
            if(!idExist(id)){
                jsonArray.put(id)
            }else if(isChecked){
                jsonArray.remove(id)
            }else{
                jsonArray.put(id)
            }

        }

        fun idExist(id: Int): Boolean {
            if (jsonArray.length()>0){
                for(i in 0..jsonArray.length()){
                    return id == jsonArray.get(i)
                }
            }

            return false
        }
        fun expiryMonthValidator(expiryMonth: String): Boolean {
            return try {
                val month = expiryMonth.toInt()
                if (month < 13) true else false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun expiryYearValidator(expiryYear: String): Boolean {
            return try {
                val year = expiryYear.toInt()
                val currentYear = Calendar.getInstance()[Calendar.YEAR]
                if (currentYear >= year) true else false
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                false
            }
        }

        fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
            try {
                val sdf = SimpleDateFormat(
                    "dd-MMM",
                    Locale.ENGLISH
                )
                val netDate = Date(epoc)
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }

        fun checkProfileComplete(sharedPreference: SharedPreference?): Boolean {
            return sharedPreference!!.getValueInt(ConstantLib.ISPROFILECOMPLETE)==1
        }
    }



}







