package com.tekzee.amiggos.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tekzee.amiggos.base.repository.MemorieRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen

import com.tekzee.amiggos.ui.postmemories.service.FileUploadService
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


class UploadWorkOurMemoryService(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
), KodeinAware {

    private val TAG = UploadWorkOurMemoryService::class.java.simpleName
    override val kodein: Kodein by closestKodein(context)
    val prefs: SharedPreference by instance<SharedPreference>()
    val repository: MemorieRepository by instance<MemorieRepository>()

    var mDisposable: Disposable? = null
    var imageUri: String? = null
    var filename:String ? = null
    var file:File? = null
    var requestFile: RequestBody?=null
    var type: Int?=1
    var mNotificationHelper: NotificationHelper? = null



    override fun doWork(): Result {
        val inputData = inputData
        uploadFileToServer(inputData)
        return Result.success()
    }


    fun uploadFileToServer(inputData: Data) {

        Log.d(TAG, "inputData ----> "+inputData.toString())
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */
        // get file file here
        if (inputData.getString(ConstantLib.FROM).equals("VIDEO", true)) {
            type = 2
            val uri: String = inputData.getString(ConstantLib.FILEURI).toString()
            imageUri = uri
            file = File(imageUri)
            filename = file!!.name + ".mp4"
            requestFile = RequestBody.create(
                "video/mp4".toMediaTypeOrNull(),
                file!!
            )
        } else {
            type = 1
            val uri: Uri = Uri.parse(inputData.getString(ConstantLib.FILEURI))
            imageUri = uri.path
        }

        val userid = prefs.getValueInt(ConstantLib.USER_ID)
        Log.e("userid---->",userid.toString())
        val useridRequestBody = RequestBody.create(
            MultipartBody.FORM,
            prefs.getValueInt(ConstantLib.USER_ID).toString()
        )

        val selectedFriends = RequestBody.create(
            MultipartBody.FORM,
            inputData.getString(ConstantLib.SELECTEDFRIENDS)!!
        )

        val ourstoridid = RequestBody.create(
            MultipartBody.FORM,
            inputData.getString(ConstantLib.OURSTORYID)!!
        )

        val taggedRequestBody = RequestBody.create(
            MultipartBody.FORM,
            inputData.getString(ConstantLib.TAGGED_ARRAY)!!
        )





//        input: MultipartBody.Part,
//        useridRequestBody: RequestBody,
//        selectedFriends: RequestBody,
//        ourstoridid: RequestBody,
//        taggedRequestBody: RequestBody,
//        createHeaders: HashMap<String, String?>


        val fileObservable =
            Flowable.create<Double>({ emitter ->
                repository.CreateOurMemorieApi(
                    createMultipartBody(imageUri!!, emitter),
                    useridRequestBody,
                    selectedFriends,
                    ourstoridid,
                    taggedRequestBody,
                    Utility.createHeaders(prefs)
                ).blockingGet()
                emitter.onComplete()
            }, BackpressureStrategy.LATEST)
        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ progress -> // call onProgress()
                onProgress(progress)
            }, { throwable -> // call onErrors() if error occurred during file upload
                onErrors(throwable)
            }) { // call onSuccess() while file upload successful

                this@UploadWorkOurMemoryService.onSuccess()
            }

    }

    private fun onErrors(throwable: Throwable) {

        mNotificationHelper = NotificationHelper(applicationContext)
        val successIntent = Intent("com.wave.ACTION_CLEAR_NOTIFICATION")
        successIntent.putExtra("notificationId", FileUploadService.NOTIFICATION_ID)
        applicationContext.sendBroadcast(successIntent)

        val resultPendingIntent: PendingIntent?
        if(prefs.getValueString(ConstantLib.TYPE).equals("3")){
           resultPendingIntent = PendingIntent.getActivity(
                applicationContext,
                0 /* Request code */, Intent(applicationContext, AHomeScreen::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }else{
            resultPendingIntent = PendingIntent.getActivity(
                applicationContext,
                0 /* Request code */, Intent(applicationContext, AHomeScreen::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

       val mBuilder: NotificationCompat.Builder = mNotificationHelper!!.getNotification(
            "error_upload_failed",
            "message_upload_failed", resultPendingIntent
        )
        mNotificationHelper!!.notify(FileUploadService.NOTIFICATION_RETRY_ID, mBuilder)
    }

    private fun onProgress(progress: Double) {
        val progressIntent = Intent(applicationContext, FileProgressReceiver::class.java)
        progressIntent.action = "com.wave.ACTION_PROGRESS_NOTIFICATION"
        progressIntent.putExtra("notificationId", FileUploadService.NOTIFICATION_ID)
        progressIntent.putExtra("progress", (100 * progress).toInt())
        applicationContext.sendBroadcast(progressIntent)
    }

    /**
     * Send Broadcast to FileProgressReceiver while file upload successful
     */
    private fun onSuccess() {
        BitmapUtils.deleteFolder()
        BitmapUtils.deleteVideoFolder()
        val successIntent = Intent(applicationContext, FileProgressReceiver::class.java)
        successIntent.action = "com.wave.ACTION_UPLOADED"
        successIntent.putExtra("notificationId", FileUploadService.NOTIFICATION_ID)
        successIntent.putExtra("progress", 100)
        applicationContext.sendBroadcast(successIntent)
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
                "story", file.name,
                createCountingRequestBody(file, MIMEType.IMAGE.value, emitter)
            )
        }else{

            return MultipartBody.Part.createFormData(
                "story", file.name,
                createCountingRequestBody(file, MIMEType.VIDEO.value, emitter)
            )
        }
    }

    private fun createCountingRequestBody(
        file: File, mimeType: String,
        emitter: FlowableEmitter<Double>
    ): RequestBody {
        val requestBody = createRequestBodyFromFile(file, mimeType)
        return CountingRequestBody(requestBody,
            CountingRequestBody.Listener { bytesWritten, contentLength ->
                val progress = 1.0 * bytesWritten / contentLength
                emitter.onNext(progress)
            })
    }


    private fun createRequestBodyFromFile(
        file: File,
        mimeType: String
    ): RequestBody {
        return RequestBody.create(mimeType.toMediaTypeOrNull(), file)
    }

}