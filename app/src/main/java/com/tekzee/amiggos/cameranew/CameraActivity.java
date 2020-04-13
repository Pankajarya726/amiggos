package com.tekzee.amiggos.cameranew;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.iammert.library.cameravideobuttonlib.CameraVideoButton;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Preview;
import com.tekzee.amiggos.R;
import com.tekzee.amiggos.base.BaseActivity;
import com.tekzee.amiggos.constant.ConstantLib;
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails;
import com.tekzee.amiggos.util.SharedPreference;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CameraActivity extends BaseActivity implements CameraVideoButton.ActionListener {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private CameraView camera;
    private long mCaptureTime;
    private CameraVideoButton videoButton;
    private ViewFlipper camera_flipper,img_flash_control;
    private ImageView img_camera_back,img_camera_front,flash_on,flash_off,img_profile,img_back;
    private TextView txt_search;
    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_acvtivity);
        sharedPreference =new SharedPreference(this);
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        setupView();
        setupClickListener();
    }

    private void setupClickListener() {
        img_camera_front.setOnClickListener(v -> {
            camera_flipper.animate();
            camera_flipper.showPrevious();
            camera.setFacing(Facing.FRONT);
        });

        img_camera_back.setOnClickListener(v -> {
            camera_flipper.animate();
            camera_flipper.showNext();
            camera.setFacing(Facing.BACK);
        });

        flash_on.setOnClickListener(v -> {
            img_flash_control.animate();
            img_flash_control.showPrevious();
            camera.setFlash(Flash.ON);
        });

        flash_off.setOnClickListener(v -> {
            img_flash_control.animate();
            img_flash_control.showNext();
            camera.setFlash(Flash.OFF);
        });


        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void setupView() {
        camera = findViewById(R.id.camera);
        camera.setFlash(Flash.OFF);
        videoButton = findViewById(R.id.videobutton);
        img_back = findViewById(R.id.img_back);
        txt_search = findViewById(R.id.txt_search);
        camera_flipper = findViewById(R.id.camera_flipper);
        img_flash_control = findViewById(R.id.img_flash_control);
        img_camera_back = findViewById(R.id.img_camera_back);
        img_camera_front = findViewById(R.id.img_camera_front);
        flash_on = findViewById(R.id.flash_on);
        flash_off = findViewById(R.id.flash_off);
        img_profile = findViewById(R.id.img_profile);

        camera.setLifecycleOwner(this);
        videoButton.setVideoDuration(5000);
        videoButton.setActionListener(this);
        camera.addCameraListener(new Listener());

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra(ConstantLib.PROFILE_IMAGE)).placeholder(R.drawable.user).into(img_profile);
        if(getIntent().getStringExtra(ConstantLib.FROM_ACTIVITY).equalsIgnoreCase("STORIEVIEWACTIVITY")){
            txt_search.setVisibility(View.GONE);
            img_profile.setVisibility(View.GONE);
        }else{
            txt_search.setVisibility(View.GONE);
            img_profile.setVisibility(View.VISIBLE);
        }


        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AProfileDetails.class);
                intent.putExtra(ConstantLib.FRIEND_ID, ""+sharedPreference.getValueInt(ConstantLib.USER_ID));
                intent.putExtra(ConstantLib.PROFILE_IMAGE, sharedPreference.getValueString(ConstantLib.PROFILE_IMAGE));
                startActivity(intent);

            }
        });

    }

    @Override
    public void onDurationTooShortError() {

    }

    @Override
    public void onEndRecord() {
        videoButton.enableVideoRecording(false);
    }

    @Override
    public void onSingleTap() {
        capturePictureSnapshot();
    }

    @Override
    public void onStartRecord() {
        captureVideoSnapshot();
        videoButton.enableVideoRecording(true);
    }

    @Override
    public void validateError(@NotNull String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


    private class Listener extends CameraListener {

        @Override
        public void onCameraOpened(@NonNull CameraOptions options) {

        }

        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
            Log.d( TAG,"Got CameraException #" + exception.getReason());

        }

        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            super.onPictureTaken(result);
            if (camera.isTakingVideo()) {
                Log.d(TAG,"Captured while taking video. Size=" + result.getSize());
                return;
            }

            // This can happen if picture was taken with a gesture.
            long callbackTime = System.currentTimeMillis();
            if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
            Log.d(TAG,"onPictureTaken called! Launching activity. Delay:");
            PicturePreviewActivity.setPictureResult(result);
            Intent intent = new Intent(CameraActivity.this, PicturePreviewActivity.class);
            intent.putExtra("delay", callbackTime - mCaptureTime);
            intent.putExtra(ConstantLib.OURSTORYID,getIntent().getStringExtra(ConstantLib.OURSTORYID));
            intent.putExtra(ConstantLib.FROM_ACTIVITY,getIntent().getStringExtra(ConstantLib.FROM_ACTIVITY));
            startActivity(intent);
            mCaptureTime = 0;
            Log.d(TAG,"onPictureTaken called! Launched activity.");
        }

        @Override
        public void onVideoTaken(@NonNull VideoResult result) {
            super.onVideoTaken(result);

            Log.d(TAG,"onVideoTaken called! Launching activity.");
            VideoPreviewActivity.setVideoResult(result);
            Intent intent = new Intent(CameraActivity.this, VideoPreviewActivity.class);
            startActivity(intent);
            Log.d(TAG,"onVideoTaken called! Launched activity.");
        }

        @Override
        public void onVideoRecordingStart() {
            super.onVideoRecordingStart();
            Log.d(TAG,"onVideoRecordingStart!");
        }

        @Override
        public void onVideoRecordingEnd() {
            super.onVideoRecordingEnd();
            Log.d(TAG,"Video taken. Processing...");
            Log.d(TAG,"onVideoRecordingEnd!");
        }

        @Override
        public void onExposureCorrectionChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers);
            Log.d(TAG,"Exposure correction:" + newValue);
        }

        @Override
        public void onZoomChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onZoomChanged(newValue, bounds, fingers);
            Log.d(TAG,"Zoom:" + newValue);
        }
    }





    private void capturePictureSnapshot() {
        if (camera.isTakingPicture()) return;
        if (camera.getPreview() != Preview.GL_SURFACE) {
            Log.d(TAG,"Picture snapshots are only allowed with the GL_SURFACE preview.");
            return;
        }
        mCaptureTime = System.currentTimeMillis();
        Log.d(TAG,"Capturing picture snapshot...");
        camera.takePictureSnapshot();
    }



    private void captureVideoSnapshot() {
        if (camera.isTakingVideo()) {
            Log.d(TAG,"Already taking video.");
            return;
        }
        if (camera.getPreview() != Preview.GL_SURFACE) {
            Log.d(TAG,"Video snapshots are only allowed with the GL_SURFACE preview.");
            return;
        }
        Log.d(TAG,"Recording snapshot for 5 seconds...");
        camera.takeVideoSnapshot(new File(getFilesDir(), "video.mp4"), 5000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
