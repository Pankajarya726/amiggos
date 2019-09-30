package com.tekzee.mallortaxi.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import cn.pedant.SweetAlert.SweetAlertDialog
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.mallortaxiclient.constant.ConstantLib
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern


class Utility {

    companion object {
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


        fun createHeaders(sharedPreferences: SharedPreference?): HashMap<String, String?> {
            val headers = HashMap<String, String?>()
            headers["language-code"] = if(sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_CODE).isNullOrEmpty()){"en"}else{
                sharedPreferences.getValueString(ConstantLib.LANGUAGE_CODE)}
            headers["api-key"] = sharedPreferences.getValueString(ConstantLib.API_TOKEN)
            headers["device_type"] = ConstantLib.DEVICETYPE
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
                    Logger.d("KeyHash:"+ Base64.encodeToString(md.digest(), Base64.DEFAULT))
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
                languageCode = sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_CODE).toString()
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

        fun showLogoutPopup(context: Context,languageData: LanguageData,message: String) {

            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(message)
                .setConfirmText(languageData.klOk)
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                }
                .show()
        }

        fun checkNullorEmptyData(data: String?): String {
            return if(data == null && data!!.isBlank()){
                ""
            }else{
                data
            }
        }


    }


}