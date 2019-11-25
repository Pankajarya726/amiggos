package com.tekzee.amiggos.ui.postmemories.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.JobIntentService
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.mallortaxi.network.ApiClient
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class FileUploadService : JobIntentService() {


    private var sharedPreferences: SharedPreference? = null
    private var disposable: Disposable? = null

    companion object {
        private val JOB_ID = 102
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, FileUploadService::class.java, JOB_ID, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = SharedPreference(this)
    }


    override fun onHandleWork(intent: Intent) {
        var imageUri: String? = null
        var filename:String ? = null
        var file:File? = null
        val requestFile: RequestBody

        if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
            val uri: String = intent.getStringExtra(ConstantLib.FILEURI)
            imageUri = uri
            file = File(imageUri)
            filename = file.name+".mp4"
            requestFile = RequestBody.create(
                "video/mp4".toMediaTypeOrNull(),
                file
            )
        }else{
            val uri: Uri = intent.getParcelableExtra(ConstantLib.FILEURI) as Uri
            imageUri = uri.path
            file = File(imageUri)
            filename = file.name
            requestFile = RequestBody.create(
                "image/jpeg".toMediaTypeOrNull(),
                file
            )

        }


        val fileMultipartBody =
            MultipartBody.Part.createFormData("story", filename, requestFile)

        val useridRequestBody = RequestBody.create(
            MultipartBody.FORM,
            intent.getStringExtra(ConstantLib.USER_ID)
        )


        disposable = ApiClient.instance.UploadFileToServer(
            fileMultipartBody, useridRequestBody,
            Utility.createHeaders(sharedPreferences)
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                val responseCode = response.code()
                when (responseCode) {
                    200 -> {
                        val responseData: AttachIdResponse? = response.body()
                        if (responseData!!.status) {
                            //            mainView.onAttachIdSuccess(responseData)
                        } else {
                            //          mainView.validateError(responseData.message.toString())
                        }
                    }
                }
            }, { error ->
                // mainView.hideProgressbar()
                // mainView.validateError(error.message.toString())
            })

    }
}