//package com.tekzee.amiggos.ui.camera;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.hardware.Camera;
//import android.media.CamcorderProfile;
//import android.media.MediaRecorder;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Environment;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import androidx.core.app.ActivityCompat;
//
//import com.blogspot.atifsoftwares.animatoolib.Animatoo;
//import com.bumptech.glide.Glide;
//import com.orhanobut.logger.Logger;
//import com.tekzee.amiggos.R;
//import com.tekzee.amiggos.ui.camera.camerautil.MyCanvas;
//import com.tekzee.amiggos.ui.camera.postimagecaptured.PostImageCapturedActivity;
//import com.tekzee.amiggos.ui.groupfriends.GroupFriendActivity;
//import com.tekzee.amiggos.ui.mymemories.MyMemoriesActivity;
//import com.tekzee.amiggos.ui.searchamiggos.SearchActivity;
//import com.tekzee.amiggos.util.OnSwipeTouchListener;
//import com.tekzee.amiggos.util.SharedPreference;
//import com.tekzee.amiggos.constant.ConstantLib;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class CameraPreview extends MyCanvas {
//
//    private SurfaceView preview=null;
//    private SurfaceHolder previewHolder=null;
//    private Camera camera=null;
//    private Camera.Parameters params;
//    private boolean inPreview=false;
//    private boolean cameraConfigured=false;
//    private boolean isRecording;
//    private boolean isFlashOn;
//    private MediaRecorder mediaRecorder;
//    private static int currentCameraId = 0;
//    private Bitmap rotatedBitmap;
//    private RelativeLayout captureMedia;
//    private FrameLayout editMedia;
//    private ImageView customButton;
//    private ImageView customButtonSquare;
//    private ImageView switchCameraBtn;
//    private ImageView img_stories;
//    private ImageView img_profile;
//    private ImageView img_group;
//    private ImageView flashButton;
//    private ImageView img_back;
//    private ImageView uploadButton;
//    private TextView uploadButtonTxt;
//    private TextView txt_search;
//    private TextView badge;
//    private TextView counter;
//    private ImageView EditCaptureSwitchBtn;
//    private LinearLayout editTextBody;
////    private ImageView capturedImage;
//    private VideoView videoView;
//    private View viewright;
//    int VideoSeconds = 0;
//    private static String fileName = null;
//    int noti_id;
//    private SharedPreference sharedPreferences = null;
//    Context context;
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        GetPermission();
//        context = this;
//
//        sharedPreferences = new SharedPreference(this);
//        captureMedia = (RelativeLayout) findViewById(R.id.camera_view);
//
//
//
//        editMedia = (FrameLayout) findViewById(R.id.edit_media);
//        customButton = (ImageView) findViewById(R.id.custom_progressBar);
//        customButtonSquare = (ImageView) findViewById(R.id.custom_square);
//        switchCameraBtn = (ImageView) findViewById(R.id.img_switch_camera);
//        img_stories = (ImageView) findViewById(R.id.img_stories);
//        img_profile = (ImageView) findViewById(R.id.img_profile);
//        img_group = (ImageView) findViewById(R.id.img_group);
//        flashButton = (ImageView) findViewById(R.id.img_flash_control);
//        img_back = (ImageView) findViewById(R.id.img_back);
//        viewright = (View) findViewById(R.id.viewright);
//        uploadButton = (ImageView) findViewById(R.id.upload_media);
//        uploadButtonTxt = (TextView) findViewById(R.id.upload_media_txt);
//        badge = (TextView) findViewById(R.id.badge);
//        txt_search = (TextView) findViewById(R.id.txt_search);
//        counter = (TextView) findViewById(R.id.counter);
//        uploadButtonTxt.setText("");
//        editTextBody = (LinearLayout) findViewById(R.id.editTextLayout);
//        //selectSticker  = (LinearLayout) findViewById(R.id.select_sticker);
//
//
//        if(getIntent().getIntExtra(ConstantLib.COUNT,0)>0)
//        {
//            badge.setText(""+getIntent().getIntExtra(ConstantLib.COUNT,0));
//            badge.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            badge.setText("");
//            badge.setVisibility(View.GONE);
//        }
//
//
//        setupclickListener();
//        ImageView addText = (ImageView) findViewById(R.id.add_text);
//        ImageView addSticker = (ImageView) findViewById(R.id.add_stickers);
//        isRecording = false;
//        isFlashOn = false;
//
//        EditCaptureSwitchBtn = (ImageView) findViewById(R.id.cancel_capture);
////        capturedImage = (ImageView) findViewById(R.id.captured_image);
//        videoView = (VideoView) findViewById(R.id.captured_video);
//
//        preview=(SurfaceView)findViewById(R.id.preview);
//        previewHolder=preview.getHolder();
//        previewHolder.addCallback(surfaceCallback);
//        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//
//        noti_id = (int) ((new Date().getTime()/1000L)%Integer.MAX_VALUE);
//
//        //setting dir and VideoFile value
//
//        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
//        File file = wrapper.getDir("Video", Context.MODE_PRIVATE);
//        dir = new File(file.getAbsolutePath() + "/ammigos");
//
//
////        File sdCard = Environment.getExternalStorageDirectory();
////        dir = new File(sdCard.getAbsolutePath() + "/Opendp");
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        defaultVideo =  dir + "/defaultVideo.mp4.nomedia";
//        File createDefault = new File(defaultVideo);
//        if (!createDefault.isFile()) {
//            try {
//                FileWriter writeDefault = new FileWriter(createDefault);
//                writeDefault.append("yy");
//                writeDefault.close();
//                writeDefault.flush();
//            } catch (Exception ex) {
//            }
//        }
//
//        uploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //perform upload function here
//            }
//        });
//
//
//        customButton.setOnTouchListener(new View.OnTouchListener() {
//
//            private Timer timer = new Timer();
//            private long LONG_PRESS_TIMEOUT = 1000;
//            private boolean wasLong = false;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(getClass().getName(), "touch event: " + event.toString());
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    // touch & hold started
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            wasLong = true;
//                            // touch & hold was long
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//
//                                    customButton.setVisibility(View.INVISIBLE);
//                                    customButtonSquare.setVisibility(View.VISIBLE);
//                                }
//
//                            });
//
//                            try {
//                                startRecording();
//                            } catch (IOException e) {
//                                String message = e.getMessage();
//                                Log.i(null, "Problem " + message);
//                                mediaRecorder.release();
//                                e.printStackTrace();
//                            }
//                        }
//                    }, LONG_PRESS_TIMEOUT);
//                    return true;
//                }
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    // touch & hold stopped
//                    timer.cancel();
//                    if(!wasLong){
//                        // touch & hold was short
//                        Log.i("Click","touch & hold was short");
//                        if (isFlashOn && currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                            params = camera.getParameters();
//                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                            camera.setParameters(params);
//
//                            camera.autoFocus(new Camera.AutoFocusCallback() {
//
//                                @Override
//                                public void onAutoFocus(final boolean success, final Camera camera) {
//                                    takePicture();
//                                }
//                            });
//
//                        } else {
//                            takePicture();
//                        }
//                    } else {
//
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//
//                                customButton.setVisibility(View.VISIBLE);
//                                customButtonSquare.setVisibility(View.INVISIBLE);
//
//                            }
//                        });
//
//
//                        stopRecording();
//                        VideoCountDown.cancel();
//
//                        VideoSeconds = 0;
//                        //customButton.setProgressWithAnimation(0);
//                        wasLong = false;
//                    }
//                    timer = new Timer();
//                    return true;
//                }
//
//                return false;
//            }
//        });
//
//        preview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FocusCamera();
//            }
//        });
//
//        switchCameraBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchCamera();
//            }
//        });
//
//        EditCaptureSwitchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditCaptureSwitch();
//            }
//        });
//
//        editTextBody.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {showHideEditText();
//            }
//        });
//        addText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {showHideEditText();
//            }
//        });
//        addSticker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stickerOptions();
//            }
//        });
//        editMedia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //StickerView.invalidate();
//            }
//        });
//
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                onBackPressed();
//
//            }
//        });
//
//
//
//        Glide.with(getApplicationContext()).load(getIntent().getStringExtra(ConstantLib.PROFILE_IMAGE)).placeholder(R.drawable.noimage).into(img_profile);
//
//        if(getIntent().getStringExtra(ConstantLib.FROM_ACTIVITY).equalsIgnoreCase("STORIEVIEWACTIVITY")){
//            txt_search.setVisibility(View.GONE);
//            img_profile.setVisibility(View.GONE);            txt_search.setVisibility(View.GONE);
//
//        }else{
//            img_profile.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//
//
//    private void setupclickListener() {
//
//        viewright.setOnTouchListener(new OnSwipeTouchListener(context){
//            @Override
//            public void onSwipeLeft() {
//
//                onBackPressed();
//
//            }
//        });
//
//        img_stories.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MyMemoriesActivity.class);
//
//                startActivity(intent);
//            }
//        });
//
//        img_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), GroupFriendActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        txt_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//    }
//
//
//    @SuppressLint("NewApi")
//    public void GetPermission() {
//
//        int PERMISSION_ALL = 1;
//        String[] PERMISSIONS = {Manifest.permission.CAMERA,
//                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (!hasPermission(this, PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//            finish();
//        }
//    }
//
//    public static boolean hasPermission(Context context, String... permissions) {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    CountDownTimer VideoCountDown = new CountDownTimer(50000,1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//            if(VideoSeconds ==6){
//                VideoCountDown.onFinish();
//            }else{
//                if(VideoSeconds!=0)
//                counter.setText(""+VideoSeconds);
//            }
//            VideoSeconds++;
//
//        }
//
//        @Override
//        public void onFinish() {
//            stopRecording();
//            VideoSeconds = 0;
//            counter.setText("");
//        }
//    };
//
//    public void FocusCamera(){
//        if (camera.getParameters().getFocusMode().equals(
//                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//        } else {
//            camera.autoFocus(new Camera.AutoFocusCallback() {
//
//                @Override
//                public void onAutoFocus(final boolean success, final Camera camera) {
//                }
//            });
//        }
//    }
//
//    private void takePicture() {
//        params = camera.getParameters();
////        List<Camera.Size> sizes = params.getSupportedPictureSizes();
////
////        List<Integer> list = new ArrayList<Integer>();
////        for (Camera.Size size : params.getSupportedPictureSizes()) {
////            Log.i("ASDF", "Supported Picture: " + size.width + "x" + size.height);
////            list.add(size.height);
////        }
////
////        Camera.Size cs = sizes.get(closest(720, list));
////        Log.i("Width x Height", cs.width+"x"+cs.height);
////        params.setPictureSize(cs.width, cs.height); //1920, 1080
////
////        //params.setRotation(90);
////        camera.setParameters(params);
//        camera.takePicture(null, null, new Camera.PictureCallback() {
//
//            @Override
//            public void onPictureTaken(byte[] data, final Camera camera) {
//                Bitmap bitmap;
//                Matrix matrix = new Matrix();
//
//                //if (bitmap.getWidth() > bitmap.getHeight()) {
//                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                    matrix.postRotate(90);
//                } else {
//                    Matrix matrixMirrory = new Matrix();
//                    float[] mirrory = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
//                    matrixMirrory.setValues(mirrory);
//                    matrix.postConcat(matrixMirrory);
//                    matrix.postRotate(90);
//                }
//                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                /*} else {
//                    rotatedBitmap = bitmap;
//                }*/
//
//                if (rotatedBitmap != null) {
////                    setStickerView(0);
////                    capturedImage.setVisibility(View.VISIBLE);
////                    capturedImage.setImageBitmap(rotatedBitmap);
////                    editMedia.setVisibility(View.VISIBLE);
////                    captureMedia.setVisibility(View.GONE);
//
//                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                    camera.setParameters(params);
//                    Log.i("Image bitmap", rotatedBitmap.toString()+"-");
//                    Uri filepath = bitmapToFile(rotatedBitmap);
//
//                    Intent intentActivity = new Intent(getApplicationContext(), PostImageCapturedActivity.class);
//                    intentActivity.putExtra("BitmapImage", filepath);
//                    intentActivity.putExtra(ConstantLib.FROM,"IMAGE");
//                    intentActivity.putExtra(ConstantLib.OURSTORYID,getIntent().getStringExtra(ConstantLib.OURSTORYID));
//                    intentActivity.putExtra(ConstantLib.FROM_ACTIVITY,getIntent().getStringExtra(ConstantLib.FROM_ACTIVITY));
//                    startActivity(intentActivity);
//
//
//
//                } else {
//                    Toast.makeText(CameraPreview.this, "Failed to Capture the picture. kindly Try Again:",
//                            Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
//    }
//
//    protected void startRecording() throws IOException {
//
//        fileName = getExternalCacheDir().getAbsolutePath();
//        fileName += "/audiorecordtest.3gp";
//
//        if (camera == null) {
//            camera = Camera.open(currentCameraId);
//            Log.i("Camera","Camera open");
//        }
//        params = camera.getParameters();
//
//        if (isFlashOn && currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            camera.setParameters(params);
//        }
//
//        mediaRecorder = new MediaRecorder();
//        camera.lock();
//        camera.unlock();
//        // Please maintain sequence of following code.
//        // If you change sequence it will not work.
//        mediaRecorder.setCamera(camera);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
//
//        mediaRecorder.setPreviewDisplay(previewHolder.getSurface());
//
//        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mediaRecorder.setOrientationHint(270);
//        } else {
//            mediaRecorder.setOrientationHint(setCameraDisplayOrientation(this, currentCameraId, camera));
//        }
//        mediaRecorder.setOutputFile(defaultVideo);
//        mediaRecorder.prepare();
//        isRecording = true;
//        mediaRecorder.start();
//        VideoCountDown.start();
//    }
//
//    public void stopRecording() {
//        if (isRecording) {
//            try {
//                params = camera.getParameters();
//                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                camera.setParameters(params);
//
//                mediaRecorder.stop();
//                mediaRecorder.reset();
//                mediaRecorder.release();
//                mediaRecorder = null;
//                isRecording = false;
//
//                Intent intentActivity = new Intent(getApplicationContext(), PostImageCapturedActivity.class);
//                intentActivity.putExtra("BitmapImage",  defaultVideo);
//                intentActivity.putExtra(ConstantLib.FROM_ACTIVITY,getIntent().getStringExtra(ConstantLib.FROM_ACTIVITY));
//                intentActivity.putExtra(ConstantLib.OURSTORYID,getIntent().getStringExtra(ConstantLib.OURSTORYID));
//                Logger.d("inside Camerapreview"+getIntent().getStringExtra(ConstantLib.OURSTORYID));
//                intentActivity.putExtra(ConstantLib.FROM,"VIDEO");
//                startActivity(intentActivity);
//                finish();
//
//
//                //                playVideo();
//            } catch (RuntimeException stopException) {
//                Log.i("Stop Recoding", "Too short video");
//                takePicture();
//            }
//            camera.lock();
//        } else {
//            Log.i("Stop Recoding", "isRecording is true");
//        }
//    }
//
//
////    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {
////
////        Context mContext;
////
////        public VideoCompressAsyncTask(Context context) {
////            mContext = context;
////        }
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////
////        }
////
////
////        @Override
////        protected String doInBackground(String... paths) {
////            String filePath = null;
////            try {
////
////                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);
////
////            } catch (URISyntaxException e) {
////                e.printStackTrace();
////            }
////            return filePath;
////
////        }@Override
////        protected void onPostExecute(String compressedFilePath) {
////            super.onPostExecute(compressedFilePath);
////            Intent intentActivity = new Intent(getApplicationContext(), PostImageCapturedActivity.class);
////            intentActivity.putExtra("BitmapImage",  compressedFilePath);
////            intentActivity.putExtra(ConstantLib.FROM_ACTIVITY,getIntent().getStringExtra(ConstantLib.FROM_ACTIVITY));
////            intentActivity.putExtra(ConstantLib.OURSTORYID,getIntent().getStringExtra(ConstantLib.OURSTORYID));
////            Logger.d("inside Camerapreview"+getIntent().getStringExtra(ConstantLib.OURSTORYID));
////            intentActivity.putExtra(ConstantLib.FROM,"VIDEO");
////            startActivity(intentActivity);
////            finish();
////        }
////    }
////
////    public void playVideo() {
////        videoView.setVisibility(View.VISIBLE);
////        editMedia.setVisibility(View.VISIBLE);
////        captureMedia.setVisibility(View.GONE);
////
////        Uri video = Uri.parse(defaultVideo);
////        videoView.setVideoURI(video);
////        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////            @Override
////            public void onPrepared(MediaPlayer mp) {
////                mp.setLooping(true);
////            }
////        });
////        videoView.start();
////        preview.setVisibility(View.INVISIBLE);
////        setStickerView(1);
////    }
//
//    public void saveMedia(View v) throws IOException {
//        if (!videoView.isShown()) {
//            Toast.makeText(this, "Saving...",Toast.LENGTH_SHORT).show();
//            File sdCard = Environment.getExternalStorageDirectory();
//            dir = new File(sdCard.getAbsolutePath() + "/ammigos");
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            String timeStamp = new SimpleDateFormat("ddMMyyHHmm").format(new Date());
//            String ImageFile = "opendp-" + timeStamp + ".jpg"; //".png";
//            File file = new File(dir, ImageFile);
//
//            try {
//                FileOutputStream fos = new FileOutputStream(file);
////                stickerView.createBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos);
//                refreshGallery(file);
//                Toast.makeText(this, "Saved!",Toast.LENGTH_LONG).show();
//            } catch (FileNotFoundException e) {
//                Toast.makeText(this, "Error saving!",Toast.LENGTH_LONG).show();
//                Log.d("", "File not found: " + e.getMessage());
//            }
//        } else {
//            if (defaultVideo != null) {
//
//
//
//
//                String timeStamp = new SimpleDateFormat("ddMMyyHHmm").format(new Date());
//                String VideoFile = "opendp-" + timeStamp + ".mp4";
//
//
//
//
//                File from = new File(defaultVideo);
//                File to = new File(dir,VideoFile);
//
//                InputStream in = new FileInputStream(from);
//                OutputStream out = new FileOutputStream(to);
//
//                byte[] buf = new byte[1024];
//                int len;
//                while ((len = in.read(buf)) > 0) {
//                    out.write(buf, 0, len);
//                }
//                in.close();
//                out.close();
//                refreshGallery(to);
//                Toast.makeText(this, "Saved!",Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Error saving!",Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    public void refreshGallery(File file) {
//        Intent mediaScanIntent = new Intent(
//                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        mediaScanIntent.setData(Uri.fromFile(file));
//        sendBroadcast(mediaScanIntent);
//    }
//
//    public void FlashControl(View v) {
//        Log.i("Flash", "Flash button clicked!");
//        boolean hasFlash = getApplicationContext().getPackageManager()
//                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//        if (!hasFlash) {
//            AlertDialog alert = new AlertDialog.Builder(CameraPreview.this)
//                    .create();
//            alert.setTitle("Error");
//            alert.setMessage("Sorry, your device doesn't support flash light!");
//            alert.setButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//            alert.show();
//            return;
//        } else {
//
//            if (!isFlashOn) {
//                isFlashOn = true;
//                flashButton.setImageResource(R.drawable.flash_button);
//                Log.i("Flash", "Flash On");
//
//            } else {
//                isFlashOn = false;
//                flashButton.setImageResource(R.drawable.flash_button);
//                Log.i("Flash", "Flash Off");
//            }
//        }
//    }
//
//    public void switchCamera() {
//        if (!isRecording) {
//            if (camera.getNumberOfCameras() != 1) {
//                camera.release();
//                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
//                } else {
//                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
//                }
//                camera = Camera.open(currentCameraId);
//                try {
//                    camera.setPreviewDisplay(previewHolder);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                startPreview();
//            }
//        } else {
//            Log.i("Switch Camera","isRecording true");
//        }
//    }
//
//    public void EditCaptureSwitch() {
//        preview.setVisibility(View.VISIBLE);
//        captureMedia.setVisibility(View.VISIBLE);
//        //capturedImage.setImageResource(android.R.color.transparent);
//        startPreview(); //onResume();
////        capturedImage.setVisibility(View.GONE);
//        editMedia.setVisibility(View.GONE);
//        videoView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (selectSticker.getVisibility() == View.VISIBLE) {
//            stickerOptions();
//        } else if(editTextBody.getVisibility() == View.VISIBLE) {
//            showHideEditText();
//        } else if (editMedia.getVisibility() == View.VISIBLE) {
//            EditCaptureSwitch();
//            removeAllStickers();
//        } else {
//            finish();
//        }
//        Animatoo.animateSlideLeft(context); //fire the slide left animation
//    }
//
//    @SuppressLint("NewApi")
//    @Override
//    public void onResume() {
//        super.onResume();
//        currentCameraId = 0;
//        camera=Camera.open(currentCameraId);
//        try {
//            camera.setPreviewDisplay(previewHolder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        startPreview();
//        //FocusCamera();
//    }
//
//    @Override
//    public void onPause() {
//        if (inPreview) {
//            camera.stopPreview();
//        }
//
//        camera.release();
//        camera=null;
//        inPreview=false;
//
//        super.onPause();
//    }
//
//    public int closest(int of, List<Integer> in) {
//        int min = Integer.MAX_VALUE;
//        int closest = of;
//        int position=0;
//        int i = 0;
//
//        for (int v: in) {
//            final int diff = Math.abs(v - of);
//            i++;
//
//            if(diff < min) {
//                min = diff;
//                closest = v;
//                position = i;
//            }
//        }
//        int rePos = position - 1;
//        Log.i("Value",closest+"-"+rePos);
//        return rePos;
//    }
//
//    private void initPreview() {
//        if (camera!=null && previewHolder.getSurface()!=null) {
//            try {
//                camera.stopPreview();
//                camera.setPreviewDisplay(previewHolder);
//            }
//            catch (Throwable t) {
//                Log.e("Preview:surfaceCallback", "Exception in setPreviewDisplay()", t);
//                Toast.makeText(CameraPreview.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//            if (!cameraConfigured) {
//
//                Camera.Parameters parameters = camera.getParameters();
//                List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
//
//                List<Integer> list = new ArrayList<Integer>();
//                for (int i=0; i < sizes.size(); i++) {
//                    Log.i("ASDF", "Supported Preview: " + sizes.get(i).width + "x" + sizes.get(i).height);
//                    list.add(sizes.get(i).width);
//                }
//                Camera.Size cs = getOptimalPreviewSize(sizes,preview.getWidth(),preview.getHeight());
//
//                Log.i("Width x Height", cs.width+"x"+cs.height);
//
//                parameters.setPreviewSize(cs.width, cs.height);
//                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//                camera.setParameters(parameters);
//
//                cameraConfigured=true;
//            }
//        }
//    }
//
//    private void startPreview() {
//        if (cameraConfigured && camera!=null) {
//            camera.setDisplayOrientation(setCameraDisplayOrientation(this, currentCameraId, camera));
//            camera.startPreview();
//            inPreview=true;
//        }
//    }
//
//    private int setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
//        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
//        android.hardware.Camera.getCameraInfo(cameraId, info);
//        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//        switch (rotation) {
//            case Surface.ROTATION_0: degrees = 0; break;
//            case Surface.ROTATION_90: degrees = 90; break;
//            case Surface.ROTATION_180: degrees = 180; break;
//            case Surface.ROTATION_270: degrees = 270; break;
//        }
//        int result;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;
//        } else {
//            result = (info.orientation - degrees + 360) % 360;
//        }
//        return result;
//    }
//
//    SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
//        public void surfaceCreated(SurfaceHolder holder) {
//            // no-op -- wait until surfaceChanged()
//        }
//
//        public void surfaceChanged(SurfaceHolder holder,
//                                   int format, int width,
//                                   int height) {
//            initPreview();
//            startPreview();
//        }
//
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            // no-op
//        }
//
//
//    };
//
//
//    public Uri bitmapToFile(Bitmap bitmap){
//        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
//
//        File file = wrapper.getDir("Images", Context.MODE_PRIVATE);
//        file = new File(file,"${UUID.randomUUID()}.jpg");
//        try{
//            // Compress the bitmap and save in jpg format
//            OutputStream stream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG,30,stream);
//            stream.flush();
//            stream.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return Uri.parse(file.getAbsolutePath());
//    }
//
//
//
//    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
//        final double ASPECT_TOLERANCE = 0.1;
//        double targetRatio=(double)h / w;
//
//        if (sizes == null) return null;
//
//        Camera.Size optimalSize = null;
//        double minDiff = Double.MAX_VALUE;
//
//        int targetHeight = h;
//
//        for (Camera.Size size : sizes) {
//            double ratio = (double) size.width / size.height;
//            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
//            if (Math.abs(size.height - targetHeight) < minDiff) {
//                optimalSize = size;
//                minDiff = Math.abs(size.height - targetHeight);
//            }
//        }
//
//        if (optimalSize == null) {
//            minDiff = Double.MAX_VALUE;
//            for (Camera.Size size : sizes) {
//                if (Math.abs(size.height - targetHeight) < minDiff) {
//                    optimalSize = size;
//                    minDiff = Math.abs(size.height - targetHeight);
//                }
//            }
//        }
//        return optimalSize;
//    }
//
//
//
//
//}