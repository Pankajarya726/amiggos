package com.tekzee.amiggos.ui.postmemories.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.tekzee.amiggos.FileProgressReceiver
import com.tekzee.amiggos.base.repository.MemorieRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.util.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.io.File

class FileUploadService : JobIntentService(),KodeinAware {

    override val kodein: Kodein by closestKodein()
    val prefs: SharedPreference by instance<SharedPreference>()
    val repository: MemorieRepository by instance<MemorieRepository>()

    var mDisposable: Disposable? = null
    var imageUri: String? = null
    var filename:String ? = null
    var file:File? = null
    var requestFile: RequestBody?=null
    var type: Int?=1

    var mNotificationHelper: NotificationHelper? = null
    override fun onCreate() {
        super.onCreate()
        mNotificationHelper = NotificationHelper(this)
    }

    override fun onHandleWork(intent: Intent) {
        try{
            Log.d(TAG, "onHandleWork: ")
            /**
             * Download/Upload of file
             * The system or framework is already holding a wake lock for us at this point
             */
            // get file file here
            if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
                type=2
                val uri: String = intent.getStringExtra(ConstantLib.FILEURI)!!
                imageUri = uri
                file = File(imageUri)
                filename = file!!.name+".mp4"
                requestFile = RequestBody.create(
                    "video/mp4".toMediaTypeOrNull(),
                    file!!
                )
            }else{

                type=1
                if(intent.getStringExtra(ConstantLib.FILEURI) != null){
                    val uri: Uri = intent.getStringExtra(ConstantLib.FILEURI)!! as Uri
                    imageUri = uri.path
                }



            }

            val useridRequestBody = RequestBody.create(
                MultipartBody.FORM,
                prefs.getValueInt(ConstantLib.USER_ID).toString()
            )
            val typeRequestBody = RequestBody.create(
                MultipartBody.FORM,
                prefs.getValueString(ConstantLib.TYPE)!!
            )
            val venueidRequestBody = RequestBody.create(
                MultipartBody.FORM,
                prefs.getValueInt(ConstantLib.USER_ID).toString()
            )
            val taggedRequestBody = RequestBody.create(
                MultipartBody.FORM,
                intent.getStringExtra(ConstantLib.TAGGED_ARRAY)!!
            )

            val fileObservable =
                Flowable.create<Double>({ emitter ->
                    repository.CreateMemorieApi(createMultipartBody(imageUri!!, emitter),useridRequestBody,typeRequestBody,venueidRequestBody,taggedRequestBody,
                        Utility.createHeaders(prefs)).blockingGet()
                    emitter.onComplete()
                }, BackpressureStrategy.LATEST)
            mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ progress -> // call onProgress()
                    onProgress(progress)
                }, { throwable -> // call onErrors() if error occurred during file upload
                    onErrors(throwable)
                }) { // call onSuccess() while file upload successful

                    if (file!!.exists()) {
                        if (file!!.delete()) {
                            System.out.println("file Deleted :" + file)
                        } else {
                            System.out.println("file not Deleted :" + file)
                        }
                    }
                    this@FileUploadService.onSuccess()
                }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun onErrors(throwable: Throwable) {
        /**
         * Error occurred in file uploading
         */
        val successIntent = Intent("com.wave.ACTION_CLEAR_NOTIFICATION")
        successIntent.putExtra("notificationId", NOTIFICATION_ID)
        sendBroadcast(successIntent)
        val resultPendingIntent: PendingIntent?
        if(prefs.getValueString(ConstantLib.TYPE).equals("3")){
            resultPendingIntent = PendingIntent.getActivity(
                this,
                0 /* Request code */, Intent(this, AHomeScreen::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }else{
            resultPendingIntent = PendingIntent.getActivity(
                this,
                0 /* Request code */, Intent(this, AHomeScreen::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }



        /**
         * Add retry action button in notification
         */
//        val retryIntent = Intent(this, RetryJobReceiver::class.java)
//        retryIntent.putExtra("notificationId", NOTIFICATION_RETRY_ID)
//        retryIntent.putExtra("mFilePath", mFilePath)
//        retryIntent.action = ACTION_RETRY
        /**
         * Add clear action button in notification
         */
//        val clearIntent = Intent(this, RetryJobReceiver::class.java)
//        clearIntent.putExtra("notificationId", NOTIFICATION_RETRY_ID)
//        clearIntent.putExtra("mFilePath", mFilePath)
//        clearIntent.action = ACTION_CLEAR
//        val retryPendingIntent = PendingIntent.getBroadcast(this, 0, retryIntent, 0)
//        val clearPendingIntent = PendingIntent.getBroadcast(this, 0, clearIntent, 0)
        val mBuilder: NotificationCompat.Builder = mNotificationHelper!!.getNotification(
            "error_upload_failed",
            "message_upload_failed", resultPendingIntent
        )
//        // attached Retry action in notification
//        mBuilder.addAction(
//            R.drawable.ic_menu_revert, getString(R.string.btn_retry_not),
//            retryPendingIntent
//        )
//        // attached Cancel action in notification
//        mBuilder.addAction(
//            R.drawable.ic_menu_revert, getString(R.string.btn_cancel_not),
//            clearPendingIntent
//        )
        // Notify notification
        mNotificationHelper!!.notify(NOTIFICATION_RETRY_ID, mBuilder)
    }

    /**
     * Send Broadcast to FileProgressReceiver with progress
     *
     * @param progress file uploading progress
     */
    private fun onProgress(progress: Double) {
        val progressIntent = Intent(this, FileProgressReceiver::class.java)
        progressIntent.action = "com.wave.ACTION_PROGRESS_NOTIFICATION"
        progressIntent.putExtra("notificationId", NOTIFICATION_ID)
        progressIntent.putExtra("progress", (100 * progress).toInt())
        sendBroadcast(progressIntent)
    }

    /**
     * Send Broadcast to FileProgressReceiver while file upload successful
     */
    private fun onSuccess() {
        val successIntent = Intent(this, FileProgressReceiver::class.java)
        successIntent.action = "com.wave.ACTION_UPLOADED"
        successIntent.putExtra("notificationId", NOTIFICATION_ID)
        successIntent.putExtra("progress", 100)
        sendBroadcast(successIntent)
    }

    private fun createRequestBodyFromFile(
        file: File,
        mimeType: String
    ): RequestBody {
        return RequestBody.create(mimeType.toMediaTypeOrNull(), file)
    }

    private fun createRequestBodyFromText(mText: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), mText)
    }

    /**
     * return multi part body in format of FlowableEmitter
     */
    private fun createMultipartBody(
        filePath: String,
        emitter: FlowableEmitter<Double>
    ): MultipartBody.Part {
        val file = File(filePath)
        if(type==1){
            return MultipartBody.Part.createFormData(
                "memory", file.name,
                createCountingRequestBody(file, MIMEType.IMAGE.value, emitter)
            )
        }else{

            return MultipartBody.Part.createFormData(
                "memory", file.name,
                createCountingRequestBody(file, MIMEType.VIDEO.value, emitter)
            )
        }
    }

    private fun createCountingRequestBody(
        file: File, mimeType: String,
        emitter: FlowableEmitter<Double>
    ): RequestBody {
        val requestBody = createRequestBodyFromFile(file, mimeType)
        return CountingRequestBody(requestBody
        ) { bytesWritten, contentLength ->
            val progress = 1.0 * bytesWritten / contentLength
            emitter.onNext(progress)
        }
    }

    companion object {
        private const val TAG = "FileUploadService"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_RETRY_ID = 2

        /**
         * Unique job ID for this service.
         */
        private const val JOB_ID = 102
        fun enqueueWork(context: Context?, intent: Intent?) {
            enqueueWork(
                context!!,
                FileUploadService::class.java,
                JOB_ID,
                intent!!
            )
        }
    }
}